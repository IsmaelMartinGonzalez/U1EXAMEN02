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
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Node root;


    public XmlManager() throws ParserConfigurationException {
        this.builderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = builderFactory.newDocumentBuilder();
    }

    public void createXML() {
        String op = "";
        if (new File("Alumnes.xml").exists()) {
            op = TextManager.fileExist();
        }else {
            op="No";
        }

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

    public void inputData() throws IOException, SAXException {
        if (loadFile())
            return;

        ArrayList<String> datas = TextManager.inputDataText();

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

        root.appendChild(subroot);
        subroot.appendChild(name);
        subroot.appendChild(course);
        subroot.appendChild(year);
        subroot.appendChild(school);

        try {
            saveFile();
        } catch (TransformerException e) {
            JOptionPane.showInputDialog("Ha ocurrido un error al guardar el alumno");
        }

    }

    public void modifyData() throws IOException, SAXException, TransformerException {
        if (loadFile())
            return;

        NodeList nodes = root.getChildNodes();

        String code = JOptionPane.showInputDialog("¿Codigo del alumno?");
        String data = TextManager.modifyDataText();


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
    }

    public void showAllXml() throws IOException, SAXException, XPathExpressionException, TransformerException {
        if (loadFile())
            return;

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

    public void inquiries() throws IOException, SAXException, XPathExpressionException {
        if (loadFile())
            return;
        boolean exit = false;
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate("//alumne", document, XPathConstants.NODESET);
        while (!exit) {
            String inquirie = TextManager.inquiriesText();
            switch (inquirie) {
                case "1":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        String nom = (String) xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING);
                        JOptionPane.showMessageDialog(null, nom);
                    }
                    break;
                case "2":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        String school = (String) xPath.evaluate("colegi", alumne, XPathConstants.STRING);
                        if (school.equals("Cide") || school.equals("cide"))
                            JOptionPane.showMessageDialog(null, xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                    }
                    break;
                case "3":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        if (alumne.getAttribute("codi_alumne").equals("3")) {
                            JOptionPane.showMessageDialog(null, xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                        }
                    }
                    break;
                case "4":
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element alumne = (Element) nodes.item(i);
                        int intYear = Integer.parseInt((String) xPath.evaluate("any_naixament", alumne, XPathConstants.STRING));
                        if (intYear <= 1990) {
                            JOptionPane.showMessageDialog(null, "Alumo nacido antes de 1990: " + xPath.evaluate("nom_alumne", alumne, XPathConstants.STRING));
                        }
                    }
                    break;
                case "0":
                    exit = true;
                    break;

            }

        }
    }

    public void deletData() throws IOException, SAXException, TransformerException {
        if (loadFile()) {
            return;
        }
        NodeList nodes = root.getChildNodes();
        String code = TextManager.deleteDataText();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getAttributes().getNamedItem("codi_alumne").getNodeValue().equals(code))
                root.removeChild(nodes.item(i));
        }
        saveFile();
    }

    private void saveFile() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult("Alumnes.xml");
        transformer.transform(source, result);
    }

    private boolean loadFile() throws IOException, SAXException {
        boolean error = false;
        if (new File("Alumnes.xml").exists()) {
            document = documentBuilder.parse("Alumnes.xml");
            root = document.getFirstChild();
        } else {
            JOptionPane.showMessageDialog(null, "Error! No hay un fichero XML", "Error!", JOptionPane.ERROR_MESSAGE);
            return error = true;
        }
        return error;

    }

    private void format() throws TransformerException, IOException, SAXException {
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

        document = documentBuilder.parse(is);
    }

}
