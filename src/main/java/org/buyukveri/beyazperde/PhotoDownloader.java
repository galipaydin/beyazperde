/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.beyazperde;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.buyukveri.common.CommonTools;
import static org.buyukveri.common.CommonTools.cleanTurkishChars;
import org.buyukveri.common.PropertyLoader;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class PhotoDownloader {

    private Properties p;
    private String imgFolderPath;
    FileWriter fw, errorfw;

    public PhotoDownloader() {
        try {
            p = PropertyLoader.loadProperties("bp");
            String folderPath = p.getProperty("folderPath").trim()+"/beyazperde/";
            File f = new File(folderPath);
            if (!f.exists()) {
                f.mkdirs();
            }

            imgFolderPath = folderPath + "/img/";

            f = new File(imgFolderPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            fw = new FileWriter(this.imgFolderPath + "/links_"+ CommonTools.getTime() +".txt");
            errorfw = new FileWriter(this.imgFolderPath + "/error_"+ CommonTools.getTime() +".txt");
        } catch (IOException ex) {
            System.out.println("ex = " + ex.getMessage());
        }

    }

    public void downloadPhotos() {
        try {
            String[] a = {"0", "a", "b", "c", "d", "e", "f", "g", "h", "ı", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

            for (int i = 0; i < a.length; i++) {
                System.out.println("Letter: " + a[i]);
                String url = "http://www.beyazperde.com/sanatcilar/tum-sanatcilar-goster/alfabetik-" + a[i] + "/";
                parseLetterIndexPage(url);
            }

        } catch (Exception e) {
        }
    }

    public void parseLetterIndexPage(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Element navbar = doc.getElementsByAttributeValueContaining("class", "navbar").first();
            Element data = navbar.getElementsByAttributeValue("class", "navcenterdata").first();
            String val = data.text();
            String lastindex = val.substring(val.indexOf("/") + 1).trim();

            for (int i = 1; i <= Integer.parseInt(lastindex); i++) {
                System.out.println("page " + i + "/" + lastindex);
                String link = url + "?page=" + i;
                parseLetterPage(link);
            }

        } catch (Exception ex) {
            System.out.println("parseLetterIndexPage: " + ex.getMessage() + " " + url);
        }

    }

    public void parseLetterPage(String url) {
        try {

            Document doc = WebPageDownloader.getPage(url);
            Elements datablock = doc.getElementsByAttributeValueContaining("class", "datablock");
            for (Element e : datablock) {
            Thread.sleep(1000);

                Element test = e.getElementsByAttributeValueContaining("href", "/fotolar/").first();
                if (test != null) {
                    //System.out.println(test);
                    String href = test.attr("href");
                    parsePhotoIndexPage("http://www.beyazperde.com" + href);
                }
            }

        } catch (Exception ex) {
            try {
                System.out.println("parseLetterPage: " + url);
                errorfw.write(url + "\n");
                errorfw.flush();
            } catch (IOException ex1) {
            }
        }
    }

    public void parsePhotoIndexPage(String url) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            Element title = doc.getElementsByAttributeValue("id", "title").first();

            Element hr = title.getElementsByTag("span").first();
            String name = hr.text().trim().replaceAll(" ", "_").toLowerCase();
            name = cleanTurkishChars(name);
//            fw.write(name + "\n");
//            fw.flush();
            System.out.println(name);

//            if (name != null && !name.equals("")) {
//                String folder = this.imgFolderPath + name;
//                
//                File f = new File(folder);
//                if (!f.exists()) {
//                    f.mkdirs();
//                }
//            }
            int count = 0;
            Elements els = doc.getElementsByAttributeValueContaining("class", "list_photo");

            for (Element el : els) {
                Elements hrefs = el.getElementsByAttribute("href");

                for (Element href : hrefs) {
                    String urlPart = href.attr("href");  ///sanatcilar/sanatci-139654/
                    String imgUrl = "http://www.beyazperde.com" + urlPart;
                    Document doc1 = WebPageDownloader.getPage(imgUrl);
                    Elements ell = doc1.getElementsByAttributeValueContaining("class", "carousel_inner");
                    for (Element el1 : ell) {
                        Element img = el1.getElementsByAttribute("src").first();
                        String imgLink = img.attr("src");
                        fw.write(name + ";" + imgLink + "\n");
                        fw.flush();
//                        System.out.println(name + ";" + imgLink );
//                            saveImage(imgLink, folder + "/" + name + "_" + count++ + ".jpg");
//                            System.out.println(name + " " + count);
                    }
                }
            }

        } catch (Exception e) {
            try {
                System.out.println("parsePhotoIndexPage " + url);
                errorfw.write(url + "\n");
                errorfw.flush();
                // parsePhotoIndexPage(url);
            } catch (IOException ex) {
            }
        }
    }

    public void saveImage(String url, String filePath) {
        try {

            try (InputStream in = new java.net.URL(url).openStream();
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
                for (int b; (b = in.read()) != -1;) {
                    out.write(b);
                }
            }
        } catch (Exception e) {
            System.out.println("saveImage " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PhotoDownloader p = new PhotoDownloader();
        p.downloadPhotos();
//        p.parseLetterIndexPage("http://www.beyazperde.com/sanatcilar/tum-sanatcilar-goster/alfabetik-z/");
//        p.parseLetterPage("http://www.beyazperde.com/sanatcilar/tum-sanatcilar-goster/alfabetik-a/?page=33");
    }
}
