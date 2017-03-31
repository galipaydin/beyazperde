/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.sinemalar.com;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.buyukveri.common.CommonTools;
import org.buyukveri.common.PropertyLoader;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class ExtractSinemalarData {

    private Properties p;
    FileWriter fw, errorfw;

    public ExtractSinemalarData() {
        try {
            p = PropertyLoader.loadProperties("bp");
            String folderPath = p.getProperty("folderPath").trim() + "/sinemalar/data/";
            File f = new File(folderPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String path = folderPath + "/films_" + CommonTools.getTime() + ".txt";
            System.out.println(path);
            fw = new FileWriter(path);
        } catch (IOException ex) {
            System.out.println("ex = " + ex.getMessage());
        }

    }

    public void indexPage() {

//                226916 250273
        String url = "http://www.sinemalar.com/film/";
        try {
            //http://www.sinemalar.com/filmler#page-17282
            for (int i = 72; i <= 75; i++) {
//                if (i != 226946) {
                url = url + i;

                Document doc = WebPageDownloader.getPage(url);
                if (doc != null) {
                    Element hgroup = doc.getElementsByTag("hgroup").first();
                    String filmName = hgroup.getElementsByAttributeValue("itemprop", "name").text();
                    String country = "";
                    Element e = doc.getElementsByAttributeValueContaining("class", "mh335").first();
                    Elements as = e.getElementsByTag("p");
                    for (Element aa : as) {
                        String atext = aa.toString();
                        if (atext.contains("Yapımı")) {
                            Elements a = aa.getElementsByTag("a");
                            if (a.size() > 0) {
                                Element b = a.last();
                                country = b.text();
                                String line = "*;" + i + ";" + filmName + ";" + country;
                                System.out.println(line);
                                fw.write(line + "\n");
                                fw.flush();
                            }
                        } else if (atext.contains("Oyuncular")) {
                            Elements links = aa.getElementsByTag("a");
                            if (links.size() > 0) {
                                for (Element link : links) {
                                    String name = link.getElementsByAttributeValue("itemprop", "name").first().text();
                                    String href = link.attr("href");
                                    String line = "&;" + i + ";" + name + ";" + href.replaceFirst("//", "");
//                                    System.out.println(line);
                                    fw.write(line + "\n");
                                    fw.flush();
                                }
                            }
                        }
                    }
                }
//                    System.out.println(as.get(2).text());

//                    Element ee = e.getElementsByTag("p").first();
//                    Elements eee = ee.getElementsByTag("a");
//                    String a=        eee.last().text();
                url = "http://www.sinemalar.com/film/";
            }
//            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + url);
            System.out.println(ex.getMessage());
        }

    }

    public static void main(String[] args) {
        ExtractSinemalarData a = new ExtractSinemalarData();
        a.indexPage();
    }
}
