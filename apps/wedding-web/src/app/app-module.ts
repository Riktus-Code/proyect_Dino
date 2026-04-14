import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { App } from './app';
import { AppRoutingModule } from './app-routing.module';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { InvitadosPageComponent } from './pages/invitados-page/invitados-page.component';
import { DetallesBodaPageComponent } from './pages/detalles-boda-page/detalles-boda-page.component';
import { ConfirmacionAsistenciaPageComponent } from './pages/confirmacion-asistencia-page/confirmacion-asistencia-page.component';
import { AdminUsuariosPageComponent } from './pages/admin-usuarios-page/admin-usuarios-page.component';
import { AdminLoginPageComponent } from './pages/admin-login-page/admin-login-page.component';
import { NoRegistradoPageComponent } from './pages/no-registrado-page/no-registrado-page.component';
import { EstaInvitadoPageComponent } from './pages/esta-invitado-page/esta-invitado-page.component';

@NgModule({
  declarations: [
    App,
    HomePageComponent,
    InvitadosPageComponent,
    DetallesBodaPageComponent,
    ConfirmacionAsistenciaPageComponent,
    AdminUsuariosPageComponent,
    AdminLoginPageComponent,
    NoRegistradoPageComponent,
    EstaInvitadoPageComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
