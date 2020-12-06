package com.iesribera;

public final class Constantes {

	private Constantes() {
	}

	public static final String TURISTA = "TU";
	public static final String PRIMERA = "PR";
	public static final int MAXIMO_PLAZAS_VUELO = 400;

	public static final class Regex {
		public static final String CODIGO_VUELO = "\\w{2}-\\w+-\\d+";
	}

	public static final class Vuelo {

		private Vuelo() {
		}

		public static final String TABLA = "VUELOS";
		public static final String CODIGO_VUELO = "COD_VUELO";
		public static final String HORA_SALIDA = "HORA_SALIDA";
		public static final String DESTINO = "DESTINO";
		public static final String PROCEDENCIA = "PROCEDENCIA";
		public static final String PLAZAS_FUMADOR = "PLAZAS_FUMADOR";
		public static final String PLAZAS_NO_FUMADOR = "PLAZAS_NO_FUMADOR";

	}

	public static final class Pasajero {

		private Pasajero() {
		}

		public static final String TABLA = "PASAJEROS";
		public static final String NUMERO = "NUM";
		public static final String TIPO_PLAZA = "TIPO_PLAZA";
		public static final String FUMADOR = "FUMADOR";
	}

}
