package ActualizarMercado;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.itextpdf.kernel.pdf.*;	//estas librerias son para poder crear un archivo pdf
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class Archivos {
	
	//es un metodo estatico lo que significa que puedo llamarlo sin crear un objeto de la clase archivos
	public static void guardarEnArchivo(String nombreArchivo, String contenido) {
		
		//con el true estoy abriendo el archivo en modo append para que cada vez que se abra añada registros sin borrar los anteriores 
		//Abro o creo un archivo con el nombre indicado
		try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo,true))) {
			writer.println();
			writer.println(contenido);// escribo contenido dentro del archivo
			System.out.println("Datos guardados en " + nombreArchivo);
			
		} catch(IOException e) {
			System.out.println("Error al crear el archivo " + e.getMessage());
		}
		
	}
        
    public static void guardarEnPDF(String nombreArchivo, String contenido) {
        try {
            File file = new File(nombreArchivo);

            if (file.exists()) {
                // Leer el contenido existente del PDF
                PdfReader reader = new PdfReader(nombreArchivo);
                PdfDocument pdfDoc = new PdfDocument(reader);
                StringBuilder contenidoExistente = new StringBuilder();

                int totalPaginas = pdfDoc.getNumberOfPages();
                for (int i = 1; i <= totalPaginas; i++) {
                    contenidoExistente.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i))).append("\n");
                }

                pdfDoc.close();
                reader.close();

                // Crear un nuevo PDF con el contenido antiguo + el nuevo
                PdfWriter writer = new PdfWriter(nombreArchivo);
                PdfDocument nuevoPdf = new PdfDocument(writer);
                Document document = new Document(nuevoPdf);

                // Agregar el contenido previo y el nuevo contenido
                document.add(new Paragraph(contenidoExistente.toString()));
                document.add(new Paragraph("\n\n")); // Espaciado
                document.add(new Paragraph(contenido));

                document.close();
                System.out.println("Datos añadidos correctamente a " + nombreArchivo);

            } else {
                // Si el archivo no existe, simplemente creamos uno nuevo
                PdfWriter writer = new PdfWriter(nombreArchivo);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph(contenido));
                document.close();

                System.out.println("Archivo creado y datos guardados en " + nombreArchivo);
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo PDF: " + e.getMessage());
        }
    }

}