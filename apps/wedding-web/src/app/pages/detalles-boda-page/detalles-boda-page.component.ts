import { Component } from '@angular/core';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-detalles-boda-page',
  standalone: false,
  templateUrl: './detalles-boda-page.component.html',
  styleUrl: './detalles-boda-page.component.css',
})
export class DetallesBodaPageComponent {
  constructor(public readonly adminAuthService: AdminAuthService) {}

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }
}
