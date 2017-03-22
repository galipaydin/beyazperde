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
public class Cast {

    private FileWriter castFW, movieNamesFW, errorFW, imgLinksFW;
    private Properties p;
    private String castFilePath, movieNamesFilePath, errorFilePath, imgLinksFilePath;
    private String imgFolderPath;

    public Cast() {
        p = PropertyLoader.loadProperties("bp");
        String folderPath = p.getProperty("folderPath").trim() + "/sinemalar/";
        System.out.println("folderPath = " + folderPath);
        File f = new File(folderPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        imgFolderPath = folderPath + "/img/";

        f = new File(imgFolderPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        imgLinksFilePath = folderPath + "/imglinks.txt";
        castFilePath = folderPath + "/cast.txt";
        movieNamesFilePath = folderPath + "/movienames.txt";
        errorFilePath = folderPath + "/error_" + CommonTools.getTime() + ".txt";
        try {
            castFW = new FileWriter(castFilePath);
            movieNamesFW = new FileWriter(movieNamesFilePath);
            errorFW = new FileWriter(errorFilePath);
            imgLinksFW = new FileWriter(imgLinksFilePath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getCastLinks() {
        try {
            for (int i = 0; i < 94; i++) {
                System.out.println("PAGE: " + i);
                String url = "http://www.sinemalar.com/en-iyi-filmler/" + i;
                Document doc = WebPageDownloader.getPage(url);
                Elements els = doc.getElementsByAttributeValue("class", "bestof-detail");
                for (Element el : els) {
                    String filmName = el.getElementsByTag("a").first().attr("title");
                    movieNamesFW.write(filmName + "\n");
                    movieNamesFW.flush();

                    Element div = el.getElementsByAttributeValue("class", "viyon-filter").first();
                    Elements as = div.getElementsByTag("a");
                    for (Element a : as) {
                        String link = a.getElementsByAttribute("href").attr("href");
                        String name = a.getElementsByAttribute("href").attr("title");
                        System.out.println(name);

                        castFW.write(name + "\n");
                        castFW.flush();

                        name = name.trim().replaceAll(" ", "_").toLowerCase();
                        name = CommonTools.cleanTurkishChars(name);
                        getPersonPhotoLinks("http:" + link, name);
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("getCastLinks: " + ex.getMessage());
        }
    }

    public void getPersonPhotoLinks(String url, String name) {
        try {
            String link = url.replace("sanatci", "galeri");
            Document doc = WebPageDownloader.getPage(link);
            Element elm
                    = doc.getElementById("personGalleryImages");

            if (elm != null) {
                Elements el = elm.getElementsByTag("a");

                for (Element e : el) {
                    String imagePageLink = e.attr("href");
                    Document doc1 = WebPageDownloader.getPage(imagePageLink);

                    Elements imgDivs = doc1.getElementsByAttributeValue("class", "gallery-slide");
                    Element imgDiv = imgDivs.first();
                    String imgLink = imgDiv.getElementsByTag("img").attr("src");
                    imgLinksFW.write(name + ";" + imgLink + "\n");
                    imgLinksFW.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("getPersonPhotoLinks:" + url);
            try {
                errorFW.write(url + "\n");
                errorFW.flush();
            } catch (IOException ex) {
            }
        }
    }

    public static void main(String[] args) {
        Cast c = new Cast();
        c.getCastLinks();
//        c.getPersonPhotoLinks("http://www.sinemalar.com/galeri/25501/orlando-bloom");
    }
}
