package com.lucasmellof.teste.exceptions;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public class InvalidProductRedirectException extends AppException {
    public static final String MESSAGE = "Houve um erro ao encontrar o link de redirecionamento.";

    public InvalidProductRedirectException() {
        super(MESSAGE);
    }

    public InvalidProductRedirectException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
