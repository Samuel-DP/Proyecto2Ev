package ActualizarMercado;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.IOException;

public class Api {
	
    // Método para conectar con la API
	
	//String endpoint es la URL de los datos que queremos obtener
    public String getApiData(String endpoint) { // este metodo devuelve una string con los datos de la api
        try {
            // Crear una conexión a la URL
            URL url = new URL(endpoint); //Creo un objeto url con la direccion (endopoint) como parametro
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Se establece una conexion HTTP con el objeto url
            conn.setRequestMethod("GET"); //Configura el metodo HTTP como GET que es el utilizado para leer datos de la api
            conn.connect(); //se establece la conexion con el servidor 

            // Verificar el código de respuesta
            int responseCode = conn.getResponseCode(); //se obtiene el codigo de respuesta HTTP
            if (responseCode != 200) { 
                throw new RuntimeException("Error en la conexión: " + responseCode); //Algo salio mal en la conexion y muestra el error relacionado con el servidor
            }

            // Leer la respuesta
            StringBuilder informacion = new StringBuilder(); //creo un objeto de Stringbuilder para almacenar la infomacion recibida desde la api
            Scanner sc = new Scanner(url.openStream());// Abre un flujo de datos desde la api (url.openStream()) y utiliza scanner para leerlos linea por linea
                while (sc.hasNext()) { //mientras el flujo de datos tenga informacion
                    informacion.append(sc.nextLine()); //cada linea de flujo se añadirá al StringBuilder
                }
            

            return informacion.toString(); //una vez toda la info ha sido leida y almacenada en el StringBuilder se convierte en un String y se devuelve como resultado del metodo

        } catch (IOException e) {
            System.out.println("Error al obtener los datos de la api " + e.getMessage());
            return null;
        }
    }

}
