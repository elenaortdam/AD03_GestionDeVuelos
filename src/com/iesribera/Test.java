package com.iesribera;

public class Test {

	private String crearCodigoVuelo() {
		//todo: elena comprobar si existe
		String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

		return String.valueOf(letras.charAt(random(letras.length()))) +
				letras.charAt(random(letras.length())) +
				"-" + letras.charAt(random(letras.length())) +
				letras.charAt(random(letras.length())) +
				"-" + numeros[random(numeros.length)] +
				numeros[random(numeros.length)];
	}

	private int random(int maximo) {
		return (int) (Math.random() * maximo);
	}

	public static void main(String[] args) {
		Test main = new Test();
		System.out.println(main.crearCodigoVuelo());
	}
}
