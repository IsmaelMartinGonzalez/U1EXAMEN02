import javax.swing.*;
import java.util.ArrayList;

public class TextManager {
    public static String mainMenu() {
        return JOptionPane.showInputDialog("***********************\n" +
                "1.- Crear un ficheroXML\n" +
                "2.- Introducir datos en el fichero XML\n" +
                "3.- Mostrar el contenido del fichero XML\n" +
                "4.- Modificar datos\n" +
                "5.- Consultas\n" +
                "6.-Eliminar un registro\n" +
                "0.- Salir\n" +
                "***********************");
    }

    public static ArrayList<String> inputDataText() {
        ArrayList<String> datas = new ArrayList<String>();
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

        String data = JOptionPane.showInputDialog("¿Que dato deseas modificar?\n" +
                "1.- Nombre del alumno\n" +
                "2.- Curso del alumno\n" +
                "3.- Año de nacimientodel alumno\n" +
                "4.- Colegio del alumno");
        switch (data) {
            case "1":
                data = "nom_alumne";
                break;
            case "2":
                data = "curs";
                break;
            case "3":
                data = "any_naixament";
                break;
            case "4":
                data = "colegi";
                break;
        }
        return data;
    }

    public static String inquiriesText() {
        return JOptionPane.showInputDialog("***********************\n" +
                "1.- Consultar todos los nombre de los alumnos\n" +
                "2.- Consultar los alumnos que vayan al colegio Cide\n" +
                "3.- Consultar el nombre del alumno con codigo 3\n" +
                "4.- Consultar los alumnos nacidos antes de 1990\n" +
                "0.- Salir\n" +
                "***********************");


    }

    public static String deleteDataText() {
        return JOptionPane.showInputDialog("¿Codigo del alumno a borrar?");
    }

    public static String fileExist() {
        return JOptionPane.showInputDialog(null, "El fichero Alumnes.xml ya existe.\n" +
                "¿Desea volver a crear el archivo?(Yes/No)");
    }
}
