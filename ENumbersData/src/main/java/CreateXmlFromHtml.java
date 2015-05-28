import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by y.belyaeva on 27.05.2015.
 */
public class CreateXmlFromHtml {


    private static final String pathToXml = "1.xml";

    public static void main(String[] args) {

        ArrayList<ENumber> list = getENumbersFromHtml("http://en.wikipedia.org/wiki/E_number#E400.E2.80.93E499_.28thickeners.2C_stabilizers.2C_emulsifiers.29");
        CreateXML();
        //TODO create file with base struct and fill it
    }

    private static ArrayList<ENumber> getENumbersFromHtml(String URL) {
        Document doc = null;
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements tables = doc.getElementsByTag("table"); //all tables
        for (Element table : tables) {
            Elements eNumbers = table.select("tr:matches(E[0-9]+"); //<tr> tags, full text of which matches regex
            for (Element eNumber : eNumbers) {
                String s = eNumber.text();
                System.out.println(s);
            }
        }
        return new ArrayList<ENumber>();
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
