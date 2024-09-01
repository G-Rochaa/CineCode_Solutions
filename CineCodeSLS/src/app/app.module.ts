import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './pages/DashBoard/dashboard.component';
import { CardsComponent } from './shared/cards/cards.component';
import { FooterComponent } from './shared/footer/footer.component';
import { CartazComponent } from './pages/Programação/em-cartaz/cartaz.component';
import { BreveComponent } from './pages/Programação/em-breve/breve.component';
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    CardsComponent,
    FooterComponent,
    CartazComponent,
    BreveComponent],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
