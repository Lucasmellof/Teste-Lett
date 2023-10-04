package com.lucasmellof.teste.exceptions;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class PriceNotFoundException extends AppException {
    public static final String MESSAGE = "Houve um erro ao encontrar o preço do produto.";

    public PriceNotFoundException() {
        super(MESSAGE);
    }

    public PriceNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
