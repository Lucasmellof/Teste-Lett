package com.lucasmellof.teste;

import com.lucasmellof.teste.product.ProductFetcher;

import java.io.IOException;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class Main {
    public static void main(String[] args) {
        try {
            ProductFetcher fetcher = ProductFetcher.fetch("https://www.netshoes.com.br/2IC-5003-008");

            System.out.println(fetcher.parse());
        } catch (IOException | InterruptedException e) {
            System.out.println("Couldn't fetch data");
        }
    }
}
