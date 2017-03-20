/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.beyazperde;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
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
    
    public PhotoDownloader() {
        p = PropertyLoader.loadProperties("bp");
        String folderPath = p.getProperty("folderPath");
        File f = new File(folderPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        
        imgFolderPath = folderPath + "/img/";
        
        f = new File(imgFolderPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        
    }
    
    public void downloadPhotos(){
        try {
           String[] a = {"0","a","b","c","d","e","f","g","h","ı","k","l","m","n"
                   ,"o","p","q","r","s","t","u","v","w","x","y","z"};

            for (int i = 0; i < a.length; i++) {
                String url = "http://www.beyazperde.com/sanatcilar/tum-sanatcilar-goster/alfabetik-"+a[i]+"/";
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
            
        } catch (NumberFormatException ex) {
            System.out.println("parseIndexPages: " + ex.getMessage());
        }
        
    }
    
    public void parseLetterPage(String url) {
        try {
            
            Document doc = WebPageDownloader.getPage(url);
            Elements datablock = doc.getElementsByAttributeValueContaining("class", "datablock");
            for (Element e : datablock) {
                
                Element test = e.getElementsByAttributeValueContaining("href", "/fotolar/").first();
                if(test!=null){
                    String href = test.attr("href");
                    parsePhotoIndexPage("http://www.beyazperde.com" + href);
                }
            }
            
        } catch (Exception ex) {
            System.out.println("parseIndexPages: " + ex.getMessage());
        }
    }
    
    public void parsePhotoIndexPage(String url) {
        try {
            
            Document doc = WebPageDownloader.getPage(url);
            Element title = doc.getElementsByAttributeValue("id", "title").first();
            
            Element hr = title.getElementsByTag("span").first();
            String name = hr.text().trim().replaceAll(" ", "_").toLowerCase();
            name = CommentsFetcher.cleanTurkishChars(name);
//            System.out.print(name + ",");
            if (name != null && !name.equals("")) {
                String folder = this.imgFolderPath +  name;

                File f = new File(folder);
                if (!f.exists()) {
                    f.mkdirs();
                }
                
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
                            saveImage(imgLink, folder + "/" + name + "_" + count++ + ".jpg");
//                            System.out.println(name + " " + count);
                        }
                    }
                }
            }
        } catch (Exception e) {
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
//        p.parseLetterPage("http://www.beyazperde.com/sanatcilar/tum-sanatcilar-goster/alfabetik-z/?page=11");
    }
}
