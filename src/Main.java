import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

//TODO Comprobar los errores que puedan surgir al introducir ID que no existen, el tipo de datos que no existen, en los menus elejir una opción que existe, que la feha sea un numero no negativo,
public class Main {
    public static void main(String[] args) {
        try {
            boolean exit = false;
            XmlManager manager = new XmlManager();

            while (!exit) {
                String op = TextManager.mainMenu();
                switch (op) {
                    case "1":
                        manager.createXML();
                        break;
                    case "2":
                        manager.inputData();
                        break;
                    case "3":
                        manager.showAllXml();
                        break;
                    case "4":
                        manager.modifyData();
                    case "5":
                        manager.inquiries();
                        break;
                    case "6":
                        manager.deletData();
                        break;
                    case "0":
                        exit = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "La opcion elegida no es correcta", "Error!", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
    }
}
