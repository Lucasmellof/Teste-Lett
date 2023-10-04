package com.lucasmellof.teste.exceptions;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class ProductNotFoundException extends AppException {
    public static final String MESSAGE = "Produto não encontrado. Verifique se o link está correto.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    public ProductNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
