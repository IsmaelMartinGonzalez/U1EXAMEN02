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

    //Methods

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
            case "yes":
                document = documentBuilder.newDocument();
                root = document.createElement("registre_alumnes");
                document.appendChild(root);
                try {
                    saveFile();
                } catch (TransformerException e) {
                    JOptionPane.showInputDialog("Ha ocurrido un error al generar el XML");
                }
                break;
            case "no":
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opcion errronea elija yes o no", "Error!", JOptionPane.ERROR_MESSAGE);
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
    public void modifyData() throws IOException, SAXException, TransformerException, XPathExpressionException {
        //Cargamos el XML
        if (loadFile())
            return;
        if (registry()) {
            //Recogemos todos los nodos del elemento raiz.
            NodeList nodes = root.getChildNodes();

            //Pedimos el ID del alumno.
            String code = JOptionPane.showInputDialog("¿Codigo del alumno?");
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getAttributes().getNamedItem("codi_alumne").getNodeValue().equals(code)) {
                    NodeList nodeList = nodes.item(i).getChildNodes();
                    //Pedimos el dato que se va ha canviar.
                    String data = TextManager.modifyDataText();
                    if (data != null) {
                        for (int j = 0; j < nodeList.getLength(); j++) {
                            if (nodeList.item(j).getNodeName().equals(data)) {
                                String newData = JOptionPane.showInputDialog("Introduce el nuevo dato");
                                nodeList.item(j).setTextContent(newData);
                                saveFile();
                                return;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Opcion erronea", "ERROR!", JOptionPane.ERROR_MESSAGE);
                        modifyData();
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Codigo introducido inexistente", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo en cargado de mostrar todos los datos del XML.
    public void showAllXml() throws IOException, SAXException, XPathExpressionException, TransformerException {
        //Cargamos el fichero.
        if (loadFile())
            return;
        //Comprobamos que el fichero no este vacio.
        if (registry()) {
            //Lo formateamos
            format();
            //Generamos un Xpath para poder lanzar consultas al archivo.
            XPath xPath = XPathFactory.newInstance().newXPath();
            //Lanzamos la consulta que nos retorna todos los nodos del fichero y mostramos los datos del fichero.
            JOptionPane.showMessageDialog(null, xPath.evaluate("/*", document, XPathConstants.STRING));
        }
    }

    //Metodo encargado de hacer consultas al XML y mostrar el resultado.
    public void inquiries() throws IOException, SAXException, XPathExpressionException {
        //Cargamos el fichero.
        if (loadFile())
            return;

        //Si no hay registros en XML no se podrán hacer consultas
        if (registry()) {
            //Generamos una bandera, un Xpath y una lista de nodos para poder hacer las consultas necesarias.
            boolean exit = false;
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes;
            while (!exit) {
                //Mostramos un menu de opciones y segun la elejida relizamos una consulta u otra.
                String inquirie = TextManager.inquiriesText();
                switch (inquirie) {
                    //Mostramos el nombre de todos los alumnos.
                    case "1" -> {
                        nodes = (NodeList) xPath.evaluate("//nom_alumne", document, XPathConstants.NODESET);
                        for (int i = 0; i < nodes.getLength(); i++) {
                            JOptionPane.showMessageDialog(null, nodes.item(i).getTextContent());
                        }
                    }
                    //MOstramos los alumnos que pertenecen al Cide.
                    case "2" -> {
                        nodes = (NodeList) xPath.evaluate("//alumne[colegi='cide']", document, XPathConstants.NODESET);
                        for (int i = 0; i < nodes.getLength(); i++) {
                            JOptionPane.showMessageDialog(null, nodes.item(i).getFirstChild().getTextContent());
                        }
                    }
                    //Mostramos el alumno con ID=3.
                    case "3" -> {
                        Node node = (Node) xPath.evaluate("//alumne[@codi_alumne='3']", document, XPathConstants.NODE);
                        if (node == null) {
                            JOptionPane.showMessageDialog(null, "No hay alumno con codigo = 3");
                        } else {
                            JOptionPane.showMessageDialog(null, node.getFirstChild().getTextContent());
                        }
                    }
                    //Mostramos todos los alumnos nacidos antes de 1990.
                    case "4" -> {
                        nodes = (NodeList) xPath.evaluate(" //alumne[any_naixament<=1990]", document, XPathConstants.NODESET);
                        for (int i = 0; i < nodes.getLength(); i++) {
                            JOptionPane.showMessageDialog(null, "Alumo nacido antes de 1990: " + nodes.item(i).getFirstChild().getTextContent());
                        }
                    }
                    //Permitimos la salida del bucle.
                    case "0" -> exit = true;
                    default -> JOptionPane.showMessageDialog(null, "Opcion incorrecta");
                }
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

    //Metodo encargado de comprobar si hay registros en el fichero
    private boolean registry() throws XPathExpressionException {
        NodeList nodes = (NodeList) XPathFactory.newInstance().newXPath().evaluate("//alumne", document, XPathConstants.NODESET);
        if (nodes.getLength() == 0) {
            JOptionPane.showMessageDialog(null, "No hay registros");
            return false;
        } else return true;
    }

}
