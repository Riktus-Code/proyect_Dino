import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-admin-login-page',
  standalone: false,
  templateUrl: './admin-login-page.component.html',
  styleUrl: './admin-login-page.component.css',
})
export class AdminLoginPageComponent {
  nombre = '';
  password = '';
  error = '';

  constructor(
    private readonly adminAuthService: AdminAuthService,
    private readonly router: Router
  ) {}

  onLogin(): void {
    this.error = '';
    this.adminAuthService.login(this.nombre, this.password).subscribe({
      next: (session) => {
        if ((session.rol ?? '').toUpperCase() === 'ADMIN') {
          this.router.navigate(['/admin/usuarios']);
          return;
        }

        this.router.navigate(['/']);
      },
      error: () => {
        this.error = 'No encontramos tu usuario en la base de datos.';
        this.router.navigate(['/no-registrado']);
      },
    });
  }
}
