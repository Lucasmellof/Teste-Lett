package com.lucasmellof.teste.product;

import com.lucasmellof.teste.Helpers;
import com.lucasmellof.teste.exceptions.AppException;
import com.lucasmellof.teste.exceptions.InvalidProductRedirectException;
import com.lucasmellof.teste.exceptions.PriceNotFoundException;
import com.lucasmellof.teste.exceptions.ProductNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class ProductFetcher {

    public static ProductFetcher fetch(String link) {
        return new ProductFetcher(link);
    }

    private final String link;

    public ProductFetcher(String link) {
        this.link = link;
    }

    // Aqui vamos fazer o download do conteúdo da página, vamos tratar alguns códigos de status e vamos retornar o
    // conteúdo da página.
    private String getDataFromWeb(String link) throws IOException, InterruptedException, AppException {
        // Vamos criar uma requisição HTTP
        HttpRequest request =
                HttpRequest.newBuilder().GET().uri(URI.create(link)).build();

        // Vamos fazer a requisição
        var response = Helpers.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // Vamos processar alguns códigos de status
        switch (response.statusCode()) {
                // Caso a URL seja redirecionada, vamos seguir o redirecionamento e pegar o conteúdo da página
            case 301, 302 -> {
                System.out.println("Received code \"" + response.statusCode() + "\". Redirecting...");
                // Vamos pegar o caminho do redirecionamento
                String path =
                        response.headers().firstValue("Location").orElseThrow(InvalidProductRedirectException::new);

                // Vamos pegar a URL base utilizando expressões regulares
                Matcher matcher = Helpers.BASE_URL_REGEX.matcher(link);
                if (!matcher.find()) {
                    // Caso não encontremos a URL base, vamos lançar uma exceção
                    throw new InvalidProductRedirectException();
                }
                // URL base encontrada
                String basePath = matcher.group();

                // Vamos fazer uma recursão para pegar o conteúdo da página
                return getDataFromWeb(basePath + path);
            }
                // Caso o produto não seja encontrado, vamos lançar uma exceção
            case 404 -> throw new ProductNotFoundException();
        }
        // Agora sim, vamos retornar o conteúdo da página
        return response.body();
    }

    // Aqui todo o conteúdo da página já foi baixado, agora vamos fazer o parse do conteúdo e retornar um objeto.
    public Product parse() throws IOException, InterruptedException {
        // Vamos pegar o conteúdo da página.
        String content = getDataFromWeb(this.link);
        // Vamos utilizar a biblioteca Jsoup para fazer o parse do conteúdo.
        Document doc = Jsoup.parse(content);

        // Aqui vamos pegar o titulo do produto.
        String title = doc.getElementsByClass("product-title")
                .first()
                .getElementsByTag("h1")
                .text();

        // Aqui vamos pegar a descrição do produto.
        String description = doc.getElementsByClass("description-text")
                .first()
                .getElementsByTag("p")
                .text();

        // Aqui vamos pegar as imagens do produto.
        String[] images = doc.getElementsByClass("photo-figure").stream()
                .map(Node::firstChild)
                .filter(Objects::nonNull)
                .map(it -> it.attr("src"))
                .map(it -> it.split("\\?")[0])
                .toArray(String[]::new);

        // Aqui vamos fazer todo processo para pegar o preço do produto.
        String price = doc.getElementsByClass("price__currency").stream()
                .map(Element::text)
                .reduce(String::concat)
                .orElseThrow(PriceNotFoundException::new);

        // Todos os dados encontrados, vamos retornar um objeto.
        return new Product(title, description, images, price);
    }
}
