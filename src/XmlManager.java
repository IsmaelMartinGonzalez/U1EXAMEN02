import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.crypto.dsig.Transform;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class XmlManager {
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Node root;


    public XmlManager() throws ParserConfigurationException {
        this.builderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = builderFactory.newDocumentBuilder();
    }

    public void createXML(String nameXML) {
        document = documentBuilder.newDocument();
        root = document.createElement("registre_" + nameXML.toLowerCase(Locale.ROOT));
        document.appendChild(root);
        try {
            saveFile(nameXML);
        } catch (TransformerException e) {
            JOptionPane.showInputDialog("Ha ocurrido un error al generar el XML");
        }
    }

    public void inputData(String nameFile, String code, String studentName, String cours, String yearAge, String schoolName) throws IOException, SAXException {
        loadFile(nameFile);

        Element subroot = document.createElement("alumne");
        subroot.setAttribute("codi_alumne", code);
        Element name = document.createElement("nom_alumne");
        name.setTextContent(studentName);
        Element course = document.createElement("curs");
        course.setTextContent(cours);
        Element year = document.createElement("any_naixament");
        year.setTextContent(yearAge);
        Element school = document.createElement("colegi");
        school.setTextContent(schoolName);

        root.appendChild(subroot);
        subroot.appendChild(name);
        subroot.appendChild(course);
        subroot.appendChild(year);
        subroot.appendChild(school);
        try {
            saveFile(nameFile);
        } catch (TransformerException e) {
            JOptionPane.showInputDialog("Ha ocurrido un error al guardar el alumno");
        }

    }

    public void modifyData(String namefile, String code) throws IOException, SAXException {
        loadFile(namefile);
        String data = JOptionPane.showInputDialog("¿Que dato deseas modificar?");
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeName());
        }

//        for (int i = 1; i <= nodes.getLength(); i++) {
//            if (nodes.item(i).getAttributes().getNamedItem("codi_alumne").getNodeValue().equals(code)) {
//                NodeList nodeList = nodes.item(i).getChildNodes();
//                for (int j = 0; j < nodeList.getLength(); j++) {
//                    if (nodeList.item(j).getLocalName().equals(data)) {
//                        String newData = JOptionPane.showInputDialog("Introduce el nuevo dato");
//                        nodeList.item(j).setTextContent(newData);
//                    }
//                }
//            }
//        }

    }

    public void showAllXml(String nameFile) throws IOException, SAXException, XPathExpressionException, TransformerException {
        loadFile(nameFile);
        format();


        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate("//alumne", document, XPathConstants.NODESET);
        String s = (String) xPath.evaluate("//*", document, XPathConstants.STRING);

        if (nodes.getLength() == 0) {
            JOptionPane.showMessageDialog(null, "No hay registros");
            return;
        }
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

    private void saveFile(String namefile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(namefile + ".xml");
        transformer.transform(source, result);
    }

    private void loadFile(String fileName) throws IOException, SAXException {
        document = documentBuilder.parse(fileName + ".xml");
        root = document.getFirstChild();
    }

    public void format() throws TransformerException, IOException, SAXException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(out);
        StreamSource source = new StreamSource(new ByteArrayInputStream(document.toString().getBytes()));
        transformer.transform(source, result);
        String f = out.toString();
        System.out.println(f);
    }
}