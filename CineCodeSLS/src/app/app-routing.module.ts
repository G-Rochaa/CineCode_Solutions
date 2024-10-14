import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/DashBoard/dashboard.component';
import { BreveComponent } from './pages/em-breve/breve.component';
import { CartazComponent } from './pages/em-cartaz/cartaz.component';

const routes: Routes = [
  {path: 'home', component: DashboardComponent},
  {path: 'em-breve', component: BreveComponent},
  {path: 'em-cartaz', component: CartazComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '**', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
