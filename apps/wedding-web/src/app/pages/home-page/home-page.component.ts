import { Component } from '@angular/core';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
})
export class HomePageComponent {
  constructor(public readonly adminAuthService: AdminAuthService) {}

  logoutDesdeHeader(): void {
    this.adminAuthService.logout();
  }
}
