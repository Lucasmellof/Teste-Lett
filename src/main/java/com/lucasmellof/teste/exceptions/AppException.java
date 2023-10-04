package com.lucasmellof.teste.exceptions;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public abstract class AppException extends RuntimeException {

	public AppException(){
		super("Ocorreu um erro desconhecido aconteceu.");
	}

	public AppException(Throwable cause) {
		super("Ocorreu um erro desconhecido aconteceu.", cause);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
}
