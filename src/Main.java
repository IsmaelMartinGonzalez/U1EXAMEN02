import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            String nameFile = "Prueba";
            XmlManagerDomXpath manager = new XmlManagerDomXpath();
//            manager.createXML("Prueba");
//            manager.inputData("1","Juan","Eso","25/08/1999", "Cide");
//            manager.inputData("2","Juan2","Eso","25/08/1999", "Cide");
//            manager.inputData("3","Juan3","Eso","25/08/1999", "Cide");
            manager.showAllXml(nameFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
