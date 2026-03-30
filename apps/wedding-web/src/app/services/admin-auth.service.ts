import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

interface AdminLoginResponse {
  token: string;
  nombre: string;
  rol: string;
}

@Injectable({
  providedIn: 'root',
})
export class AdminAuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'session_token';
  private readonly nombreKey = 'session_nombre';
  private readonly rolKey = 'session_rol';

  constructor(private readonly http: HttpClient) {}

  login(nombre: string, password: string): Observable<AdminLoginResponse> {
    return this.http
      .post<AdminLoginResponse>(`${this.apiUrl}/admin/login`, { nombre, password })
      .pipe(tap((response) => {
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem(this.nombreKey, response.nombre);
        localStorage.setItem(this.rolKey, response.rol);
      }));
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getNombre(): string {
    return localStorage.getItem(this.nombreKey) ?? '';
  }

  isAdmin(): boolean {
    return (localStorage.getItem(this.rolKey) ?? '').toUpperCase() === 'ADMIN';
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.nombreKey);
    localStorage.removeItem(this.rolKey);
  }
}
