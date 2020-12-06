package com.iesribera;

public class Pasajero {

	private int numero;
	private String codigoVuelo;
	private String tipoPlaza;
	private boolean esFumador;

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getCodigoVuelo() {
		return codigoVuelo;
	}

	public void setCodigoVuelo(String codigoVuelo) {
		this.codigoVuelo = codigoVuelo;
	}

	public String getTipoPlaza() {
		if (tipoPlaza.trim().equals(Constantes.TURISTA)) {
			tipoPlaza = "TURISTA";
		} else if (tipoPlaza.trim().equals(Constantes.PRIMERA)) {
			tipoPlaza = "PRIMERA";
		}
		return tipoPlaza;
	}

	public void setTipoPlaza(String tipoPlaza) {
		this.tipoPlaza = tipoPlaza;
	}

	public boolean isEsFumador() {
		return esFumador;
	}

	public void setEsFumador(boolean esFumador) {
		this.esFumador = esFumador;
	}

	@Override public String toString() {
		return
				"Número: " + numero +
						" Vuelo: " + codigoVuelo + " Plaza :" + getTipoPlaza() +
						" Fumador: " + esFumador;
	}
}

