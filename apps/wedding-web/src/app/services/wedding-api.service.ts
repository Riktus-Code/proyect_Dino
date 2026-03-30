import { Injectable } from '@angular/core';

export interface ConfirmationRequest {
  nombre: string;
  email: string;
  asiste: boolean;
  acompanantes?: number;
}

@Injectable({
  providedIn: 'root',
})
export class WeddingApiService {
  getDetallesBoda(): void {
    // TODO: implementar llamada HTTP al backend.
  }

  enviarConfirmacionAsistencia(_request: ConfirmationRequest): void {
    // TODO: implementar llamada HTTP al backend.
  }
}
