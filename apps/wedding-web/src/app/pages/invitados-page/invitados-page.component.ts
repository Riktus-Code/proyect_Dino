import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AdminAuthService } from '../../services/admin-auth.service';
import { WeddingApiService } from '../../services/wedding-api.service';

@Component({
  selector: 'app-invitados-page',
  standalone: false,
  templateUrl: './invitados-page.component.html',
  styleUrl: './invitados-page.component.css',
})
export class InvitadosPageComponent {
  nombre = '';
  apellido = '';
  enviando = false;
  error = '';

  constructor(
    public readonly adminAuthService: AdminAuthService,
    private readonly weddingApiService: WeddingApiService,
    private readonly router: Router
  ) {}

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }

  buscarInvitado(): void {
    this.error = '';
    const nombreLimpio = this.nombre.trim();
    const apellidoLimpio = this.apellido.trim();

    if (!nombreLimpio || !apellidoLimpio) {
      this.error = 'Por favor, completa nombre y apellido.';
      return;
    }

    this.enviando = true;
    this.weddingApiService.buscarInvitado(nombreLimpio, apellidoLimpio).pipe(
      finalize(() => {
        this.enviando = false;
      })
    ).subscribe({
      next: (response) => {
        if (response.invitado) {
          this.router.navigate(['/esta-invitado']);
          return;
        }
        this.router.navigate(['/no-registrado']);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 404) {
          this.router.navigate(['/no-registrado']);
          return;
        }
        this.error = 'No hemos podido validar tu invitación ahora mismo. Inténtalo de nuevo en unos minutos.';
      },
    });
  }
}
