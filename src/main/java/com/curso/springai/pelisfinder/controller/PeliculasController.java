package com.curso.springai.pelisfinder.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curso.springai.pelisfinder.model.PeliculaRecomendada;
import com.curso.springai.pelisfinder.service.PeliculasService;

@RestController
public class PeliculasController {
	private final PeliculasService peliculasService;

	public PeliculasController(PeliculasService peliculasService) {
		this.peliculasService = peliculasService;
	}

	@GetMapping("peliculas")
	public ResponseEntity<List<PeliculaRecomendada>> buscar(@RequestParam String preferencia) {
		return ResponseEntity.ok(peliculasService.buscar(preferencia));
	}
}
