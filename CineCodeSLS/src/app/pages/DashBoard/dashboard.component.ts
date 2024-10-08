import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Filme } from 'src/app/models/filme';
import { FilmeService } from 'src/app/services/filme.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  filmes: Filme[] = [];

  constructor(private filmeService: FilmeService) { }

  ngOnInit() {
    this.filmeService.getFilmes().subscribe((data: Filme[]) => {
      this.filmes = data;
    });

  }


}
