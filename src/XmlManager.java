/*
  Author: Ismael
  Desc: Clase encargada de gestionar el flujo de informacion relacionado con el XML.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;

public class XmlManager {
    //Atributos
    private final DocumentBuilder documentBuilder;
    private Document document;
    private Node root;

    //Constructor
    public XmlManager() throws ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = builderFactory.newDocumentBuilder();
    }

    //Metodo encargado de crear un archivo XML.
    public void createXML() {
        String op;
        //Comprovamos si exixte el fichero alumnes.xml o no y preguntamos al usuario si desea sobreescribir o no
        if (new File("Alumnes.xml").exists()) {
            op = TextManager.fileExist();
        } else {
            op = "No";
        }

        //Si la opcion dada por el usuario es yes creamos el fichero alumnes.xml, si es no no hacemos nada.
        switch (op) {
            case "Yes":
                document = documentBuilder.newDocument();
                root = document.createElement("registre_alumnes");
                document.appendChild(root);
                try {
                    saveFile();
                } catch (TransformerException e) {
                    JOptionPane.showInputDialog("Ha ocurrido un error al generar el XML");
                }
                break;
            case "No":
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opcion errronea elija Yes o No", "Error!", JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

    //Metodo encargado de introducir datos dentro del fichero XML.
    public void inputData() throws IOException, SAXException {
        //Cargamos el fichero.
        if (loadFile())
            return;
        //Mostramos un menu de opciones y recogemos los resultados.
        ArrayList<String> datas = TextManager.inputDataText();

        //Creamos los elemntos utilizando los datos del arraylist.
        Element subroot = document.createElement("alumne");
        subroot.setAttribute("codi_alumne", datas.get(0));
        Element name = document.createElement("nom_alumne");
        name.setTextContent(datas.get(1));
        Element course = document.createElement("curs");
        course.setTextContent(datas.get(2));
        Element year = document.createElement("any_naixament");
        year.setTextContent(datas.get(3));
        Element school = document.createElement("colegi");
        school.setTextContent(datas.get(4));

        //Añadimos todos los elemntos a su correspondiente padre.
        root.appendChild(subroot);
        subroot.appendChild(name);
        subroot.appendChild(course);
        subroot.appendChild(year);
        subroot.appendChild(school);

        try {
            //Guardamos los datos en el archivo.
            saveFile();
        } catch (TransformerException e) {
            JOptionPane.showInputDialog("Ha ocurrido un error al guardar el alumno");
        }

    }

    //Metodo encargado de modificar datos de un nodo segun el ID del alumno.
    public void modifyData() throws IOException, SAXException, TransformerException {
        //Cargamos el XML
        if (loadFile())
            return;
        //Recogemos todos los nodos del elemento raiz.
        NodeList nodes = root.getChildNodes();

        //Pedimos el ID del alumno y el dato que se va ha camviar.
        String code = JOptionPane.showInputDialog("¿Codigo del alumno?");
        String data = TextManager.modifyDataText();

        //Generamos un bucle para buscar el ID del almumno. En caso de no exitir se lanza un mensaje de error.
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getAttributes().getNamedItem("codi_alumne").getNodeValue().equals(code)) {
                NodeList nodeList = nodes.item(i).getChildNodes();

                for (int j = 0; j < nodeList.getLength(); j++) {
                    if (nodeList.item(j).getNodeName().equals(data)) {
                        String newData = JOptionPane.showInputDialog("Introduce el nuevo dato");

                        nodeList.item(j).setTextContent(newData);
                        saveFile();
                        return;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Codigo introducido inexistente", "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    //Metodo en cargado de mostrar todos los datos del XML.
    public void showAllXml() throws IOException, SAXException, XPathExpressionException, TransformerException {
        //Cargamos el fichero.
        if (loadFile())
            return;
        //Lo formateamos
        format();
        //Generamos un Xpath para poder lanzar consultas al archivo.
        XPath xPath = XPathFactory.newInstance().newXPath();
        //Lanzamos una consulta que nos retornara todos los alumnos del fichero
        NodeList nodes = (NodeList) xPath.evaluate("//alumne", document, XPathConstants.NODESET);
        //Lanzmos otra consulta que nos lanzará que nos retorna todos los nodos del fichero.
        String s = (String) xPath.evaluate("//*", document, XPathConstants.STRING);
        //Comprobamos que el fichero no este vacio.
        if (nodes.getLength() == 0) {
            JOptionPane.showMessageDialog(null, "No hay registros");
            return;
        }
        //Mostramos los datos del fichero.
        JOptionPane.showMessageDialog(null, s);

//        for (int i = 0; i < nodes.getLength(); i++) {
//            Element alumne = (Element) nodes.item(i);
//            String f = (String) xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING);
//            String f2 = (String) xPath.evaluate("curs", alumne, XPathConstants.STRING);
//            String f3 = (String) xPath.evaluate("any_naixament", alumne, XPathConstants.STRING);
//            String f4 = (String) xPath.evaluate("colegi", alumne, XPathConstants.STRING);
//            System.out.println("Nombre= " + f);
//            System.out.println("Curso= " + f2);
//            System.out.println("Año de nacimiento= " + f3);
//            System.out.println("Colegio= " + f4);
//            System.out.println("----------");
//        }

    }

    //Metodo encargado de hacer consultas al XML y mostrar el resultado.
    public void inquiries() throws IOException, SAXException, XPathExpressionException {
        //Cargamos el fichero.
        if (loadFile())
            return;
        //Generamos una bandera, un Xpath y una lista de nodos para poder hacer las consultas necesarias.
        boolean exit = false;
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate("//alumne", document, XPathConstants.NODESET);
        while (!exit) {
            //Mostramos un menu de opciones y segun la elejida relizamos una consulta u otra.
            String inquirie = TextManager.inquiriesText();
            switch (inquirie) {
                //Mostramos el nombre de todos los alumnos.
                case "1":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        JOptionPane.showMessageDialog(null, xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                    }
                    break;
                //MOstramos los alumnos que pertenecen al Cide.
                case "2":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        JOptionPane.showMessageDialog(null, xPath.evaluate("colegi='cide' or colegi='Cide'", alumne, XPathConstants.STRING));
                    }
                    break;
                //Mostramos el alumno con ID=3.
                case "3":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        if (alumne.getAttribute("codi_alumne").equals("3")) {
                            JOptionPane.showMessageDialog(null, xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                        }
                    }
                    break;
                //Mostramos todos los alumnos nacidos antes de 1990.
                case "4":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        int intYear = Integer.parseInt((String) xPath.evaluate("any_naixament", alumne, XPathConstants.STRING));
                        if (intYear <= 1990) {
                            JOptionPane.showMessageDialog(null, "Alumo nacido antes de 1990: " + xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                        }
                    }
                    break;
                //Permitimos la salida del bucle.
                case "0":
                    exit = true;
                    break;

            }

        }
    }

    //Metodo encargado de eliminar un registro del archivo.
    public void deletData() throws IOException, SAXException, TransformerException {
        //Cargamos el fichero.
        if (loadFile()) {
            return;
        }
        NodeList nodes = root.getChildNodes();
        //Mostramos un menu para pedir que ID desa eliminar.
        String code = TextManager.deleteDataText();
        //Buscamos el ID y eliminamos todos sus reistros.
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getAttributes().getNamedItem("codi_alumne").getNodeValue().equals(code))
                root.removeChild(nodes.item(i));
        }
        //Guardamos el resultado
        saveFile();
    }

    //Metodo encargado de guardar todos los cambios que se realizen sobre el fichero.
    private void saveFile() throws TransformerException {
        //Usamos los metodos transform para guardar el fichero.
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult("Alumnes.xml");
        transformer.transform(source, result);
    }

    //Metodo encargado de cargar el fichero en memoria para poder tratar con el.
    private boolean loadFile() throws IOException, SAXException {
        //Comprobamos si el fichero existe si es asi retornamos un false en caso contrario retornamos un mensaje de error.
        if (new File("Alumnes.xml").exists()) {
            document = documentBuilder.parse("Alumnes.xml");
            root = document.getFirstChild();
            return false;
        } else {
            JOptionPane.showMessageDialog(null, "Error! No hay un fichero XML", "Error!", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    //Metodo encargado de formatear el fichero.
    private void format() throws TransformerException, IOException, SAXException {
        //Para formatear el fichero usamos las propiedades de tranform que nos permites darle formato y guardamos el resultado en un String.
        StringWriter sw = new StringWriter();

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(document);

        transformer.transform(source, result);

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(sw.toString()));

        //Finalmente guardamos el string formateado dentro de nuestra variable documento.
        document = documentBuilder.parse(is);
    }

}
