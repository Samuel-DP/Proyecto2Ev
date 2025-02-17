package ActualizarMercado;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.itextpdf.kernel.pdf.*;	//estas librerias son para poder crear un archivo pdf
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class Archivos {
	
	public static void guardarEnArchivo(String nombreArchivo, String contenido) {
		
		//con el true estoy abriendo el archivo en modo append para que cada vez que se abra añada registros sin borrar los anteriores 
		try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo,true))) {
			writer.println(contenido);
			System.out.println("Datos guardados en " + nombreArchivo);
			
		} catch(IOException e) {
			System.out.println("Error al crear el archivo " + e.getMessage());
		}
		
	}
        
    public static void guardarEnPDF(String nombreArchivo, String contenido) {
        try {
            // Crear un documento PDF
            PdfWriter writer = new PdfWriter(nombreArchivo);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Agregar contenido al PDF
            document.add(new Paragraph(contenido));

            // Cerrar el documento
            document.close();
            System.out.println("Datos guardados en " + nombreArchivo);
            
        } catch (IOException e) {
            System.out.println("Error al crear el archivo PDF: " + e.getMessage());
        }
    }
	
 
}
