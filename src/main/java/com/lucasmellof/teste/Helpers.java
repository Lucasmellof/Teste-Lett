package com.lucasmellof.teste;

import java.net.http.HttpClient;
import java.util.regex.Pattern;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 04/10/2023
 */

// Classe de utilidades
public class Helpers {
	// Expressão regular para pegar a URL base
	public static final Pattern BASE_URL_REGEX = Pattern.compile("^(https?://[^/]+)");
	// Vamos utilizar o HttpClient que foi adicionado no Java 11, para fazer as requisições HTTP
	public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

	public Helpers() {
		throw new IllegalStateException("Classes utilitárias não podem ser instanciadas.");
	}

}
