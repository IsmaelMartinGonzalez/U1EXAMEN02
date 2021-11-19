//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//public class Activity11DA {
//
//    private DocumentBuilderFactory factory;
//    private DocumentBuilder builder;
//    private Document document;
//    private XPath xpath;
//
//
//    final static String jugadores = "/jugador[@nombre=";
//    final static String equipos = "//equipo";
//
//    public Activity11DA() throws SAXException, IOException, ParserConfigurationException {
//        factory = DocumentBuilderFactory.newInstance();
//        builder = factory.newDocumentBuilder();
//        document = builder.parse(new File("futbol.xml"));
//
//        xpath = XPathFactory.newInstance().newXPath();
//
//    }
//
//
//    public void getAttributes(String query, XPath xpath, Document document, String attributeName) throws XPathExpressionException {
//
//        NodeList nodos = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
//
//        if (nodos.getLength() == 0) {
//            System.out.println("NO SE HAN ENCONTRADO REGISTROS");
//        }
//
//        for (int i = 0; i < nodos.getLength(); i++) {
//            System.out.println("----------------------------");
//            System.out.println("\n" + nodos.item(i).getAttributes().getNamedItem(attributeName) + "\n");
//            System.out.println("----------------------------");
//        }
//    }
//
//    public void menu() throws XPathExpressionException {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Que desea buscar?\n-jugadores\n-equipos\n-ligas\n-selecciones\n-------");
//        String opcion = sc.nextLine();
//        switch (opcion) {
//            case "jugadores":
//                System.out.println("Que jugador buscas: ");
//                String jugador = sc.nextLine();
//                getAttributes("//jugador[@nombre='" + jugador + "']", xpath, document, "nombre");
//                menu();
//                break;
//            case "equipos":
//                System.out.println("Que equipo buscas: ");
//                String equipo = sc.nextLine();
//                getAttributes("//equipo[@nombre='" + equipo + "']", xpath, document, "nombre");
//                menu();
//                break;
//            case "ligas":
//                System.out.println("Que liga buscas: ");
//                String liga = sc.nextLine();
//                getAttributes("//liga[@nombre='" + liga + "']", xpath, document, "nombre");
//                menu();
//                break;
//            case "selecciones":
//                System.out.println("Que seleccion buscas: ");
//                String seleccion = sc.nextLine();
//                getAttributes("//seleccion[@nombre='" + seleccion + "']", xpath, document, "nombre");
//                menu();
//                break;
//            case "salir":
//                return;
//            default:
//                menu();
//        }
//    }
//
//
//    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
//        Activity11DA ac = new Activity11DA();
//        ac.menu();
//    }
//
//}