import javax.swing.*;
import java.util.ArrayList;

public class TextManager {
    public static String mainMenu() {
        return JOptionPane.showInputDialog("""
                ***********************
                1.- Crear un ficheroXML
                2.- Introducir datos en el fichero XML
                3.- Mostrar el contenido del fichero XML
                4.- Modificar datos
                5.- Consultas
                6.-Eliminar un registro
                0.- Salir
                ***********************""");
    }

    public static ArrayList<String> inputDataText() {
        ArrayList<String> datas = new ArrayList<>();
        String code = JOptionPane.showInputDialog("¿Codigo del alumno?");
        String studentName = JOptionPane.showInputDialog("¿Nombre del alumno?");
        String cours = JOptionPane.showInputDialog("¿Curso del alumno?");
        String yearAge = JOptionPane.showInputDialog("¿Año de nacimiento del alumno?");
        String schoolName = JOptionPane.showInputDialog("¿Escuela del alumno?");
        datas.add(code);
        datas.add(studentName);
        datas.add(cours);
        datas.add(yearAge);
        datas.add(schoolName);
        return datas;
    }

    public static String modifyDataText() {

        String data = JOptionPane.showInputDialog("""
                ¿Que dato deseas modificar?
                1.- Nombre del alumno
                2.- Curso del alumno
                3.- Año de nacimientodel alumno
                4.- Colegio del alumno""");
        if ("1".equals(data)) {
            data = "nom_alumne";
        } else if ("2".equals(data)) {
            data = "curs";
        } else if ("3".equals(data)) {
            data = "any_naixament";
        } else if ("4".equals(data)) {
            data = "colegi";
        }
        return data;
    }

    public static String inquiriesText() {
        return JOptionPane.showInputDialog("""
                ***********************
                1.- Consultar todos los nombre de los alumnos
                2.- Consultar los alumnos que vayan al colegio Cide
                3.- Consultar el nombre del alumno con codigo 3
                4.- Consultar los alumnos nacidos antes de 1990
                0.- Salir
                ***********************""");


    }

    public static String deleteDataText() {
        return JOptionPane.showInputDialog("¿Codigo del alumno a borrar?");
    }

    public static String fileExist() {
        return JOptionPane.showInputDialog(null, "El fichero Alumnes.xml ya existe.\n" +
                "¿Desea volver a crear el archivo?(Yes/No)");
    }
}
