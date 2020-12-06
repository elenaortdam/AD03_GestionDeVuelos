package com.iesribera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public void menu() throws IOException {
		BaseDatos baseDatos = BaseDatos.getInstance();

		BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));
		int opcionSeleccionada = 0;
		String entradaUsuario = entradaDatos.readLine().trim();
		//TODO: elena validar entradas
		//TODO: elena controlar que el codigo de los vuelos tengan el formato adecuado
		opcionSeleccionada = Integer.parseInt(entradaUsuario);

		switch (opcionSeleccionada) {
			case 1:
				List<Vuelo> vuelos = mostrarInformacionVuelos();
				for (Vuelo vuelo : vuelos) {
					String informacionVuelo =
							String.format("Fecha: %s Codigo: %s Procedencia: %s Destino: %s",
										  vuelo.getHoraSalida(),
										  vuelo.getCodigoVuelo(),
										  vuelo.getProcedencia().replaceAll("\n", ""),
										  vuelo.getDestino().replaceAll("\n", ""));
					System.out.println(informacionVuelo);
				}
				break;
			case 2:
				List<Pasajero> pasajeros = mostrarInformaciónPasajeros();
				for(Pasajero pasajero: pasajeros){
					System.out.println(pasajero);
				}
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			default:

		}
	}

	private List<Vuelo> mostrarInformacionVuelos() {

		List<Vuelo> vuelos = new ArrayList<>();
		try {
			BaseDatos instance = BaseDatos.getInstance();
			ResultSet resultSet = instance.consultaBD("select * from vuelos v");
			while (resultSet.next()) {
				Vuelo vuelo = new Vuelo();
				vuelo.setCodigoVuelo(resultSet.getString(Constantes.Vuelo.CODIGO_VUELO));
				vuelo.setDestino(resultSet.getString(Constantes.Vuelo.DESTINO));
				vuelo.setHoraSalida(resultSet.getString(Constantes.Vuelo.HORA_SALIDA));
				vuelo.setProcedencia(resultSet.getString(Constantes.Vuelo.PROCEDENCIA));
				vuelos.add(vuelo);
			}
			instance.cerrarConsulta();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vuelos;
	}

	private List<Pasajero> mostrarInformaciónPasajeros() {

		List<Pasajero> pasajeros = new ArrayList<>();
		try {
			BaseDatos instance = BaseDatos.getInstance();
			ResultSet resultSet = instance.consultaBD("select * from pasajeros");
			while (resultSet.next()) {
				Pasajero pasajero = new Pasajero();
				pasajero.setNumero(resultSet.getInt(Constantes.Pasajero.NUMERO));
				pasajero.setCodigoVuelo(resultSet.getString(Constantes.Vuelo.CODIGO_VUELO));
				String tipoPlaza = resultSet.getString(Constantes.Pasajero.TIPO_PLAZA);
				pasajero.setTipoPlaza(tipoPlaza);
				String esFumador = resultSet.getString(Constantes.Pasajero.FUMADOR);
				pasajero.setEsFumador(esFumador.trim().equals("SI"));
				pasajeros.add(pasajero);
			}
			instance.cerrarConsulta();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pasajeros;
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Escoja una opción: \n" +
								   "1. Mostrar información general \n" +
								   "2. Mostrar información de los pasajeros \n" +
								   "3. Ver los pasajeros de un vuelo \n" +
								   "4. Insertar un nuevo vuelo\n" +
								   "5. Borrar vuelo\n" +
								   "6. Convertir vuelo de fumadores a no fumadores");
		Main main = new Main();
		main.menu();

	}
}
