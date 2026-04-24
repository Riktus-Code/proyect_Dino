import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, timeout } from 'rxjs';

export interface ConfirmationRequest {
  nombre: string;
  apellido: string;
  alergias: string;
  acompanante: boolean;
}

export interface ConfirmationResponse {
  confirmada: boolean;
  mensaje: string;
}

export interface InvitadoLookupResponse {
  invitado: boolean;
  nombre: string;
  apellido: string;
}

@Injectable({
  providedIn: 'root',
})
export class WeddingApiService {
  private readonly apiBaseUrl = this.resolveApiBaseUrl();

  constructor(private readonly http: HttpClient) {}

  getDetallesBoda(): void {
    // TODO: implementar llamada HTTP al backend.
  }

  enviarConfirmacionAsistencia(request: ConfirmationRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiBaseUrl}/confirmacion`, request).pipe(timeout(20000));
  }

  buscarInvitado(nombre: string, apellido: string): Observable<InvitadoLookupResponse> {
    const params = new HttpParams()
      .set('nombre', nombre.trim())
      .set('apellido', apellido.trim());

    return this.http.get<InvitadoLookupResponse>(`${this.apiBaseUrl}/usuarios/buscar-invitado`, {
      params,
    }).pipe(timeout(10000));
  }

  private resolveApiBaseUrl(): string {
    return 'http://localhost:8080/api';
  }
}
