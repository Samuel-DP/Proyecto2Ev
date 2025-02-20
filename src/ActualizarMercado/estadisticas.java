package ActualizarMercado;
import java.util.Scanner;

import com.google.gson.Gson;	//Estas librerias son herramientas de Google que ayudan a convertir JSON en objetos de java
import com.google.gson.GsonBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class estadisticas {
	
	Scanner sc = new Scanner(System.in);
	
	int menu() {
		System.out.println("Bienvenido al actualizador de mercado, que desea saber");
		System.out.println("1. Precio actual de BTC");
		System.out.println("2. Precio actual de ETH");
		System.out.println("3. Precio actual de XRP");
		System.out.println("4. Cuales son las empresas que tienen Bitcoin en balance");
		System.out.println("5. Cuantas veces ha caido BTC un 50% este año");
		System.out.println("6. Cuantas veces ha subido BTC un 50% este año");
		System.out.println("7. Maximo y minimo de BTC en el ultimo año");
		System.out.println("8. Volumen de transacciones de BTC en las ultimas 24h");
		return sc.nextInt();
	}
	
	
    // Método para mostrar el precio actual
    public String mostrarPrecioActual(Api api, String moneda) {
        String endpoint = "https://api.coingecko.com/api/v3/simple/price?ids=" + moneda + "&vs_currencies=usd";
        String respuesta = api.getApiData(endpoint); //llama al metodo getApiData que obtiene los datos de la api y los devueve en una string
        
        if (respuesta != null) {
            return "Precio actual de " + moneda.toUpperCase() + ": " + respuesta;
            
        } else {
            return "Error al obtener datos de la API.";
        }
    }
    
    
    public String tenenciaEmpresas(Api api) {
    	String endpoint= "https://api.coingecko.com/api/v3/companies/public_treasury/bitcoin";
		 String respuesta = api.getApiData(endpoint);
		    if (respuesta != null) {
		        try {
		            // Crear un objeto Gson con la funcion setPrettyPrinting para dar formato bonito al JSON
		            Gson gson = new GsonBuilder().setPrettyPrinting().create();

		            // Convertir el JSON en un objeto genérico sin especificar una clase concreta(object. class), esto permite trabajar con los datos en java
		            Object jsonObject = gson.fromJson(respuesta, Object.class);

		            // Convertir el objeto en un JSON bonito(prettyJson) que tendra saltos de linea y tabulaciones
		            return gson.toJson(jsonObject);


		        } catch (Exception e) {
		            return "Error al procesar el JSON: " + e.getMessage();
		        }
		    } else {
		        return "Error al obtener datos de la API.";
		    }
		}
    
    public String caida50 (Api api) {
    	String endpoint= "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=365&interval=daily";
   	 	String respuesta = api.getApiData(endpoint);
        if (respuesta != null) {
        	try {
        		
        		//Para poder trabajar con los datos en Java, primero tenemos que convertir el JSON en un objeto java
        		//Para eso usamos JsonParser de la libreria Gson
                JsonObject jsonObject = JsonParser.parseString(respuesta).getAsJsonObject();
                
                //Extraemos el array prices que contiene los datos que necesitamos
                JsonArray precios = jsonObject.getAsJsonArray("prices");
                
                if (precios == null || precios.size() == 0) {
                    return "No hay datos de precios disponibles.";
                }

                int caidas50 = 0;
                double maxPrecio = 0;

                // Recorrer los precios diarios, dentro del array de precios tengo otros 365 elementos q en realidad son 365 arays [1703721600000, 42000.25]
                for (JsonElement elemento : precios) {
                    JsonArray datos = elemento.getAsJsonArray(); //cambio el tipo de elemento a array para poder usar el get y poder extraer el precio USD
                    double precio = datos.get(1).getAsDouble(); // Obtener el precio en USD

                    if (precio > maxPrecio) {
                        maxPrecio = precio; // Si encontramos un nuevo maximo lo actualizamos
                    } else if (precio <= maxPrecio / 2) { // Si el precio baja un 50% contamos la caida
                        caidas50++;
                        maxPrecio = precio; // Resetear el máximo para evitar contar la misma caída varias veces
                    }
                }

                return "Bitcoin ha caído un 50% un total de " + caidas50 + " veces en el último año.";
        		
        	} catch (Exception e) {
                return "Error al procesar los datos: " + e.getMessage();
        		
        	}
     
        } else {
            return "Error al obtener datos de la API.";
        }
    	
    }
    
    public String subida50 (Api api) {

    	String endpoint= "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=365&interval=daily";
   	 	String respuesta = api.getApiData(endpoint);
   	 	
        if (respuesta != null) {
        	
            try {
        		//Convertimos el Json en un objeto de java
        		//Usamos JsonParser para convertir la respuesta en un objeto json
                JsonObject jsonObject = JsonParser.parseString(respuesta).getAsJsonObject();
                //Esto nos permite acceder al array "prices" y analizar los valores
                JsonArray precios = jsonObject.getAsJsonArray("prices");
                
                if (precios == null || precios.size() == 0) {
                    return "No hay datos de precios disponibles.";
                }
                
                int subidas50 = 0;
                double minPrecio = Double.MAX_VALUE; //=1.79E308 //Solo se usa al inicio para asegurarnos de que cualquier precio real sea menor y reemplace su valor

                //Recorremos los precios, dentro del array de precios tengo otros 365 elementos q en realidad son 365 arays [1703721600000, 42000.25]
                for (JsonElement elemento : precios) {
                    JsonArray datos = elemento.getAsJsonArray();//cambio el tipo de elemento a array para poder usar el get y poder extraer el precio USD
                    double precio = datos.get(1).getAsDouble(); //Obtiene el precio en USD ya que el 0 es el timestamp [1703721600000, 42000.25]

                    if (precio < minPrecio) {  
                        minPrecio = precio;  //Si encontramos nuevo minimo lo actualizamos 
                    } else if (precio >= minPrecio * 1.5) {  
                        subidas50++;   // Si el precio sube un 50%, contamos la subida
                        minPrecio = precio;  // Reiniciamos el mínimo para evitar contar la misma subida varias veces
                    }
                }

                return "Bitcoin ha subido un 50% un total de " + subidas50 + " veces en el último año.";

            } catch (Exception e) {
                return "Error al procesar los datos: " + e.getMessage();
            }
        } else {
            return "Error al obtener datos de la API.";
        }
        	
    }
    
    public String maxMinBtc (Api api) {
        String endpoint = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=365&interval=daily";
        String respuesta = api.getApiData(endpoint);

        if (respuesta != null) {
            try {
                JsonObject jsonObject = JsonParser.parseString(respuesta).getAsJsonObject();
                JsonArray precios = jsonObject.getAsJsonArray("prices");

                if (precios == null || precios.size() == 0) {
                    return "No hay datos de precios disponibles.";
                }

                double maxPrecio = Double.MIN_VALUE; // Inicializamos con el valor más bajo posible
                double minPrecio = Double.MAX_VALUE; // Inicializamos con el valor más alto posible

                //Recorremos cada elemento de dentro del array prices , que es otro array con estos datos [1703721600000, 42000.25]
                //Para acceder a los valores reales dentro del array , primero debemos extraer cada jsonElement y luego convertirlo en el tipo adecuado
                for (JsonElement elemento : precios) {
                    JsonArray datos = elemento.getAsJsonArray();//Convertimos a JsonArray
                    double precio = datos.get(1).getAsDouble(); // Obtiene el precio en USD

                    if (precio > maxPrecio) {
                        maxPrecio = precio; // Actualiza el precio máximo si encontramos uno mayor
                    }

                    if (precio < minPrecio) {
                        minPrecio = precio; // Actualiza el precio mínimo si encontramos uno menor
                    }
                }

                return "El precio más alto de BTC en el último año fue $" + maxPrecio + " y el más bajo fue $" + minPrecio + ".";

            } catch (Exception e) {
                return "Error al procesar los datos: " + e.getMessage();
            }
        } else {
            return "Error al obtener datos de la API.";
        }
    }
    
    public String VolTransacciones (Api api) {
    	String endpoint = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd&include_24hr_vol=true";
    	String respuesta = api.getApiData(endpoint);
    	
    	if (respuesta != null) {
    		try {
                // Convertir la respuesta JSON en un objeto JsonObject
                JsonObject jsonObject = JsonParser.parseString(respuesta).getAsJsonObject();
                
                // Extraer el objeto "bitcoin" que contiene los datos que me interesan
                JsonObject bitcoinData = jsonObject.getAsJsonObject("bitcoin");
                
                // Obtener el valor del volumen de transacciones en 24h
                double volumen24h = bitcoinData.get("usd_24h_vol").getAsDouble();
                
                // Devolver solo el volumen en formato claro
                return "El volumen de transacciones en BTC en las últimas 24 horas es: $" + volumen24h;
                
    		} catch (Exception e) {
                return "Error al procesar los datos: " + e.getMessage(); 	
    		}
    		
    	} else {
    		return "Error al obtener los datos de la api";
    	}
    	
    }
    
    public void programa (Api api) { //utilizo como parametro el objeto api , xq este contiene el metodo getApiData, que se usa para interactuar con la api de coingecko
    	String respuesta="si";
    	
    	do {
        	int opcion = menu();
        	String datosAExportar=""; //Variable para almacenar los datos obtenidos
        	
        	switch(opcion) {
        	
        	case 1:
        		datosAExportar = mostrarPrecioActual(api, "bitcoin");
        		System.out.println(datosAExportar);
        		break;
        	case 2:
        		datosAExportar = mostrarPrecioActual(api, "ethereum");
        		System.out.println(datosAExportar);
        		break;
        	case 3:
        		datosAExportar = mostrarPrecioActual(api, "ripple");
        		System.out.println(datosAExportar);
        		break;
        	case 4: 
        		datosAExportar = tenenciaEmpresas(api);
        		System.out.println(datosAExportar);
        		break;
        	case 5:
        		datosAExportar = caida50(api);
        		System.out.println(datosAExportar);
        		break;
        	case 6:
        		datosAExportar = subida50(api);
        		System.out.println(datosAExportar);
        		break;
        	case 7:
        		datosAExportar = maxMinBtc(api);
        		System.out.println(datosAExportar);
        		break;
        	case 8:
        		datosAExportar = VolTransacciones(api);
        		System.out.println(datosAExportar);
        		break;
        	} 
        	
        	System.out.println("Quiere exportar estos datos a un archivo externo? si/no");
        	sc.nextLine();
        	respuesta= sc.nextLine();
        	
        	if (respuesta.equals("si")) {
        		System.out.println("Ingrese el nombre del archivo (ejemplo: datos.txt o datos.pdf)");
        		String nombreArchivo = sc.nextLine();
        		
        		if (nombreArchivo.endsWith(".pdf")) {
        			Archivos.guardarEnPDF(nombreArchivo, datosAExportar);
        		} else {
        			Archivos.guardarEnArchivo(nombreArchivo, datosAExportar);
        		}
        		
        	} 
        	
        	System.out.println("Desea seguir consultando datos?? si/no");
        	respuesta= sc.nextLine();
        	System.out.println("---------------------------------------");
        	System.out.println("                                       ");
        	System.out.println("                                       ");
        	System.out.println("---------------------------------------");
        	
    	} while(respuesta.equals("si"));
    	
    	System.out.println("Hasta la proxima!!!");

    }


}
