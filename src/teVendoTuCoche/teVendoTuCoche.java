package teVendoTuCoche;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;

import clasesBaseDeDatos.Cliente;
import clasesBaseDeDatos.Coches;
import clasesBaseDeDatos.HibernateUtil;
import clasesBaseDeDatos.Venta;

/**
 * @author Jorge Alonso
 * @version 1.0
 * Practica teVendoTuCoche
 * En caso de que no conecte cambiar la contraseña en el archivo de configuración
 */
public class teVendoTuCoche {

	
	/*
	 * Inicio del main
	 */
	public static void main(String[] args) {
		int mS;
		// Lo primero será obtener la sesión actual
		SessionFactory session = HibernateUtil.getSessionFactory();
		// crear la sesión
		Session sesion = session.openSession();
		// crear la transacción de la sesión
		Transaction tx = sesion.beginTransaction();
		do {
			menu();
			mS = menuSeleccionado(session,sesion,tx);
		}while(mS!= 5);
		
		session.close();
		sesion.close();
	}
	
	/*
	 * Métodos para mostrar todos los menús
	 */
	public static void menu() {
		System.out.println("***APLICACION TEVENDOTUCOCHE****");
		System.out.println("********MENU PRINCIPAL********");
		System.out.println("1. Altas: ");
		System.out.println("2. Bajas: ");
		System.out.println("3. Modificaciones: ");
		System.out.println("4. Informes: ");
		System.out.println("5. Salir del programa: ");
	}
	
	public static void menuAltas() {
		System.out.println("***APLICACION TEVENDOTUCOCHE****");
		System.out.println("********MENU ALTAS********");
		System.out.println("1. Alta de venta: ");
		System.out.println("2. Alta de cliente: ");
		System.out.println("3. Alta de vehiculo: ");
		System.out.println("4. Volver al menú anterior: ");
	}
	
	public static void menuBajas() {
		System.out.println("***APLICACION TEVENDOTUCOCHE****");
		System.out.println("********MENU BAJAS********");
		System.out.println("1. Baja de venta: ");
		System.out.println("2. Baja de cliente: ");
		System.out.println("3. Baja de vehiculo: ");
		System.out.println("4. Volver al menú anterior: ");
	}
	
	public static void menuModificaciones() {
		System.out.println("***APLICACION TEVENDOTUCOCHE****");
		System.out.println("******MENU MODIFICACIONES******");
		System.out.println("1. Modificación de venta: ");
		System.out.println("2. Modificación de cliente: ");
		System.out.println("3. Modificación de vehiculo: ");
		System.out.println("4. Volver al menú anterior: ");
	}
	
	public static void menuInformes() {
		System.out.println("***APLICACION TEVENDOTUCOCHE****");
		System.out.println("********MENU INFORMES********");
		System.out.println("1. Informes de los clientes por ciudad: ");
		System.out.println("2. Seleccionar los datos vehículos por número de plazas: ");
		System.out.println("3. Seleccionar los datos de venta por rango de fechas: ");
		System.out.println("4. Consulta generica: ");
		System.out.println("5. Volver al menú anterior: ");
	}
	
	/*
	 * Funcionalidad principal de los menús y llamadas a otros metodos
	 */
	public static int menuSeleccionado(SessionFactory session, Session sesion, Transaction tx) {
		int menu;
		int menuF = 5;
		int menu2;
		Scanner sc = new Scanner(System.in);
		Scanner sc2 = new Scanner(System.in);
		try {
			menu = sc.nextInt();
			
			switch(menu) {
			case 1:
				menuAltas();
				menu2 = sc2.nextInt();
				switch(menu2) {
				case 1:
					altaVenta(sesion,tx);
					break;
				case 2:
					altaCliente(sesion,tx);
					break;
				case 3:
					altaCoche(sesion,tx);
					break;
				case 4:
					break;
				}
				break;
			case 2:
				menuBajas();
				menu2 = sc2.nextInt();
				switch(menu2) {
				case 1:
					bajaGenerico(sesion,tx,"Venta","id_Venta");
					break;
				case 2:
					bajaGenerico(sesion,tx,"Cliente","id_Cliente");
					break;
				case 3:
					bajaGenerico(sesion,tx,"Coches","id_Coche");
					break;
				case 4:
					menuF = 4;
					break;
				}
				break;
			case 3:
				menuModificaciones();
				menu2 = sc2.nextInt();
				switch(menu2) {
				case 1:
					modificarVentas(sesion,tx);
					break;
				case 2:
					modificarCliente(sesion,tx);
					break;
				case 3:
					modificarCoche(sesion,tx);
					break;
				case 4:
					menuF = 4;
					break;
				}
				break;
			case 4:
				menuInformes();
				menu2 = sc2.nextInt();
				switch(menu2) {
				case 1:
					mostrarDatosClientesPorCiudad(sesion);
					break;
				case 2:
					mostrarDatosVehiculoPorPlazas(sesion);
					break;
				case 3:
					seleccionarDatosPorRangoDeFechas(sesion,tx);
					break;
				case 4:
					consultaGenerica(sesion,tx);
					break;
				case 5:
					menuF = 4;
					break;
				}
				break;
			case 5:
				System.out.println("Saliendo del programa...");
				menuF = 5;
				break;
			default:
				System.out.println("Opcion incorrecta");
			}
		} catch(InputMismatchException e) {
			System.out.println("Error al seleccionar una opción");
		}
		
		
		return menuF;
	}


	/*
	 * Método para dar de alta una venta
	 * Se piden todos los datos al usuario
	 * Si todos los datos son correctos se ejecuta 
	 */
	public static void altaVenta(Session sesion,Transaction tx) {
		boolean venta = false;
		while(!venta) {
			try {
				System.out.println("Dar de alta una venta");
				Scanner sc = new Scanner(System.in);
				Venta vn = new Venta();
				int id = devolverIDMax("Venta","id_Venta",sesion);
				vn.setIdVenta(id+1);
				System.out.println("Inserta el id  del cliente: (Required)");
				int idC = sc.nextInt();
				Cliente cli = new Cliente();
				cli = (Cliente) sesion.load(Cliente.class, idC);
				vn.setCliente(cli);
				System.out.println("Inserta el id  del coche: (Required)");
				int idCo = sc.nextInt();
				Coches coche = new Coches();
				coche = (Coches) sesion.load(Coches.class, idCo);
				vn.setCoches(coche);
				System.out.println("La fecha de compra será la de hoy");
				vn.setFechaCompra(new java.sql.Date(new Date().getTime()));
				System.out.println("La fecha de entrega será 5 dias mas tarde ");
				long cincoDiasEnMili = 5L * 24 * 60 * 60 * 1000; 
				Date fecha = new java.sql.Date(new Date().getTime());
				Date fechaFinal = new Date(fecha.getTime() + cincoDiasEnMili);
				vn.setFechaEntrega(fechaFinal);
				System.out.println("Introduce el precio de la venta: (Required)");
				int precio = sc.nextInt();
				vn.setPrecio(precio);
				sesion.save(vn);
				tx.commit();
				System.out.println("Venta guardada con exito");
				venta = true;
			}catch (InputMismatchException e) {
			    System.out.println("Por favor, ingresa un número válido.");
			} catch (NullPointerException e) {
			    System.out.println("El cliente o el coche no existen.");
			} catch (ConstraintViolationException e) {
			    System.out.println("Violación de restricciones en la base de datos: " + e.getMessage());
			} catch (TransactionException e) {
			    System.out.println("Error en la transacción: " + e.getMessage());
			} catch (NumberFormatException e) {
			    System.out.println("Formato de número inválido: " + e.getMessage());
			} catch (IllegalArgumentException e) {
			    System.out.println("Argumento inválido: " + e.getMessage());
			} catch (HibernateException e) {
			    System.out.println("Error al guardar la venta. Por favor, intenta nuevamente.");
			} catch (Exception e) {
			    System.out.println("Ocurrió un error inesperado: " + e.getMessage());
			}
		}
		
		
		
		
	}
	
	/*
	 * Método para dar de alta a un cliente
	 * Se piden todos los datos al usuario
	 * Si todos los datos son correctos se ejecuta 
	 */
	public static void altaCliente(Session sesion, Transaction tx) {
	    boolean clienteCreado = false; // Indicador para repetir en caso de error

	    while (!clienteCreado) { // Repetir mientras no se cree el cliente
	        try {
	            System.out.println("Dar de alta a un cliente");
	            Scanner sc = new Scanner(System.in);
	            Scanner sc2 = new Scanner(System.in);
	            Cliente cli = new Cliente();

	            int id = devolverIDMax("Cliente", "id_Cliente", sesion);
	            cli.setIdCliente((id + 1));

	            System.out.println("Inserta el DNI del cliente: (Required)");
	            String dni = sc.nextLine();
	            cli.setDni(dni);

	            System.out.println("Inserta el nombre del cliente: (Required)");
	            String nombre = sc.nextLine();
	            cli.setNombre(nombre);

	            System.out.println("Inserta la dirección del cliente: (Required)");
	            String direccion = sc.nextLine();
	            cli.setDireccion(direccion);

	            System.out.println("Inserta la ciudad del cliente: (Required)");
	            String ciudad = sc.nextLine();
	            cli.setCiudad(ciudad);

	            System.out.println("Inserta el teléfono del cliente: (Required)");
	            String telefono = sc.nextLine();
	            cli.setTelefono(telefono);

	            System.out.println("Inserta el email del cliente: (Required)");
	            String email = sc.nextLine();
	            cli.setEmail(email);

	            System.out.println("Inserta la edad del cliente:");
	            Byte edad = sc2.nextByte();
	            cli.setEdad(edad);

	            System.out.println("Inserta el sexo del cliente:");
	            String sexo = sc.nextLine();
	            cli.setSexo(sexo);

	            // Guardar el cliente
	            sesion.save(cli);
	            tx.commit();
	            System.out.println("Cliente creado correctamente");
	            clienteCreado = true; // Salir del bucle

	        } catch (InputMismatchException e) {
	            System.out.println("Error: El valor ingresado no es válido. Por favor, intenta nuevamente.");
	        } catch (ConstraintViolationException e) {
	            System.out.println("Error: Violación de restricciones en la base de datos: " + e.getMessage());
	        } catch (TransactionException e) {
	            System.out.println("Error: La transacción no pudo completarse. " + e.getMessage());
	        } catch (IllegalArgumentException e) {
	            System.out.println("Error: Argumento inválido. Por favor, revisa los datos ingresados.");
	        } catch (HibernateException e) {
	            System.out.println("Error: Ocurrió un problema con Hibernate. Intenta nuevamente.");
	        } catch (Exception e) {
	            System.out.println("Error inesperado: " + e.getMessage());
	        }
	    }
	}
	
	
	/*
	 * Método para dar de alta a un coche
	 * Se piden todos los datos al usuario
	 * Si todos los datos son correctos se ejecuta 
	 */
	public static void altaCoche(Session sesion, Transaction tx) {
	    boolean cocheCreado = false; // Indicador para repetir en caso de error

	    while (!cocheCreado) { // Repetir mientras no se cree el coche
	        try {
	            System.out.println("Dar de alta a un coche");
	            Scanner sc = new Scanner(System.in);
	            Scanner sc2 = new Scanner(System.in);
	            Coches coche = new Coches();

	            int id = devolverIDMax("Coches", "id_Coche", sesion);
	            coche.setIdCoche((id + 1));

	            System.out.println("Inserta la marca del coche: (Required)");
	            String marca = sc.nextLine();
	            coche.setMarca(marca);

	            System.out.println("Inserta la matrícula del coche: (Required)");
	            String matricula = sc.nextLine();
	            coche.setMatricula(matricula);

	            System.out.println("Inserta el modelo del coche: (Required)");
	            String modelo = sc.nextLine();
	            coche.setModelo(modelo);

	            System.out.println("Inserta el color del coche: (Required)");
	            String color = sc.nextLine();
	            coche.setColor(color);

	            System.out.println("Inserta el número de plazas: (Required)");
	            Byte plazas = sc2.nextByte();
	            coche.setPlazas(plazas);

	            System.out.println("Inserta los extras del coche:");
	            String extras = sc.nextLine();
	            coche.setExtras(extras);

	            // Guardar el coche
	            sesion.save(coche);
	            tx.commit();
	            System.out.println("Coche creado correctamente");
	            cocheCreado = true; // Salir del bucle

	        } catch (InputMismatchException e) {
	            System.out.println("Error: El valor ingresado no es válido. Por favor, intenta nuevamente.");
	        } catch (ConstraintViolationException e) {
	            System.out.println("Error: Violación de restricciones en la base de datos: " + e.getMessage());
	        } catch (TransactionException e) {
	            System.out.println("Error: La transacción no pudo completarse. " + e.getMessage());
	        } catch (IllegalArgumentException e) {
	            System.out.println("Error: Argumento inválido. Por favor, revisa los datos ingresados.");
	        } catch (HibernateException e) {
	            System.out.println("Error: Ocurrió un problema con Hibernate. Intenta nuevamente.");
	        } catch (Exception e) {
	            System.out.println("Error inesperado: " + e.getMessage());
	        }
	    }
	}
	
	/*
	 * Metodo para dar de baja de forma generica
	 * Al metodo se le pasa el nombre de la tabla y el nombre del id para identificar que dato quieres dar de baja
	 * Dependiendo del nombre de la tabla muestra unos datos u otros
	 * Pide confirmación para borrar el dato de la base de datos
	 */
	public static void bajaGenerico(Session sesion, Transaction tx, String nTabla, String nId) {
	    try {
	        System.out.println("Dar baja");
	        Scanner sc = new Scanner(System.in);
	        Scanner sc2 = new Scanner(System.in);

	        System.out.println("Escribe el id de la tabla " + nTabla + " que quieres dar de baja");
	        int id = sc.nextInt();

	        if (nTabla.equals("Venta")) {
	            mostrarVenta(sesion, id);
	        } else if (nTabla.equals("Cliente")) {
	            mostrarCliente(sesion, id);
	        } else {
	            mostrarCoche(sesion, id);
	        }

	        String sqlDel = "DELETE FROM " + nTabla + " WHERE " + nId + " = :idt";
	        System.out.println("¿Estás seguro que quieres eliminar el campo si/no?");
	        String res = sc2.nextLine();

	        if (res.equalsIgnoreCase("si")) {
	            Query q = sesion.createQuery(sqlDel);
	            q.setParameter("idt", id);
	            int filasModif = q.executeUpdate();
	            tx.commit();
	            System.out.println("Filas borradas: " + filasModif);
	        } else {
	            System.out.println("Operación cancelada");
	        }

	    } catch (InputMismatchException e) {
	        System.out.println("Error: Se esperaba un número como ID. Por favor, intenta nuevamente.");
	    } catch (ObjectNotFoundException e) {
	        System.out.println("Error: ID no encontrado en la tabla " + nTabla);
	    } catch (ConstraintViolationException e) {
	        System.out.println("Error: No se puede eliminar porque está relacionado con otros registros.");
	    } catch (SQLGrammarException e) {
	        System.out.println("Error: Hay un problema en la sintaxis SQL. Revisa el nombre de las tablas o columnas.");
	    } catch (TransactionException e) {
	        System.out.println("Error: La transacción no pudo completarse. " + e.getMessage());
	    } catch (HibernateException e) {
	        System.out.println("Error: Ocurrió un problema con Hibernate. Intenta nuevamente.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: Argumento inválido. Por favor, revisa los datos ingresados.");
	    }
	}
	
	
	/*
	 * Metodo para modificar una venta
	 * Para ello se pide el id de la venta a modificar
	 * Se crea un submenu para seleccionar que dato quieres modificar
	 * El id de venta no puede ser modificado
	 * En caso de cambiar el cliente o el coche se pedirá el id del mismo
	 * Se pedirá confirmación para ejecutar el cambio
	 */
	public static void modificarVentas(Session sesion, Transaction tx) {
	    try {
	        System.out.println("Modificar Ventas");
	        Scanner sc = new Scanner(System.in);
	        Scanner sc2 = new Scanner(System.in);
	        Boolean seguir = false;
	        System.out.print("Escribe el id de la venta que quieras modificar: ");
	        int id = sc.nextInt();
	       
	        Venta vt = new Venta();
	        vt = (Venta) sesion.load(Venta.class, id);
	        while (!seguir) {
	        	System.out.println("¿Que dato quieres modificar?");
	 	        mostrarVenta(sesion, id);
	 	        System.out.println("6.Salir y guardar");
	 	        System.out.println("7.Salir sin guardar");
	            System.out.println("Dato a modificar: ");
	            int dato = sc.nextInt();
	            switch (dato) {
	                case 1:
	                    System.out.println("Se cambia la fecha de venta a la actual");
	                    vt.setFechaCompra(new java.sql.Date(new Date().getTime()));
	                    break;
	                case 2:
	                    System.out.println("Fecha de entrega en 5 dias");
	                    long cincoDiasEnMili = 5L * 24 * 60 * 60 * 1000;
	                    Date fecha = new java.sql.Date(new Date().getTime());
	                    Date fechaFinal = new Date(fecha.getTime() + cincoDiasEnMili);
	                    vt.setFechaEntrega(fechaFinal);
	                    break;
	                case 3:
	                    System.out.println("Escribe el nuevo precio: ");
	                    int precio = sc.nextInt();
	                    vt.setPrecio(precio);
	                    System.out.println("Precio guardado, los cambios se verán al finalizar la modificación");
	                    break;
	                case 4:
	                    System.out.println("Escribe el ID del cliente");
	                    id = sc.nextInt();
	                    Cliente cli = new Cliente();
	                    cli = (Cliente) sesion.load(Cliente.class, id);
	                    vt.setCliente(cli);
	                    break;
	                case 5:
	                    System.out.println("Inserta el id del coche: ");
	                    int idCo = sc.nextInt();
	                    Coches coche = new Coches();
	                    coche = (Coches) sesion.load(Coches.class, idCo);
	                    vt.setCoches(coche);
	                    break;
	                case 6:
	                    System.out.println("¿Estas seguro que quieres salir y guardar? si/no");
	                    String res = sc2.nextLine();
	                    if (res.equalsIgnoreCase("si")) {
	                        sesion.save(vt);
	                        tx.commit();
	                        System.out.println("Venta guardada con exito");
	                        seguir = true;
	                    }System.out.println("Volviendo al menú");
	                    
	                    break;
	                case 7:
	                    System.out.println("¿Estas seguro que quieres salir sin guardar? si/no");
	                    String res2 = sc2.nextLine();
	                    if (res2.equalsIgnoreCase("si")) {
	                        System.out.println("Saliendo sin guardar");
	                        seguir = true;
	                    }System.out.println("Volviendo al menu");
	                    
	                    break;
	                default:
	                    System.out.println("Opción no válida. Intenta nuevamente.");
	                    break;
	            }
	        }
	    } catch (InputMismatchException e) {
	        System.out.println("Error: El valor ingresado no es válido. Por favor, intenta nuevamente.");
	    } catch (ObjectNotFoundException e) {
	        System.out.println("Error: ID no encontrado en la base de datos.");
	    } catch (ConstraintViolationException e) {
	        System.out.println("Error: Violación de restricciones en la base de datos.");
	    } catch (TransactionException e) {
	        System.out.println("Error: No se pudo completar la transacción.");
	    } catch (HibernateException e) {
	        System.out.println("Error: Problema con Hibernate. Intenta nuevamente.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: Se intentó asignar un valor inválido.");
	    } catch (Exception e) {
	        System.out.println("Error inesperado: " + e.getMessage());
	    }
	}
	
	
	/*
	 * Metodo para modificar un cliente
	 * Para ello se pide el id del cliente a modificar
	 * Se crea un submenu para seleccionar que dato quieres modificar
	 * El id de cliente no puede ser modificado
	 * Se pedirá confirmación para ejecutar el cambio
	 */
	public static void modificarCliente(Session sesion, Transaction tx) {
	    try {
	        System.out.println("Modificar Cliente");
	        Scanner sc = new Scanner(System.in);
	        Scanner sc2 = new Scanner(System.in);
	        Boolean seguir = false;
	        System.out.print("Escribe el id del cliente que quieras modificar: ");
	        int id = sc.nextInt();
	       
	        Cliente cli = new Cliente();
	        cli = (Cliente) sesion.get(Cliente.class, id);
	        while (!seguir) {
	        	System.out.println("¿Qué dato quieres modificar?");
	 	        mostrarCliente(sesion, id);
	 	        System.out.println("9. Salir y guardar");
	 	        System.out.println("10. Salir sin guardar");
	            System.out.println("Dato a modificar: ");
	            int dato = sc.nextInt();
	            switch (dato) {
	                case 1:
	                    System.out.println("Introduce el nuevo DNI del cliente");
	                    String dni = sc.nextLine();
	                    cli.setDni(dni);
	                    break;
	                case 2:
	                    System.out.println("Introduce el nombre del cliente:");
	                    sc.nextLine();
	                    String nombre = sc.nextLine();
	                    cli.setNombre(nombre);
	                    break;
	                case 3:
	                    System.out.println("Introduce la dirección del cliente");
	                    sc.nextLine();
	                    String direccion = sc.nextLine();
	                    cli.setDireccion(direccion);
	                    break;
	                case 4:
	                    System.out.println("Introduce la ciudad del cliente: ");
	                    sc.nextLine();
	                    String ciudad = sc.nextLine();
	                    cli.setCiudad(ciudad);
	                    break;
	                case 5:
	                    System.out.println("Introduce el teléfono del cliente");
	                    sc.nextLine();
	                    String numero = sc.nextLine();
	                    cli.setTelefono(numero);
	                    break;
	                case 6:
	                    System.out.println("Introduce el email del cliente");
	                    sc.nextLine();
	                    String email = sc.nextLine();
	                    cli.setEmail(email);
	                    break;
	                case 7:
	                    System.out.println("Introduce la edad del cliente");
	                    byte edad = sc2.nextByte();
	                    cli.setEdad(edad);
	                    break;
	                case 8:
	                    System.out.println("Introduce el sexo del cliente:");
	                    sc.nextLine();
	                    String sexo = sc.nextLine();
	                    cli.setSexo(sexo);
	                    break;
	                case 9:
	                    System.out.println("¿Estás seguro que quieres salir y guardar? si/no");
	                    sc.nextLine();
	                    String res = sc.nextLine();
	                    if (res.equalsIgnoreCase("si")) {
	                        sesion.save(cli);
	                        tx.commit();
	                        System.out.println("Cliente guardado con éxito");
	                        seguir = true;
	                    }
	                    System.out.println("Volviendo al menú");
	                    break;
	                case 10:
	                    System.out.println("¿Estás seguro que quieres salir sin guardar? si/no");
	                    sc.nextLine();
	                    String res2 = sc.nextLine();
	                    if (res2.equalsIgnoreCase("si")) {
	                        System.out.println("Saliendo sin guardar");
	                        seguir = true;
	                    }
	                    System.out.println("Volviendo al menú");
	                    break;
	                default:
	                    System.out.println("Opción no válida. Intenta nuevamente.");
	                    break;
	            }
	        }
	    } catch (InputMismatchException e) {
	        System.out.println("Error: Entrada no válida. Por favor, ingresa un valor correcto.");
	    } catch (ObjectNotFoundException e) {
	        System.out.println("Error: ID no encontrado en la base de datos.");
	    } catch (ConstraintViolationException e) {
	        System.out.println("Error: Violación de restricciones en la base de datos.");
	    } catch (TransactionException e) {
	        System.out.println("Error: No se pudo completar la transacción.");
	    } catch (HibernateException e) {
	        System.out.println("Error: Problema con Hibernate. Intenta nuevamente.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: Se intentó asignar un valor inválido.");
	    } catch (Exception e) {
	        System.out.println("Error inesperado: " + e.getMessage());
	    }
	}
	
	
	/*
	 * Metodo para modificar un coche
	 * Para ello se pide el id del coche a modificar
	 * Se crea un submenu para seleccionar que dato quieres modificar
	 * El id de coche no puede ser modificado
	 * Se pedirá confirmación para ejecutar el cambio
	 */
	public static void modificarCoche(Session sesion, Transaction tx) {
	    try {
	        System.out.println("Modificar Coches");
	        Scanner sc = new Scanner(System.in);
	        Scanner sc2 = new Scanner(System.in);
	        Scanner sc3 = new Scanner(System.in);
	        Boolean seguir = false;
	        System.out.print("Escribe el id del coche que quieras modificar: ");
	        int id = sc2.nextInt();
	       
	        Coches coche = new Coches();
	        coche = (Coches) sesion.get(Coches.class, id);
	        while (!seguir) {
	        	System.out.println("¿Que dato quieres modificar?");
	 	        mostrarCoche(sesion, id);
	 	        System.out.println("7.Salir y guardar");
	 	        System.out.println("8.Salir sin guardar");
	            System.out.println("Dato a modificar: ");
	            int dato = sc2.nextInt();
	            switch (dato) {
	                case 1:
	                    System.out.println("Introduce la marca del coche");
	                    String marca = sc.nextLine();
	                    coche.setMarca(marca);
	                    break;
	                case 2:
	                    System.out.println("Introduce el modelo del coche");
	                    String modelo = sc.nextLine();
	                    coche.setModelo(modelo);
	                    break;
	                case 3:
	                    System.out.println("Introduce el color del coche");
	                    String color = sc.nextLine();
	                    coche.setColor(color);
	                    break;
	                case 4:
	                    System.out.println("Introduce la matricula del coche");
	                    String matricula = sc.nextLine();
	                    coche.setMatricula(matricula);
	                    break;
	                case 5:
	                    System.out.println("Introduce el nº de plazas del coche");
	                    Byte plazas = sc3.nextByte();
	                    coche.setPlazas(plazas);
	                    break;
	                case 6:
	                    System.out.println("Introduce los extras del coche");
	                    String extras = sc.nextLine();
	                    coche.setExtras(extras);
	                    break;
	                case 7:
	                    System.out.println("¿Estas seguro que quieres salir y guardar? si/no");
	                    String res = sc.nextLine();
	                    if (res.equalsIgnoreCase("si")) {
	                        sesion.save(coche);
	                        tx.commit();
	                        System.out.println("Coche guardado con exito");
	                        seguir = true;
	                    }
	                    System.out.println("Volviendo al menú");
	                    break;
	                case 8:
	                    System.out.println("¿Estas seguro que quieres salir sin guardar? si/no");
	                    String res2 = sc.nextLine();
	                    if (res2.equalsIgnoreCase("si")) {
	                        System.out.println("Saliendo sin guardar");
	                        seguir = true;
	                    }
	                    System.out.println("Volviendo al menú");
	                    break;
	                default:
	                    System.out.println("Opción no válida. Intenta nuevamente.");
	                    break;
	            }
	        }
	    } catch (InputMismatchException e) {
	        System.out.println("Error: El valor ingresado no es válido. Por favor, intenta nuevamente.");
	    } catch (ObjectNotFoundException e) {
	        System.out.println("Error: ID no encontrado en la base de datos.");
	    } catch (ConstraintViolationException e) {
	        System.out.println("Error: Violación de restricciones en la base de datos.");
	    } catch (TransactionException e) {
	        System.out.println("Error: No se pudo completar la transacción.");
	    } catch (HibernateException e) {
	        System.out.println("Error: Problema con Hibernate. Intenta nuevamente.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: Se intentó asignar un valor inválido.");
	    } catch (Exception e) {
	        System.out.println("Error inesperado: " + e.getMessage());
	    }
	}
	
	/*
	 * Metodo para mostrar los datos de clientes por ciudad
	 * Se pedirá la ciudad al usuario
	 * Se mostrarán los clientes de esa ciudad en caso de que existan
	 */
	public static void mostrarDatosClientesPorCiudad(Session sesion) {
	    try {
	        System.out.println("Mostrar datos de clientes por ciudad");
	        Scanner sc = new Scanner(System.in);
	        System.out.println("Escribe el nombre de la ciudad: ");
	        String ciudad = sc.nextLine();
	        Query q = sesion.createQuery("from Cliente as cli where cli.ciudad = :ciudad");
	        q.setParameter("ciudad", ciudad);
	        List<Cliente> listaCli = q.list();
	        Iterator<Cliente> iterador = listaCli.iterator();
	        while (iterador.hasNext()) {
	            Cliente cli = (Cliente) iterador.next();
	            mostrarCliente(sesion, cli.getIdCliente());
	            System.out.println("\n\n****************************************");
	        }
	        System.out.println("Numero de clientes de " + ciudad + " = " + listaCli.size());
	    } catch (HibernateException e) {
	        System.out.println("Error al consultar los datos de los clientes: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	    }
	}

	
	
	/*
	 * Metodo para mostrar los datos de coche por nº de puertas
	 * Se pedirá el numero de puertas
	 * Se mostraran los coches que cumplan ese requisito
	 */
	public static void mostrarDatosVehiculoPorPlazas(Session sesion) {
	    try {
	        System.out.println("Mostrar datos de coches por numero de plazas");
	        Scanner sc = new Scanner(System.in);
	        System.out.println("Escribe el numero de plazas: ");
	        Byte plazas = sc.nextByte();
	        Query q = sesion.createQuery("from Coches as coche where coche.plazas = :plazas");
	        q.setParameter("plazas", plazas);
	        List<Coches> listacoches = q.list();
	        Iterator<Coches> iterador = listacoches.iterator();
	        while (iterador.hasNext()) {
	            Coches coche = (Coches) iterador.next();
	            mostrarCoche(sesion, coche.getIdCoche());
	            System.out.println("\n\n****************************************");
	        }
	        System.out.println("Numero de coches con " + plazas + " plazas = " + listacoches.size());
	    } catch (HibernateException e) {
	        System.out.println("Error al consultar los datos de los coches: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	    }
	}

	
	/*
	 * Metodo para mostrar datos en función la fecha que se haya introducido
	 */
	public static void seleccionarDatosPorRangoDeFechas(Session sesion, Transaction tx) {
	    try {
	        System.out.println("Mostrar datos de venta por rango de fechas");
	        System.out.println("Escribe la fecha de venta (yyyy-mm-dd)");
	        Scanner sc = new Scanner(System.in);
	        String fechaV = sc.nextLine();

	        String sql = "SELECT c.marca, c.modelo, cli.nombre, cli.ciudad, cli.edad, cli.sexo, v.precio, v.fecha_compra "
	                + "FROM Venta v JOIN Coches c ON v.coche = c.id_coche JOIN Cliente cli "
	                + "ON v.cliente = cli.id_cliente WHERE v.fecha_compra = :fechav";
	        Query q = sesion.createNativeQuery(sql);
	        q.setParameter("fechav", fechaV);
	        List<Object[]> result = q.list();
	        mostrarDatos(result);

	        System.out.println("Escribe la fecha de entrega(yyyy-mm-dd)");
	        String fechaE = sc.nextLine();
	        String sql2 = "SELECT c.marca, c.modelo, cli.nombre, cli.ciudad, cli.edad, cli.sexo, v.precio, v.fecha_compra "
	                + "FROM Venta v JOIN Coches c ON v.coche = c.id_coche JOIN Cliente cli "
	                + "ON v.cliente = cli.id_cliente WHERE v.fecha_entrega = :fechae";
	        q = sesion.createNativeQuery(sql2);
	        q.setParameter("fechae", fechaE);
	        result = q.list();
	        mostrarDatos(result);

	        // Confirmar la transacción
	        tx.commit();
	    } catch (HibernateException e) {
	        System.out.println("Error al realizar la consulta: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	    }
	}

	
	/*
	 * Metodo para mostrar los datos
	 */
	public static void mostrarDatos(List<Object[]> result) {
		// Mostrar los resultados
        for (Object[] dato : result) {
            String marca = (String) dato[0];
            String modelo = (String) dato[1];
            String nombreCliente = (String) dato[2];
            String ciudadCliente = (String) dato[3];
            Byte edadCliente = (byte) dato[4];
            String sexoCliente = (String) dato[5];
            int precio = (int) dato[6];
            Date fechaCompra = (Date) dato[7];

            // Imprimir los datos
            System.out.println("Marca: " + marca);
            System.out.println("Modelo: " + modelo);
            System.out.println("Cliente: " + nombreCliente);
            System.out.println("Ciudad: " + ciudadCliente);
            System.out.println("Edad: " + edadCliente);
            System.out.println("Sexo: " + sexoCliente);
            System.out.println("Precio: " + precio);
            System.out.println("Fecha de Compra: " + fechaCompra);
            System.out.println("---------------------------------------------------");
         
        }
	}
	
	/*
	 * Metodo que permite que se introduzca una consulta generica
	 */
	public static void consultaGenerica(Session sesion,Transaction tx) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce una consulta generica");
		String consulta = sc.nextLine();
		Boolean permitida  = validarConsulta(consulta);
		if(permitida) {
			 try {
		            // Ejecutar la consulta proporcionada por el usuario
		            Query q = sesion.createNativeQuery(consulta);
		            List<Object[]> resultados = q.list();

		            // Mostrar resultados
		            mostrarResultados(resultados);

		            tx.commit();
		        } catch (Exception e) {
		            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
		        }
		}else {
			System.out.println("Consulta no permitida");
		}
		
		
	}
	
	/*
	 * Metodo para validar la consulta
	 */
	public static boolean validarConsulta(String consulta) {
		consulta =consulta.toLowerCase();
		return !(consulta.contains("delete") || consulta.contains("drop") || consulta.contains("update"));
	}
	
	/*
	 * Metodo para mostrar los resultados de la consulta generica
	 */
	private static void mostrarResultados(List<Object[]> resultados) {
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        }else {
        	for (Object[] fila : resultados) {
                for (Object campo : fila) {
                    System.out.print(campo + "\n");
                }
                
                System.out.println("***************************");
            }
        }

        
    }
	
	/*
	 * Metodo para mostrar ventas
	 */
	public static void mostrarVenta(Session sesion,int id) throws ObjectNotFoundException{
		Venta vt = new Venta();
		vt = (Venta) sesion.load(Venta.class, id);
		System.out.println("ID de venta = " + vt.getIdVenta());
		System.out.println("1.Fecha de compra = " + vt.getFechaCompra());
		System.out.println("2.Fecha de entrega = " + vt.getFechaEntrega());
		System.out.println("3.Precio de venta = " + vt.getPrecio());
		System.out.println("4.ID del Cliente = " + vt.getCliente().getIdCliente());
		System.out.println("5.Coche = " + vt.getCoches().getMatricula());
	}
	
	/*
	 * metodo para mostrar clientes
	 */
	public static void mostrarCliente(Session sesion,int id) throws ObjectNotFoundException{
		Cliente cli = new Cliente();
		cli = (Cliente) sesion.load(Cliente.class, id);
		System.out.println("ID del cliente = " + cli.getIdCliente());
		System.out.println("1.DNI del cliente = " + cli.getDni());
		System.out.println("2.Nombre = " + cli.getNombre());
		System.out.println("3.Direccion del cliente = " + cli.getDireccion());
		System.out.println("4.Ciudad del cliente = " + cli.getCiudad());
		System.out.println("5.Telefono del cliente = " + cli.getTelefono());
		System.out.println("6.Email del cliente = " + cli.getEmail());
		System.out.println("7.Edad del cliente = " + cli.getEdad());
		System.out.println("8.Sexo del cliente = " + cli.getSexo());
	}
	
	/*
	 * Metodo para mostrar coches
	 */
	public static void mostrarCoche (Session sesion,int id) throws ObjectNotFoundException{
		Coches coche = new Coches();
		coche = (Coches) sesion.load(Coches.class, id);
		System.out.println("ID del coche = " + coche.getIdCoche());
		System.out.println("1.Marca del coche = " + coche.getMarca());
		System.out.println("2.Modelo del coche = " + coche.getModelo());
		System.out.println("3.Color del coche = " + coche.getColor());
		System.out.println("4.Matricula = " + coche.getMatricula());
		System.out.println("5.Nº Plazas = " + coche.getPlazas());
		System.out.println("6.Extras = "  + coche.getExtras());
	}
	
	/*
	 * Metodo que devuelve el ultimo id en una tabla especifica
	 */
	public static int devolverIDMax(String nombreTabla, String nombreid,Session sesion) {
			String sql = "SELECT MAX("+nombreid+ ") FROM " + nombreTabla;
			Query qslq = sesion.createNativeQuery(sql);
			return (int) qslq.uniqueResult();
	}
	

}
