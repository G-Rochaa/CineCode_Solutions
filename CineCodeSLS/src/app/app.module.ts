import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './pages/DashBoard/dashboard.component';
import { CardsComponent } from './shared/cards/cards.component';
import { FooterComponent } from './shared/footer/footer.component';
import { CartazComponent } from './pages/em-cartaz/cartaz.component';
import { BreveComponent } from './pages/em-breve/breve.component';
import { NavComponent } from './shared/nav/nav.component';
@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    CardsComponent,
    FooterComponent,
    CartazComponent,
    BreveComponent,
    NavComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
