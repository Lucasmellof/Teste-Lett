package com.lucasmellof.teste.exceptions;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class ProductInfoNotFound extends AppException {
    public static final String MESSAGE = "Houve um erro ao encontrar informações do produto. Informação: ";

    public ProductInfoNotFound(String message) {
        super(MESSAGE + message);
    }

    public ProductInfoNotFound(String message, Throwable cause) {
        super(MESSAGE + message, cause);
    }
}
