package com.curso.springai.pelisfinder.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.curso.springai.pelisfinder.model.PeliculaRecomendada;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/**
 * Al arrancar, leemos las descripciones de peliculas.json y almacenamos sus embeddings en memoria,
 * guardando título y año como metadatos.
 */
@Service
public class PeliculasServiceImpl implements PeliculasService, InitializingBean {

	// Formato de cada entrada: "Título (año): descripción"
	private static final Pattern TITULO_ANYO = Pattern.compile("^(.+?) \\((\\d{4})\\):");

	private final VectorStore vectorStore;
	private final JsonMapper jsonMapper;
	@Value("classpath:peliculas.json")
	private Resource peliculas;

	public PeliculasServiceImpl(VectorStore vectorStore, JsonMapper jsonMapper) {
		this.vectorStore = vectorStore;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public List<PeliculaRecomendada> buscar(String preferencia) {
		return vectorStore.similaritySearch(SearchRequest.builder()
				.query(preferencia)
				.topK(5)
				.similarityThreshold(0.5)
				.build()).stream()
				.map(d -> new PeliculaRecomendada(
						(String) d.getMetadata().get("titulo"),
						(Integer) d.getMetadata().get("anyo"),
						(int) Math.round(d.getScore() * 100)))
				.toList();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<String> descripciones = jsonMapper.readValue(peliculas.getInputStream(), new TypeReference<List<String>>() {});
		vectorStore.add(descripciones.stream().map(this::toDocument).toList());
		System.out.println("Películas almacenadas en memoria: " + descripciones.size());
	}

	private Document toDocument(String descripcion) {
		Matcher m = TITULO_ANYO.matcher(descripcion);
		if (!m.find()) {
			return new Document(descripcion, Map.of("titulo", descripcion));
		}
		return new Document(descripcion, Map.of("titulo", m.group(1), "anyo", Integer.parseInt(m.group(2))));
	}
}
