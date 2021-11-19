import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            String nameFile = "Prueba";
            XmlManager manager = new XmlManager();
            String op = JOptionPane.showInputDialog("Op?");
            switch (op) {
                case "1":
                    manager.createXML("Prueba");
                    break;
                case "2":
                    manager.inputData(nameFile,JOptionPane.showInputDialog("Code?"), "Juan", "Eso", "25/08/1999", "Cide");
                    break;
                case "3":
                    manager.showAllXml(nameFile);
                    break;
                case "4":
                    manager.modifyData(nameFile, JOptionPane.showInputDialog("Code?"));
                    break;

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
        }
    }
}
