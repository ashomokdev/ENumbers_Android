import org.apache.commons.io.FileUtils;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.io.*;

/**
 * Created by y.belyaeva on 27.05.2015.
 */
public class CreateXmlFromHtml {


    private static final String pathToXml = "1.xml";

    public static void main(String[] args) {

        CreateXML();
        //TODO create file with base struct and fill it
    }

    private static void CreateXML() {
        try {
            String text = new Xembler(
                    new Directives()
                            .add("root")
                            .add("order")
                            .attr("id", "553")
                            .set("$140.00")
            ).xml();

            File xml = new File(pathToXml);
            if (!xml.exists()) {
                xml.createNewFile();
            }

            FileUtils.writeStringToFile(xml, text);

        } catch (ImpossibleModificationException ime) {
            ime.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
