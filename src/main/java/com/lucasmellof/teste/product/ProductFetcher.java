package com.lucasmellof.teste.product;

import com.lucasmellof.teste.Helpers;
import com.lucasmellof.teste.exceptions.AppException;
import com.lucasmellof.teste.exceptions.InvalidProductRedirectException;
import com.lucasmellof.teste.exceptions.ProductInfoNotFound;
import com.lucasmellof.teste.exceptions.ProductNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(link))
                .setHeader("User-Agent", Helpers.USER_AGENT)
                .build();

        // Vamos fazer a requisição
        var response = Helpers.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // Vamos processar alguns códigos de status
        switch (response.statusCode()) {
                // Caso a URL seja redirecionada, vamos seguir o redirecionamento e pegar o conteúdo da página
            case 301, 302 -> {
                // Vamos pegar o caminho do redirecionamento
                String path =
                        response.headers().firstValue("Location").orElseThrow(InvalidProductRedirectException::new);

                // Vamos pegar a URL base utilizando expressões regulares
                String basePath = Helpers.getBaseUrl(link);
                if (basePath == null) {
                    // Caso não encontremos a URL base, vamos lançar uma exceção
                    throw new InvalidProductRedirectException();
                }
                // URL base encontrada
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
    public Product parse() throws AppException, IOException, InterruptedException {
        // Vamos pegar o conteúdo da página.
        String content = getDataFromWeb(this.link);
        // Vamos utilizar a biblioteca Jsoup para fazer o parse do conteúdo.
        Document doc = Jsoup.parse(content);

        // Aqui vamos pegar o titulo do produto.
        Element titleElement = doc.getElementsByAttribute("data-productName").first();
        if (titleElement == null) {
            throw new ProductInfoNotFound("Título");
        }
        String title = titleElement.text();

        // Aqui vamos pegar a descrição do produto.
        Element descriptionElement =
                doc.getElementsByAttributeValue("itemprop", "description").first();
        if (descriptionElement == null) {
            throw new ProductInfoNotFound("Descrição");
        }
        Element descriptionParagraph = descriptionElement.getElementsByTag("p").first();
        if (descriptionParagraph == null) {
            throw new ProductInfoNotFound("Parágrafo de descrição");
        }
        String description = descriptionParagraph.text();

        // Aqui vamos pegar as imagens do produto.
        List<Node> nodes = doc.getElementsByClass("photo-figure").first().childNodes();
        String[] images = nodes.stream()
                .filter(it -> it.nodeName().equals("img"))
                .map(it -> it.attr("src"))
                .map(it -> it.split("\\?")[0])
                .toArray(String[]::new);

        if (images.length == 0) {
            throw new ProductInfoNotFound("Imagens");
        }

        // Aqui vamos fazer todo processo de validação para pegar o preço do produto.
        Element priceDiv = doc.getElementsByClass("default-price").first();
        if (priceDiv == null) {
            throw new ProductInfoNotFound("Div de preço");
        }
        Element priceSpan = priceDiv.getElementsByTag("span").first();
        if (priceSpan == null) {
            throw new ProductInfoNotFound("Span de preço");
        }
        Element priceStrong = priceSpan.getElementsByTag("strong").first();
        if (priceStrong == null) {
            throw new ProductInfoNotFound("Strong de preço");
        }
        String price = priceStrong.text();

        // Todos os dados encontrados, vamos retornar um objeto.
        return new Product(title, description, images, price);
    }
}
