namespace CineCodeV2.Presentation.Controllers
{
    using CineCodeV2.Application.Services;
    using CineCodeV2.Application.Services.Interfaces;
    using CineCodeV2.Domain.Entities;
    using Microsoft.AspNetCore.Mvc;
    using System.Collections.Generic;

    [Route("api/[controller]")]
    [ApiController]
    public class FilmesController : ControllerBase
    {
        private readonly IFilmeService _filmeService; 

        public FilmesController(IFilmeService filmeService) 
        {
            _filmeService = filmeService;
        }

        [HttpGet]
        public ActionResult<List<Filme>> Get()
        {
            var filmes = _filmeService.GetFilmes();
            return Ok(filmes);
        }
    }
}
