/*
  Author: Ismael
  Desc: Clase principal que gestiona la aplicacion.
 */

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

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
                        break;
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

        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException | TransformerException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
