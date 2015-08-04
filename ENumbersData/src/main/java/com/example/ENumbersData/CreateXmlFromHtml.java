package com.example.ENumbersData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Created by y.belyaeva on 27.05.2015.
 */
public class CreateXmlFromHtml {

    private static Logger log = Logger.getLogger(CreateXmlFromHtml.class.getName());

    private static final String pathToXml = "res/raw/base.xml";
    private static final String base_url = "http://en.wikipedia.org/wiki/E_number#E400.E2.80.93E499_.28thickeners.2C_stabilizers.2C_emulsifiers.29";
    private static final String comments_url_1 = "http://www.additivealert.com.au/search.php?start=10&end=20&count=298&process=previous&flg=0";
    private static final String comments_url_2 = "http://nac.allergyforum.com/additives/colors100-181.htm";

    public static void main(String[] args) {
        ArrayList<ENumber> list = getENumbersFromHtml(base_url);
        CreateXML(list);
    }

    private static ArrayList<ENumber> getENumbersFromHtml(String url) {
        ArrayList<ENumber> data = createData(url);
        data = addComments_for_url_1(data, comments_url_1);
        data = addComments_for_url_2(data, comments_url_2);
        return data;
    }


    private static ArrayList<ENumber> addComments_for_url_1(ArrayList<ENumber> data, String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.select("table.bg1"); //all tables with class bg1
            for (Element table : tables) {
                Elements info = table.select("tr.tablebg1"); //all tr with tablebg1 class
                if (info.size() != 3) {
                    throw new Exception("Wrond string was selected" + table.text() + "info size = " + info.size());//not interesting line
                }
                String code = "E" + info.get(0).select("td").get(1).text();
                String comment = info.get(1).select("td").get(1).text();
                Predicate<ENumber> codeEquals = (v) -> (v.getCode().equals(code));
                Consumer<ENumber> addComment = (e) -> e.setComment(comment);
                data.stream()
                        .filter(codeEquals)
                        .forEach(addComment);
            }

            try {
                url = doc.select("td.textto").select("a[href]:matches(Next)").first().attr("abs:href"); //link to the next page
                addComments_for_url_1(data, url);
            } catch (NullPointerException e) {
                //next link not found. Stop working.
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }



    private static ArrayList<ENumber> addComments_for_url_2(ArrayList<ENumber> data, String url) {
        //TODO refactoring needed - get only one table
        log.info("addComments_for_url_2 called");
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table:matches(^E[0-9]{3,5}?)").first(); //all tables, full text of which matches the regex.



                Elements info = table.select("td"); //all td

                log.info("info size = "+ info.size() + "\n before while");
                int i = 0;
                while (i < info.size()) {

                    String tdText = info.get(i).text();

                    if (tdText.matches("^E[0-9]{3,5}")) {
                        String code = tdText;
                        String name = info.get(++i).text();
                        String comments = info.get(++i).text();

                        Predicate<ENumber> codeEquals = (v) -> (v.getCode().equals(code));
                        Consumer<ENumber> addComment = (e) -> e.setComment(comments);
                        data.stream()
                                .filter(codeEquals)
                                .forEach(addComment);

                        log.info(code + " added");
                    }
                    i++;
                }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static ArrayList<ENumber> createData(String url) {
        try {
            ArrayList<ENumber> result = new ArrayList<ENumber>();

            Document doc = Jsoup.connect(url).get();

            Elements tables = doc.getElementsByTag("table"); //all tables

            for (Element table : tables) {
                Elements eNumbers = table.select("tr:matches(^E[0-9]{3,5})"); //<tr> tags, full text of which matches the regex.

                for (Element eNumber : eNumbers) {
                    Elements info = eNumber.getElementsByTag("td");

                    String code = "";
                    String name = "";
                    String purpose = "";
                    String status = "";

                    try {
                        code = info.get(0).text().replaceAll("\\[[^>]*\\]", ""); //regex for deletion links - [any character that isn't >], for example "Approved in the EU.[18]" will be replased with "Approved in the EU."
                        name = info.get(1).text().replaceAll("\\[[^>]*\\]", ""); //regex for deletion links - [any character that isn't >], for example "Approved in the EU.[18]" will be replased with "Approved in the EU."
                        status = info.get(3).text().replaceAll("\\[[^>]*\\]", ""); //regex for deletion links - [any character that isn't >], for example "Approved in the EU.[18]" will be replased with "Approved in the EU."

                        String add_text = "";
                        if (code.matches("^E1[0-9][0-9][a-z]?")) //if E100-E199 than Color
                        {
                            add_text = "Color ";
                        }
                        purpose = add_text + info.get(2).text().replaceAll("\\[[^>]*\\]", ""); //regex for deletion links - [any character that isn't >], for example "Approved in the EU.[18]" will be replased with "Approved in the EU."

                    } catch (IndexOutOfBoundsException e) {
                        log.info(
                                "createData Method, " +
                                        "Issue with " +
                                        code);
                    }
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

    private static void CreateXML(ArrayList<ENumber> data) {
        try {
            File xml = new File(pathToXml);
            if (!xml.exists()) {
                xml.createNewFile();
            }
            org.w3c.dom.Document dom = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            dom.appendChild(dom.createElement("root"));

            for (ENumber elem : data) {
                Directives directives = new Directives()
                        .xpath("/root")
                        .addIf("eNumbers")
                        .add("eNumber");

                PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(data.get(0).getClass()); //without Spring: Introspector.getBeanInfo(data.get(0).getClass()).getPropertyDescriptors())
                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (pd.getName().equals("class")) {
                        //excludes  <class>class com.example.ENumbersData.ENumber</class> from base.xml
                    } else {
                        Method getter = pd.getReadMethod();
                        directives.add(pd
                                .getName())
                                .set(String.valueOf(getter.invoke(elem)))
                                .up();
                    }
                }
                new Xembler(directives).apply(dom);
            }

//before reflection was added.
//            for (ENumber elem : data) {
//                new Xembler(
//                        new Directives()
//                                .xpath("/root")
//                                .addIf("eNumbers")
//                                .add("eNumber")
//                                .add("code")
//                                .set(elem.getCode()).up()
//                                .add("name")
//                                .set(elem.getName()).up()
//                                .add("purpose")
//                                .set(elem.getPurpose()).up()
//                                .add("status")
//                                .set(elem.getStatus()).up()
//                ).apply(dom);
//            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(pathToXml));
            Source input = new DOMSource(dom);
            transformer.transform(input, output);
        } catch (ImpossibleModificationException ime) {
            ime.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
