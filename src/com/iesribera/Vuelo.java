package com.iesribera;

import java.util.ArrayList;
import java.util.List;

public class Vuelo {

	private String codigoVuelo;
	private String horaSalida;
	private String destino;
	private String procedencia;
	private int plazasFumador;
	private int plazasNoFumador;
	private int plazasTurista;
	private int plazasPrimera;
	private List<Pasajero> pasajeros = new ArrayList<>();

	public String getCodigoVuelo() {
		return codigoVuelo;
	}

	public void setCodigoVuelo(String codigoVuelo) {
		this.codigoVuelo = codigoVuelo;
	}

	public String getHoraSalida() {
		return horaSalida;

	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public int getPlazasFumador() {
		return plazasFumador;
	}

	public void setPlazasFumador(int plazasFumador) {
		this.plazasFumador = plazasFumador;
	}

	public int getPlazasNoFumador() {
		return plazasNoFumador;
	}

	public void setPlazasNoFumador(int plazasNoFumador) {
		this.plazasNoFumador = plazasNoFumador;
	}

	public int getPlazasTurista() {
		return plazasTurista;
	}

	public void setPlazasTurista(int plazasTurista) {
		this.plazasTurista = plazasTurista;
	}

	public int getPlazasPrimera() {
		return plazasPrimera;
	}

	public void setPlazasPrimera(int plazasPrimera) {
		this.plazasPrimera = plazasPrimera;
	}

	public List<Pasajero> getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(List<Pasajero> pasajeros) {
		this.pasajeros = pasajeros;
	}

	@Override public String toString() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Codigo vuelo: ").append(codigoVuelo);
		stringBuilder.append(" Hora salida: ").append(horaSalida);
		stringBuilder.append(" Destino: ").append(destino);
		stringBuilder.append(" Procedencia: ").append(procedencia);
		stringBuilder.append(" Plazas fumador: ").append(plazasFumador);
		stringBuilder.append(" Plazas no fumador: ").append(plazasNoFumador);
		stringBuilder.append(" Plazas turista: ").append(plazasTurista);
		stringBuilder.append(" Plazas primera: ").append(plazasPrimera);
		for (Pasajero pasajero : pasajeros) {
			stringBuilder.append("\n Pasajeros: ").append(pasajero);
		}

		return stringBuilder.toString();
	}
}
