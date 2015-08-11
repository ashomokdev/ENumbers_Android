package com.example.enumberdata;

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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Iuliia on 08.08.2015.
 */
public class URLProcessorImpl implements  URLProcessor {
    private  Logger log = Logger.getLogger(URLProcessorImpl.class.getName());

    private static final String pathToXml = "res/raw/base.xml";
    private List<String> urlList;
    private ENumbersService enumberService;

    public URLProcessorImpl(List<String> urlList) {
        this.urlList = urlList;
    }

    //TODO rewrite
    public void init() {
        ArrayList<ENumber> data = createData(urlList.get(0));
        addAdditionalInfoForURL1(data, urlList.get(1));
        addAdditionalInfoForURL2or3(data, urlList.get(3));
        addAdditionalInfoForURL2or3(data, urlList.get(2));

        enumberService = new ENumbersServiceImpl(data);
        enumberService.reformatAdditionalInfo();

        if (!data.isEmpty()) {
            createXML(data);
        }

    }

    private  ArrayList<ENumber> addAdditionalInfoForURL2or3(ArrayList<ENumber> data, String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            //getting all pages
            Elements a_hrefs = doc.select("a[href]:matches([0-9]+)"); // a with href, full text of which matches the regex Any number

            Collection<String> a_hrefs_noDups = new HashSet<String>();
            a_hrefs_noDups.add(url);

            for (Element a_href : a_hrefs) {
                a_hrefs_noDups.add(a_href.attr("abs:href")); //If you want to get an absolute URL, there is a attribute key prefix abs:
            }

            for (String a_href : a_hrefs_noDups) {

                doc = Jsoup.connect(a_href).get();

                Element table = doc.select("table:matches(E[0-9]{3,5})").first(); //table, full text of which matches the regex. More info http://jsoup.org/cookbook/extracting-data/selector-syntax

                Elements info = table.select("td"); //all td

                int i = 0;

                while (i < info.size()) {

                    String tdText = info.get(i).text();

                    if (tdText.matches(".{0,5}E[0-9]{3,5}")) {

                        String codeText = tdText;

                        Matcher matcher = Pattern.compile("E[0-9]{3,5}").
                                matcher(codeText);

                        if (matcher.find()) {
                            try {
                                String code = matcher.group(0);

                                String name = info.get(++i).text();
                                String comment = info.get(++i).text();

                                Predicate<ENumber> codeEquals = (v) -> (v.getCode().equals(code));
                                Consumer<ENumber> addComment = (e) -> e.AddAdditionalInfo(comment);
                                data.stream()
                                        .filter(codeEquals)
                                        .forEach(addComment);
                            } catch
                                    (Exception e) {
                                log.info(e.getMessage());
                            }
                        }
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    private  ArrayList<ENumber> addAdditionalInfoForURL1(ArrayList<ENumber> data, String url) {
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
                Consumer<ENumber> addComment = (e) ->  e.AddAdditionalInfo(comment);
                data.stream()
                        .filter(codeEquals)
                        .forEach(addComment);
            }

            try {
                url = doc.select("td.textto").select("a[href]:matches(Next)").first().attr("abs:href"); //link to the next page, If you want to get an absolute URL, there is a attribute key prefix abs:
                addAdditionalInfoForURL1(data, url);
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


//    private  ArrayList<ENumber> addComments_for_url_2(ArrayList<ENumber> data, String url) {
//        try {
//            Document doc = Jsoup.connect(url).get();
//
//            //getting all pages
//            Elements a_hrefs = doc.select("a[href]:matches([0-9]+)"); // a with href, full text of which matches the regex Any number
//
//            Collection<String> a_hrefs_noDups = new HashSet<String>();
//
//            for (Element a_href : a_hrefs) {
//                a_hrefs_noDups.add(a_href.attr("abs:href")); //If you want to get an absolute URL, there is a attribute key prefix abs:
//            }
//
//            for (String a_href : a_hrefs_noDups) {
//
//                doc = Jsoup.connect(a_href).get();
//
//                Element table = doc.select("table:matches(E[0-9]{3,5})").first(); //table, full text of which matches the regex. More info http://jsoup.org/cookbook/extracting-data/selector-syntax
//
//                Elements info = table.select("td"); //all td
//
//                int i = 0;
//
//                while (i < info.size()) {
//
//                    String tdText = info.get(i).text();
//
//                    if (tdText.matches("^E[0-9]{3,5}")) {
//                        String code = tdText;
//                        String name = info.get(++i).text();
//                        String comments = info.get(++i).text();
//
//                        Predicate<ENumber> codeEquals = (v) -> (v.getCode().equals(code));
//                        Consumer<ENumber> addComment = (e) -> e.addAdditionalInfo(comments);
//                        data.stream()
//                                .filter(codeEquals)
//                                .forEach(addComment);
//                    }
//                    i++;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    private  ArrayList<ENumber> createData(String url) {
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

    private void createXML(ArrayList<ENumber> data) {
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
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //formatting
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

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
