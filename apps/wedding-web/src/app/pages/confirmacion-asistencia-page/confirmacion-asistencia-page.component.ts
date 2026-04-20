import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import Swal from 'sweetalert2';
import { AdminAuthService } from '../../services/admin-auth.service';
import { ConfirmationRequest, WeddingApiService } from '../../services/wedding-api.service';

@Component({
  selector: 'app-confirmacion-asistencia-page',
  standalone: false,
  templateUrl: './confirmacion-asistencia-page.component.html',
  styleUrl: './confirmacion-asistencia-page.component.css',
})
export class ConfirmacionAsistenciaPageComponent {
  nombre = '';
  apellido = '';
  alergias = '';
  acompanante: boolean | null = null;
  enviando = false;
  error = '';
  mensajeExito = '';

  constructor(
    public readonly adminAuthService: AdminAuthService,
    private readonly weddingApiService: WeddingApiService
  ) {}

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }

  seleccionarAcompanante(valor: boolean, event: Event): void {
    const input = event.target as HTMLInputElement;
    this.acompanante = input.checked ? valor : null;
  }

  async confirmarAsistencia(): Promise<void> {
    this.error = '';
    this.mensajeExito = '';

    const nombreLimpio = this.nombre.trim();
    const apellidoLimpio = this.apellido.trim();

    if (!nombreLimpio || !apellidoLimpio) {
      this.error = 'Debes introducir nombre y apellido.';
      return;
    }

    if (this.acompanante === null) {
      this.error = 'Debes marcar si iras acompanado (Si o No).';
      return;
    }

    const acompananteSeleccionado = this.acompanante;

    const request: ConfirmationRequest = {
      nombre: nombreLimpio,
      apellido: apellidoLimpio,
      alergias: this.alergias.trim(),
      acompanante: acompananteSeleccionado,
    };

    this.enviando = true;

    try {
      const response = await firstValueFrom(this.weddingApiService.enviarConfirmacionAsistencia(request));
      if (!response) {
        this.error = 'No se ha podido guardar la confirmación. Inténtalo más tarde.';
        return;
      }

      this.mensajeExito = response.mensaje;
      void Swal.fire({
        icon: 'success',
        title: 'Confirmación registrada',
        text: response.mensaje,
        confirmButtonText: 'Perfecto',
        confirmButtonColor: '#1b6aa3',
        background: '#ffffff',
        color: '#163047',
        allowOutsideClick: false,
        allowEscapeKey: false,
        customClass: {
          popup: 'wedding-swal-popup',
          title: 'wedding-swal-title',
          confirmButton: 'wedding-swal-confirm',
        },
      });
    } catch (error) {
      const httpError = error as HttpErrorResponse;
      const message = httpError.error?.mensaje as string | undefined;

      if (httpError.status === 409) {
        void Swal.fire({
          icon: 'warning',
          title: 'Ya confirmado',
          text: message ?? 'Ya existe una confirmación previa para este usuario.',
          confirmButtonText: 'Entendido',
          confirmButtonColor: '#1b6aa3',
          background: '#ffffff',
          color: '#163047',
          allowOutsideClick: false,
          allowEscapeKey: false,
          customClass: {
            popup: 'wedding-swal-popup',
            title: 'wedding-swal-title',
            confirmButton: 'wedding-swal-confirm',
          },
        });
        this.error = message ?? 'Ya existe una confirmación previa para este usuario.';
        return;
      }

      if (httpError.status === 404) {
        void Swal.fire({
          icon: 'info',
          title: 'Usuario no invitado',
          text: message ?? 'No hemos encontrado tus datos en la lista de invitados.',
          confirmButtonText: 'Entendido',
          confirmButtonColor: '#1b6aa3',
          background: '#ffffff',
          color: '#163047',
          allowOutsideClick: false,
          allowEscapeKey: false,
          customClass: {
            popup: 'wedding-swal-popup',
            title: 'wedding-swal-title',
            confirmButton: 'wedding-swal-confirm',
          },
        });
        this.error = message ?? 'No hemos encontrado tus datos en la lista de invitados.';
        return;
      }

      this.error = 'No se ha podido guardar la confirmación. Inténtalo más tarde.';
    } finally {
      this.enviando = false;
    }
  }
}
