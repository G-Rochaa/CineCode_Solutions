import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Filme } from '../models/filme';

@Injectable({
  providedIn: 'root'
})
export class FilmeService {
  private apiUrl = 'https://localhost:7264/api/filmes'; 

  constructor(private http: HttpClient) { }

  getFilmes(): Observable<Filme[]> {
    return this.http.get<Filme[]>(this.apiUrl);
  }
}
