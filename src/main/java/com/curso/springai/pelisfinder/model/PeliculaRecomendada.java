package com.curso.springai.pelisfinder.model;

/**
 * Película devuelta al usuario junto con su porcentaje de coincidencia con la búsqueda
 */
public record PeliculaRecomendada(String titulo, Integer anyo, int coincidencia) {
}
