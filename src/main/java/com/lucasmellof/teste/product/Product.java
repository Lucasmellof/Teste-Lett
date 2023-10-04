package com.lucasmellof.teste.product;

import java.util.Arrays;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 03/10/2023
 */
public record Product(String title, String description, String[] image, String price) {
	@Override
	public String toString() {
		return "Product{" +
				       "title='" + title + '\'' +
				       ", description='" + description + '\'' +
				       ", image=" + Arrays.toString(image) +
				       ", price=" + price +
				       '}';
	}
}
