using System.Collections.Generic;
using CineCodeV2.Domain.Entities;

namespace CineCodeV2.Application.Services.Interfaces
{
    public interface IFilmeService
    {
        List<Filme> GetFilmes();

    }
}
