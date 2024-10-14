namespace CineCodeV2.Domain.Entities;

public class Filme
{
    public int Id { get; set; }
    public string Titulo { get; set; } = string.Empty;
    public DateOnly AnoLancamento { get; set; }
    public string Autor { get; set; } = string.Empty;
    public TimeOnly Duracao { get; set; }
    public int StatusFilme { get; set; }
    public string Sinopse { get; set; } = string.Empty;
    public int Classificacao { get; set; }
    public string Imagem { get; set; } = string.Empty;

}
