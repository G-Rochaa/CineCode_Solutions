import { Time } from "@angular/common";

export interface Filme {
    id: number;
    titulo: string;
    anoLancamento: Date; 
    autor: string;
    duracao: Time;   
    statusFilme: number;
    sinopse: string;
    classificacao: number;
    imagem: string;
  }
  