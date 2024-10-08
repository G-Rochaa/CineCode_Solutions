namespace CineCodeV2.Application.Services
{
    using MySql.Data.MySqlClient;
    using System.Collections.Generic;
    using Microsoft.Extensions.Configuration;
    using CineCodeV2.Domain.Entities;
    using System.Data;
    using CineCodeV2.Application.Services.Interfaces;

    public class FilmeService : IFilmeService
    {
        private readonly string _connectionString;

        public FilmeService(IConfiguration configuration)
        {
            _connectionString = configuration.GetConnectionString("DefaultConnection");
        }

        public List<Filme> GetFilmes() 
        {
            var filmes = new List<Filme>();

            using (var connection = new MySqlConnection(_connectionString))
            {
                connection.Open();

                var query = "SELECT * FROM Tb_filmes";
                using (var command = new MySqlCommand(query, connection))
                {
                    using (var reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            filmes.Add(new Filme
                            {
                                Id = reader.GetInt32("id_filme"),
                                Titulo = reader.GetString("titulo"),
                                AnoLancamento = DateOnly.FromDateTime(reader.GetDateTime("ano_lancamento")),
                                Autor = reader.GetString("autor"),
                                Duracao = TimeOnly.FromTimeSpan(reader.GetTimeSpan("duracao")),
                                StatusFilme = reader.GetInt32("status_filme"),
                                Sinopse = reader.GetString("sinopse"),
                                Classificacao = reader.GetInt32("classificacao"),
                                Imagem = reader.GetString("imagem")
                            });
                        }
                    }
                }
            }

            return filmes;
        }
    }
}
