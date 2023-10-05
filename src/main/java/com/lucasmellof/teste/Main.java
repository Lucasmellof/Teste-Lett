package com.lucasmellof.teste;

import com.lucasmellof.teste.exceptions.AppException;
import com.lucasmellof.teste.product.Product;
import com.lucasmellof.teste.product.ProductFetcher;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Bem vindo ao programa teste!");
        System.out.println();
        System.out.println("Digite o link do produto que deseja buscar ou digite \"ajuda\" para ver os comandos disponíveis.");


        // Vamos criar um scanner para ler os comandos
        Scanner scanner = new Scanner(System.in);

        // Vamos ler os comandos
        while (scanner.hasNextLine()) {
            // Vamos pegar o comando digitado e remover os espaços em branco
            String cmd = scanner.nextLine().trim();
            // Caso o comando esteja vazio, vamos ignorar
            if (cmd.isEmpty()) continue;

            // Vamos verificar qual comando foi digitado
            switch (cmd.toLowerCase()) {
                case "help", "ajuda", "a", "h" -> {
                    System.out.println("Comandos disponíveis:");
                    System.out.println(" - [link] - Busca o produto no link");
                    System.out.println(" - help, ajuda, a, h - Mostra esta mensagem");
                    System.out.println(" - quit, exit, sair, q, e, x - Sai do programa");
                }
                case "quit", "exit", "sair", "q", "e", "x" -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
                // Caso o comando não seja nenhum dos comandos acima, vamos verificar se é um link
                default -> {
                    // Vamos verificar se o link é válido
                    if (!Helpers.isValidUrl(cmd)) {
                        System.out.println("O link digitado não é válido. Digite um link válido.");
                        System.out.println();
                        continue;
                    }

                    // Vamos verificar se o link é da Netshoes
                    if (!Helpers.isNetshoesUrl(cmd)) {
                        System.out.println("O link digitado não é da Netshoes. Digite um link da Netshoes.");
                        System.out.println();
                        continue;
                    }

                    // Vamos buscar o produto
                    var product = ProductFetcher.fetch(cmd);

                    try {
                        // Vamos fazer o parse do produto e imprimir os dados
                        Product parse = product.parse();
                        printProduct(parse);
                    } catch (AppException | IOException | InterruptedException e) {
                        System.out.println("Houve um erro ao buscar o produto: " + e.getMessage());
                    }


                    System.out.println();
                    System.out.println("Para buscar outro produto, digite o link do produto que deseja buscar:");
                }
            }
        }
    }

    private static void printProduct(Product product) {
        System.out.println("Produto encontrado!");
        System.out.println();
        System.out.println("Título: " + product.title());
        System.out.println("Preço: " + product.price());
        System.out.println("Imagens: \n  " + String.join(", ", product.image()));
        System.out.println("Descrição: \n  " + product.description());
        System.out.println();
    }
}
