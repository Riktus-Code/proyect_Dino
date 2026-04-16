import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminAuthService } from './admin-auth.service';

export interface UsuarioAdmin {
  id?: number;
  nombre: string;
  apellido: string;
  correo: string;
  password: string;
  rol: string;
}

@Injectable({
  providedIn: 'root',
})
export class UsuarioAdminService {
  private readonly apiUrl = this.resolveApiBaseUrl();

  constructor(
    private readonly http: HttpClient,
    private readonly adminAuthService: AdminAuthService
  ) {}

  listarTodos(): Observable<UsuarioAdmin[]> {
    return this.http.get<UsuarioAdmin[]>(this.apiUrl, {
      headers: this.getAdminHeaders(),
    });
  }

  obtenerPorId(id: number): Observable<UsuarioAdmin> {
    return this.http.get<UsuarioAdmin>(`${this.apiUrl}/${id}`, {
      headers: this.getAdminHeaders(),
    });
  }

  crear(usuario: UsuarioAdmin): Observable<UsuarioAdmin> {
    return this.http.post<UsuarioAdmin>(this.apiUrl, usuario, {
      headers: this.getAdminHeaders(),
    });
  }

  actualizar(id: number, usuario: UsuarioAdmin): Observable<UsuarioAdmin> {
    return this.http.put<UsuarioAdmin>(`${this.apiUrl}/${id}`, usuario, {
      headers: this.getAdminHeaders(),
    });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getAdminHeaders(),
    });
  }

  private getAdminHeaders(): HttpHeaders {
    const token = this.adminAuthService.getToken() ?? '';
    return new HttpHeaders({
      'X-Admin-Token': token,
    });
  }

  private resolveApiBaseUrl(): string {
    const configuredUrl = (globalThis as { __WEDDING_API_URL__?: string }).__WEDDING_API_URL__;
    if (configuredUrl) {
      return `${configuredUrl.replace(/\/$/, '')}/usuarios`;
    }

    return 'https://proyect-dino.onrender.com/api/usuarios';
  }
}
