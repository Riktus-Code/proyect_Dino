import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { AdminAuthService } from '../../services/admin-auth.service';
import { UsuarioAdmin, UsuarioAdminService } from '../../services/usuario-admin.service';

@Component({
  selector: 'app-admin-usuarios-page',
  standalone: false,
  templateUrl: './admin-usuarios-page.component.html',
  styleUrl: './admin-usuarios-page.component.css',
})
export class AdminUsuariosPageComponent {
  buscarId: number | null = null;
  usuarioBuscado: UsuarioAdmin | null = null;
  usuarios: UsuarioAdmin[] = [];

  usuarioForm: UsuarioAdmin = {
    nombre: '',
    apellido: '',
    correo: '',
    password: '',
    rol: 'USER',
  };

  mensajeError = '';
  mensajeOk = '';

  constructor(
    public readonly adminAuthService: AdminAuthService,
    private readonly usuarioAdminService: UsuarioAdminService,
    private readonly router: Router
  ) {
    if (!this.adminAuthService.isLoggedIn()) {
      this.router.navigate(['/admin/login']);
      return;
    }

    if (!this.adminAuthService.isAdmin()) {
      this.router.navigate(['/']);
      return;
    }

    this.cargarTodos();
  }

  onLogoutAdmin(): void {
    this.adminAuthService.logout();
    this.usuarios = [];
    this.usuarioBuscado = null;
    this.mensajeOk = 'Sesión admin cerrada.';
    this.mensajeError = '';
    this.router.navigate(['/admin/login']);
  }

  irALoginAdmin(): void {
    this.router.navigate(['/admin/login']);
  }

  cargarTodos(): void {
    this.limpiarMensajes();
    this.usuarioAdminService.listarTodos().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.usuarioBuscado = null;
      },
      error: (error) => this.procesarError(error),
    });
  }

  buscarUno(): void {
    this.limpiarMensajes();
    if (!this.buscarId) {
      this.mensajeError = 'Introduce un ID válido para buscar.';
      return;
    }

    this.usuarioAdminService.obtenerPorId(this.buscarId).subscribe({
      next: (usuario) => {
        this.usuarioBuscado = usuario;
      },
      error: (error) => this.procesarError(error),
    });
  }

  prepararEdicion(usuario: UsuarioAdmin): void {
    this.usuarioForm = {
      id: usuario.id,
      nombre: usuario.nombre,
      apellido: usuario.apellido,
      correo: usuario.correo,
      password: usuario.password,
      rol: usuario.rol,
    };
    this.mensajeOk = `Editando usuario ID ${usuario.id}`;
    this.mensajeError = '';
  }

  guardarUsuario(): void {
    this.limpiarMensajes();

    if (!this.usuarioForm.nombre || !this.usuarioForm.apellido || !this.usuarioForm.correo || !this.usuarioForm.password) {
      this.mensajeError = 'Nombre, apellido, correo y password son obligatorios.';
      return;
    }

    if (this.usuarioForm.id) {
      this.usuarioAdminService.actualizar(this.usuarioForm.id, this.usuarioForm).subscribe({
        next: () => {
          this.mensajeOk = 'Usuario actualizado correctamente.';
          this.resetFormulario();
          this.cargarTodos();
        },
        error: (error) => this.procesarError(error),
      });
      return;
    }

    this.usuarioAdminService.crear(this.usuarioForm).subscribe({
      next: () => {
        this.mensajeOk = 'Usuario creado correctamente.';
        this.resetFormulario();
        this.cargarTodos();
      },
      error: (error) => this.procesarError(error),
    });
  }

  eliminarUsuario(id?: number): void {
    this.limpiarMensajes();
    if (!id) {
      this.mensajeError = 'ID de usuario inválido.';
      return;
    }

    this.usuarioAdminService.eliminar(id).subscribe({
      next: () => {
        this.mensajeOk = 'Usuario eliminado correctamente.';
        this.cargarTodos();
      },
      error: (error) => this.procesarError(error),
    });
  }

  resetFormulario(): void {
    this.usuarioForm = {
      nombre: '',
      apellido: '',
      correo: '',
      password: '',
      rol: 'USER',
    };
  }

  private limpiarMensajes(): void {
    this.mensajeError = '';
    this.mensajeOk = '';
  }

  private procesarError(error: HttpErrorResponse): void {
    if (error.status === 401) {
      this.mensajeError = 'No autorizado. Inicia sesión como admin.';
      return;
    }

    if (error.status === 404) {
      this.mensajeError = 'Usuario no encontrado.';
      return;
    }

    this.mensajeError = 'Ha ocurrido un error en la operación.';
  }
}
