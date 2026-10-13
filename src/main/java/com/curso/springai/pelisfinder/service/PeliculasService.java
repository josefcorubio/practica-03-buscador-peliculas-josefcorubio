package com.curso.springai.pelisfinder.service;

import java.util.List;

import com.curso.springai.pelisfinder.model.PeliculaRecomendada;

public interface PeliculasService {
	List<PeliculaRecomendada> buscar(String preferencia);
}
