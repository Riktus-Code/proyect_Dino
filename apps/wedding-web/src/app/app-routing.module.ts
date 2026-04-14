import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { InvitadosPageComponent } from './pages/invitados-page/invitados-page.component';
import { DetallesBodaPageComponent } from './pages/detalles-boda-page/detalles-boda-page.component';
import { ConfirmacionAsistenciaPageComponent } from './pages/confirmacion-asistencia-page/confirmacion-asistencia-page.component';
import { AdminUsuariosPageComponent } from './pages/admin-usuarios-page/admin-usuarios-page.component';
import { AdminLoginPageComponent } from './pages/admin-login-page/admin-login-page.component';
import { NoRegistradoPageComponent } from './pages/no-registrado-page/no-registrado-page.component';
import { EstaInvitadoPageComponent } from './pages/esta-invitado-page/esta-invitado-page.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'invitados', component: InvitadosPageComponent },
  { path: 'invitacion', redirectTo: 'invitados' },
  { path: 'detalles', component: DetallesBodaPageComponent },
  { path: 'confirmacion', component: ConfirmacionAsistenciaPageComponent },
  { path: 'esta-invitado', component: EstaInvitadoPageComponent },
  { path: 'admin/login', component: AdminLoginPageComponent },
  { path: 'admin/usuarios', component: AdminUsuariosPageComponent },
  { path: 'no-registrado', component: NoRegistradoPageComponent },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
