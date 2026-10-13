package com.curso.springai.pelisfinder.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Al arrancar, leemos las descripciones de peliculas.json y almacenamos sus embeddings en el VectorStore
 */
@Service
public class PeliculasService implements InitializingBean {

	private final VectorStore vectorStore;
	private final JsonMapper jsonMapper;
	@Value("classpath:peliculas.json")
	private Resource peliculas;

	public PeliculasService(VectorStore vectorStore, JsonMapper jsonMapper) {
		this.vectorStore = vectorStore;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<String> descripciones = jsonMapper.readValue(peliculas.getInputStream(), new TypeReference<List<String>>() {});
		vectorStore.add(descripciones.stream().map(Document::new).toList());
		System.out.println("Películas almacenadas en memoria: " + descripciones.size());
	}
}
