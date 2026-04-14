import { Component } from '@angular/core';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-esta-invitado-page',
  standalone: false,
  templateUrl: './esta-invitado-page.component.html',
  styleUrl: './esta-invitado-page.component.css',
})
export class EstaInvitadoPageComponent {
  constructor(public readonly adminAuthService: AdminAuthService) {}

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }
}
