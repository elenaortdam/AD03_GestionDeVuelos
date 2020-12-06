package com.iesribera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.iesribera.Constantes.Pasajero.FUMADOR;
import static com.iesribera.Constantes.Vuelo.*;

public class Aerolinea {
	private static final String textoMenu =
			"----------------------------------------------------\n" +
					"Escoja una opción: \n" +
					"0. Salir del programa\n" +
					"1. Mostrar información general \n" +
					"2. Mostrar información de los pasajeros \n" +
					"3. Ver los pasajeros de un vuelo \n" +
					"4. Insertar nuevo vuelo\n" +
					"5. Borrar vuelo introducido previamente\n" +
					"6. Convertir vuelos de fumadores a no fumadores \n" +
					"----------------------------------------------------";

	private final List<String> codigoVuelosCreados = new ArrayList<>();

	public void menu() {

		System.out.println(textoMenu);

		BufferedReader entradaDatos = new BufferedReader(new InputStreamReader(System.in));
		int opcionSeleccionada = 0;
		String entradaUsuario = "";
		do {

			try {
				do {
					entradaUsuario = entradaDatos.readLine();
					if (entradaUsuario.isEmpty()) {
						System.out.println("Debe introducir una opción del menú");
					} else {
						entradaUsuario = entradaUsuario.trim();
					}
				} while (entradaUsuario.isEmpty());

				opcionSeleccionada = Integer.parseInt(entradaUsuario);
				opcionesMenu(entradaDatos, opcionSeleccionada);
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		} while (opcionSeleccionada != 0);
	}

	private void opcionesMenu(BufferedReader entradaDatos, int opcionSeleccionada) throws IOException {
		List<Pasajero> pasajeros;
		String entradaUsuario;
		switch (opcionSeleccionada) {
			case 1:
				System.out.println("Se está consultando los vuelos...");
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
				System.out.println(textoMenu);
				break;
			case 2:
				pasajeros = mostrarInformacionPasajeros();
				for (Pasajero pasajero : pasajeros) {
					System.out.println(pasajero);
				}
				System.out.println(textoMenu);
				break;
			case 3:
				System.out.println("¿De qué vuelo quieres ver los pasajeros?: ");
				entradaUsuario = entradaDatos.readLine().trim();
				pasajeros = mostrarPasajerosVuelo(entradaUsuario);
				for (Pasajero pasajero : pasajeros) {
					System.out.println(pasajero);
				}
				System.out.println(textoMenu);
				break;
			case 4:
				Vuelo vuelo = crearVuelo(entradaDatos);
				this.codigoVuelosCreados.add(vuelo.getCodigoVuelo());
				insertarVuelo(vuelo);
				System.out.printf("El vuelo %s se ha creado correctamente%n", vuelo.getCodigoVuelo());
				System.out.printf("Datos del vuelo: %s%n", vuelo.toString());
				System.out.println(textoMenu);
				break;
			case 5:
				if (this.codigoVuelosCreados.isEmpty()) {
					System.out.println("No existen ningún vuelo que se pueda borrar, cree uno antes");
				} else {
					borrarVuelo(entradaDatos, this.codigoVuelosCreados);
				}
				System.out.println(textoMenu);
				break;
			case 6:
				System.out.println("Se convertirán todos los vuelos de fumadores a no fumadores." +
										   "¿Está seguro de que quiere continuar? (S/N)");
				String continuar;
				do {
					System.out.println("Introduce S para continuar o N para cancelar");
					continuar = entradaDatos.readLine().trim().toUpperCase();
				} while (continuar.isEmpty());
				if (continuar.equals("S")) {
					try {
						actualizarVuelos();
						System.out.println("Se han actualizado los vuelos correctamente");
					} catch (SQLException throwables) {
						System.out.println("Ha ocurrido un error al cambiar los vuelos a no fumador," +
												   "inténtelo de nuevo más tarde");
					}
				}
				System.out.println(textoMenu);
				break;
			default:
				System.out.println("Ups...esa opción no es válida introduce una del 0 al 6");
		}
	}

	private void borrarVuelo(BufferedReader entradaDatos, List<String> vuelosCreados) throws IOException {
		String codigoIntroducido = "";
		do {
			System.out.println("Introduzca el codigo del vuelo que desea borrar");
			codigoIntroducido = comprobarFormato(entradaDatos.readLine().trim().toUpperCase(),
												 Constantes.Regex.CODIGO_VUELO);
		} while (codigoIntroducido.isEmpty());

		String borrarVuelo = "";
		for (String codigoVuelo : vuelosCreados) {
			if (codigoIntroducido.equals(codigoVuelo.trim())) {
				borrarVuelo = codigoVuelo;
			}
		}
		if (borrarVuelo.isEmpty()) {
			System.out.println("No se ha creado ningún vuelo con ese código");
		} else {
			try {
				borrarVuelo(borrarVuelo);
			} catch (SQLException throwables) {
				System.out.printf("Ha ocurrido un error al borrar el vuelo %s. Error: %s\n",
								  borrarVuelo, throwables.getLocalizedMessage());
			}
			System.out.printf("El vuelo: %s se ha borrado correctamente", borrarVuelo);
		}
	}

	private Vuelo crearVuelo(BufferedReader entradaDatos) throws IOException {
		String codigoVuelo = crearCodigoVuelo();
		System.out.println("Introduzca los datos del vuelo ");
		LocalDate fechaVuelo = comprobarFecha(entradaDatos);
		String hora = comprobarHora(entradaDatos);
		String fechaHoraVuelo = String.format("%d/%d/%s-%s", fechaVuelo.getDayOfMonth(),
											  fechaVuelo.getMonthValue(),
											  String.valueOf(fechaVuelo.getYear()).substring(2),
											  hora);
		String procedenciaIntroducida = "";
		String destinoIntroducido = "";
		do {
			procedenciaIntroducida = comprobarPaises(entradaDatos,
													 "procedencia");
			destinoIntroducido = comprobarPaises(entradaDatos,
												 "destino");
			if (procedenciaIntroducida.equals(destinoIntroducido)) {
				System.out.println("El destino y la procedencia no pueden ser iguales, introdúzcalas de nuevo");
			}
		} while (procedenciaIntroducida.equals(destinoIntroducido));

		Integer plazasTotales = Constantes.MAXIMO_PLAZAS_VUELO;
		Integer plazasFumador = comprobarPlazasVuelo(entradaDatos, "fumador");
		Integer plazasNoFumador = plazasTotales - plazasFumador;

		Integer plazasTurista = comprobarPlazasVuelo(entradaDatos, "turista");
		Integer plazasPrimera = plazasTotales - plazasTurista;

		Vuelo vuelo = new Vuelo();
		vuelo.setCodigoVuelo(codigoVuelo);
		vuelo.setHoraSalida(fechaHoraVuelo);
		vuelo.setProcedencia(procedenciaIntroducida);
		vuelo.setDestino(destinoIntroducido);
		vuelo.setPlazasFumador(plazasFumador);
		vuelo.setPlazasNoFumador(plazasNoFumador);
		vuelo.setPlazasTurista(plazasTurista);
		vuelo.setPlazasPrimera(plazasPrimera);
		return vuelo;
	}

	private Integer comprobarPlazasVuelo(BufferedReader entradaDatos, String nombreCampo) throws IOException {
		String fumador;
		Integer plazasFumador = null;
		do {
			System.out.printf("¿Cuántas plazas de %s tiene el vuelo? \n", nombreCampo);
			fumador = entradaDatos.readLine().trim();
			if (fumador.isEmpty()) {
				System.out.println("Este campo es requerido. Introdúzcalo de nuevo");
			} else {
				int plazas = Integer.parseInt(fumador);
				if (plazas > Constantes.MAXIMO_PLAZAS_VUELO) {
					System.out.printf("El número de plazas no puede ser mayor al máximo del vuelo: %s \n",
									  Constantes.MAXIMO_PLAZAS_VUELO);
				} else {
					plazasFumador = plazas;
				}
			}
		} while (plazasFumador == null);
		return plazasFumador;
	}

	private String comprobarPaises(BufferedReader entradaDatos, String nombreCampo) throws IOException {
		String pais = "";
		do {
			System.out.printf("Introduzca el país de %s del vuelo: \n", nombreCampo);
			String paisIntroducido;
			paisIntroducido = entradaDatos.readLine().trim().toUpperCase();
			if (paisIntroducido.isEmpty()) {
				System.out.println("Este campo no puede estar vacío");
			} else {
				pais = comprobarFormato(paisIntroducido, "\\w{1,}");
				if (pais.isEmpty()) {
					System.out.println("Este campo no puede estar vacío");
				}
			}
		} while (pais.isEmpty());
		return pais;
	}

	private String comprobarHora(BufferedReader entradaDatos) throws IOException {
		String hora;
		String entradaUsuario;
		boolean cumpleFormato = true;
		do {
			do {
				System.out.println("Introduzca la hora del vuelo (formato 24 horas -> HH:mm): ");
				entradaUsuario = entradaDatos.readLine().trim();
				hora = comprobarFormato(entradaUsuario, "\\d{2}:\\d{2}");
				if (hora.isEmpty()) {
					System.out.println("La hora no tiene el formato correcto, introduzcala de nuevo");
				}
			} while (hora.isEmpty());
			String[] horaSeparada = hora.split(":");
			int horaIntroducida = Integer.parseInt(horaSeparada[0].trim());
			int minutosIntroducidos = Integer.parseInt(horaSeparada[1].trim());

			if (horaIntroducida < 0 || horaIntroducida > 24) {
				System.out.println("La hora introducida no es correcta. Introduzca la hora de nuevo:");
				cumpleFormato = false;
			}
			if (minutosIntroducidos < 0 || minutosIntroducidos > 59) {
				System.out.println("Los minutos introducidos no son correctos. Introduzca la hora de nuevo");
				cumpleFormato = false;
			}
		} while (!cumpleFormato);
		return hora;
	}

	private LocalDate comprobarFecha(BufferedReader entradaDatos) throws IOException {
		String entradaUsuario;
		LocalDate fechaIntroducida;
		do {
			int dia;
			do {
				System.out.println("Introduzca el día: ");
				entradaUsuario = entradaDatos.readLine().trim();
				dia = Integer.parseInt(entradaUsuario);
				if (dia < 1 || dia > 31) {
					System.out.println("El día debe estar entre 1 y 31. Introduzca el día de nuevo:");
				}
			} while (dia < 1 || dia > 31);
			int mes = 0;
			do {
				System.out.println("Introduzca el mes: ");
				entradaUsuario = entradaDatos.readLine().trim();
				if (entradaUsuario.isEmpty()) {
					System.out.println("El mes no puede estar vacío, introduzca un mes válido");
				} else {
					mes = Integer.parseInt(entradaUsuario);
					if (mes < 1 || mes > 12) {
						System.out.println("El mes debe estar entre 1 y 12. Introduzca el mes de nuevo:");
					}
				}

			} while (mes < 1 || mes > 12);
			int anio;
			do {
				System.out.println("Introduzca el año (formato 20XX): ");
				entradaUsuario = entradaDatos.readLine().trim();
				anio = Integer.parseInt(entradaUsuario);
				if (anio < LocalDate.now().getYear()) {
					System.out.println("El año introducido no es correcto.");
				}
			} while (anio < LocalDate.now().getYear());
			fechaIntroducida = LocalDate.of(anio, mes, dia);
			if (fechaIntroducida.isBefore(LocalDate.now())) {
				System.out.println("La fecha introducida no puede ser anterior a la actual. Introduzca los datos de nuevo.");
			}

		} while (fechaIntroducida.isBefore(LocalDate.now()));
		return fechaIntroducida;
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

	private List<Pasajero> mostrarInformacionPasajeros() {
		List<Pasajero> pasajeros = new ArrayList<>();

		try {
			BaseDatos instance = BaseDatos.getInstance();
			ResultSet resultSet = instance.consultaBD("select * from pasajeros");
			pasajeros = extraerDatosPasajeros(resultSet);
			instance.cerrarConsulta();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pasajeros;
	}

	private void comprobarEntrada(String entrada, String nombreCampo) {
		if (entrada == null || entrada.isEmpty()) {
			throw new IllegalArgumentException(String.format("El %s es un parámetro necesario",
															 nombreCampo));
		}

	}

	private List<Pasajero> mostrarPasajerosVuelo(String codigoVuelo) {
		comprobarEntrada(codigoVuelo, "Código de vuelo");
		codigoVuelo = codigoVuelo.trim().toUpperCase();
		String codigoVueloCorrecto = comprobarFormato(codigoVuelo, "\\w{2}-\\w+-\\d+");
		if (codigoVueloCorrecto.isEmpty()) {
			throw new IllegalArgumentException("El formato del código no es correcto." +
													   " Formato correcto: XX-XX-1234");
		}
		List<Pasajero> pasajeros = new ArrayList<>();
		try {
			BaseDatos instance = BaseDatos.getInstance();
			ResultSet resultSet =
					instance.consultaBD(String.format("select * from pasajeros p where p.%s in ('%s')",
													  Constantes.Vuelo.CODIGO_VUELO, codigoVueloCorrecto));
			pasajeros = extraerDatosPasajeros(resultSet);
			instance.cerrarConsulta();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pasajeros;
	}

	private String comprobarFormato(String entradaDatos, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(entradaDatos);
		String datosCorrectos = "";
		while (matcher.find()) {
			datosCorrectos = matcher.group();
		}
		return datosCorrectos;
	}

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

	private void borrarVuelo(String codigoVuelo) throws SQLException {

		Vuelo vuelo = new Vuelo();

		BaseDatos instance = BaseDatos.getInstance();
		instance.consultaBD(String.format("delete from %s where %s = '%s'",
										  TABLA, Constantes.Vuelo.CODIGO_VUELO,
										  codigoVuelo));
		instance.cerrarConsulta();

	}

	private List<Pasajero> extraerDatosPasajeros(ResultSet resultSet) throws SQLException {
		List<Pasajero> pasajeros = new ArrayList<>();
		while (resultSet.next()) {
			Pasajero pasajero = new Pasajero();
			pasajero.setNumero(resultSet.getInt(Constantes.Pasajero.NUMERO));
			pasajero.setCodigoVuelo(resultSet.getString(Constantes.Vuelo.CODIGO_VUELO));
			String tipoPlaza = resultSet.getString(Constantes.Pasajero.TIPO_PLAZA);
			pasajero.setTipoPlaza(tipoPlaza);
			String esFumador = resultSet.getString(FUMADOR);
			pasajero.setEsFumador(esFumador.trim().equals("SI"));
			pasajeros.add(pasajero);
		}
		return pasajeros;
	}

	public void insertarVuelo(Vuelo vuelo) {
		try {
			BaseDatos instance = BaseDatos.getInstance();

			instance.consultaBD(String.format("insert into %s values ('%s','%s'," +
													  "'%s','%s','%s','%s','%s','%s')",
											  TABLA,
											  vuelo.getCodigoVuelo(), vuelo.getHoraSalida(),
											  vuelo.getDestino(), vuelo.getProcedencia(),
											  vuelo.getPlazasFumador(), vuelo.getPlazasNoFumador(),
											  vuelo.getPlazasTurista(), vuelo.getPlazasTurista()));
			instance.cerrarConsulta();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Ha ocurrido un error al introducir el vuelo");
		}
	}

	public void actualizarVuelos() throws SQLException {

		BaseDatos instance = BaseDatos.getInstance();
		//Sumamos las plazas de fumadores y no fumadores y ponemos a 0 las plazas para fumadores
		ResultSet resultSet =
				instance.consultaBD("UPDATE " + TABLA + " SET "
											+ PLAZAS_NO_FUMADOR + " = " + PLAZAS_NO_FUMADOR + " + " + PLAZAS_FUMADOR + ", "
											+ PLAZAS_FUMADOR + " = 0 " +
											"WHERE " + PLAZAS_FUMADOR + " > 0");

		instance.consultaBD("UPDATE " + Constantes.Pasajero.TABLA + " SET " +
									FUMADOR + " = NO " +
									"where " + FUMADOR + " = SI");
		instance.cerrarConsulta();

	}

	public static void main(String[] args) throws IOException {

		Aerolinea main = new Aerolinea();
		main.menu();

	}
}
