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
        CreateXML(list);
        //TODO create file with base struct and fill it
    }

    private static ArrayList<ENumber> getENumbersFromHtml(String URL) {
        try {
            ArrayList<ENumber> result = new ArrayList<ENumber>();
            Document doc = Jsoup.connect(URL).get();
            Elements tables = doc.getElementsByTag("table"); //all tables
            for (Element table : tables) {
                Elements eNumbers = table.select("tr:matches(^E[0-9])"); //<tr> tags, full text of which matches the regex. Another way - tr:matches(E[0-9]{3,4}\s)
                for (Element eNumber : eNumbers) {
                    Elements info = eNumber.getElementsByTag("td");
                    if (info.size() != 4) {
                        throw new Exception("Wrond string was selected" + eNumber.text() + "info size = " + info.size());

                    }
                    String code = info.get(0).text();
                    String name = info.get(1).text();
                    String purpose = info.get(2).text();
                    String status = info.get(3).text().replaceAll("\\[[0-9]+\\]", ""); //regex for deletion links to the source, for example "Approved in the EU.[18]" will be replased with "Approved in the EU."
                    ENumber instance = new ENumber(code, name, purpose, status);
                    result.add(instance);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void CreateXML(ArrayList<ENumber> list) {
        try {
            String text = new Xembler(
                    new Directives()
                            .add("eNumber")
                            .add("code")
                            .set("$140.00").up()
                            .add("name")
                            .set("Me")

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
