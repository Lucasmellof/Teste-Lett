package com.lucasmellof.teste;

import java.net.http.HttpClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 04/10/2023
 */

// Classe de utilidades
public class Helpers {
    // Expressão regular para pegar a URL base
    public static final Pattern BASE_URL_REGEX = Pattern.compile("^(https?://[^/]+)");
    // Expressão regular para validar a URL
    public static final Pattern URL_VALIDATION_REGEX = Pattern.compile("(https?)://[^\\s/$.?#].\\S*");
    //
    public static final Pattern NETSHOES_REGEX =
            Pattern.compile("/https?://(?:www\\.)?netshoes\\.com\\.br/(.*)/gm");
    // Vamos utilizar o HttpClient que foi adicionado no Java 11, para fazer as requisições HTTP
    public static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    // Vamos definir um User-Agent para que o site não bloqueie a nossa requisição
    public static final String USER_AGENT = "ProductFetcher/1.0 - Java 11 - https://lucasmellof.com/";

    // Vamos criar um construtor privado para que a classe não possa ser instanciada
    public Helpers() {
        throw new IllegalStateException("Classes utilitárias não podem ser instanciadas.");
    }

    // Vamos pegar o conteúdo da página
    public static String getBaseUrl(String url) {
        Matcher matcher = BASE_URL_REGEX.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    // Vamos validar a URL utilizando expressões regulares
    public static boolean isValidUrl(String url) {
        return URL_VALIDATION_REGEX.matcher(url).matches();
    }

    public static boolean isNetshoesUrl(String url) {
        return NETSHOES_REGEX.matcher(url).matches();
    }

}
