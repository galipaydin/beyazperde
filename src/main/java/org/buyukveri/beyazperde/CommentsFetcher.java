package org.buyukveri.beyazperde;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;
import org.buyukveri.common.PropertyLoader;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class CommentsFetcher {

    private FileWriter bpCommentsFW, userCommentsFW, bpCommentsLatinFW, userCommentsLatinFW, castFW, movieNamesFW;
    private Properties p;
    private String userCommentsFilePath, bpCommentsFilePath, castFilePath, movieNamesFilePath;
    private String userCommentsLatinFilePath, bpCommentsLatinFilePath;
    private String imgFolderPath;

    public CommentsFetcher() {
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

        bpCommentsFilePath = folderPath + "/bpcomments_tr.txt";
        userCommentsFilePath = folderPath + "/usercomments_tr.txt";
        castFilePath = folderPath + "/cast.txt";
        movieNamesFilePath = folderPath + "/movienames.txt";

        bpCommentsLatinFilePath = folderPath + "/bpcomments.txt";
        userCommentsLatinFilePath = folderPath + "/usercomments.txt";

        try {
            bpCommentsFW = new FileWriter(bpCommentsFilePath, true);
            userCommentsFW = new FileWriter(userCommentsFilePath, true);
            castFW = new FileWriter(castFilePath, true);
            movieNamesFW = new FileWriter(movieNamesFilePath, true);

            bpCommentsLatinFW = new FileWriter(bpCommentsLatinFilePath, true);
            userCommentsLatinFW = new FileWriter(userCommentsLatinFilePath, true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void parseIndexPages(String url, int startPage) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            String anasayfa = doc.html();
            int last = anasayfa.lastIndexOf("</span></li>");
            anasayfa = anasayfa.substring(0, last);
            last = anasayfa.lastIndexOf(">") + 1;
            anasayfa = anasayfa.substring(last);
            int lastpage = Integer.parseInt(anasayfa);
            parseIndexPage("http://www.beyazperde.com/filmler/elestiriler-beyazperde");

            for (int i = startPage; i <= lastpage; i++) {
                System.out.println("PAGE = " + i);
                String u = "http://www.beyazperde.com/filmler/elestiriler-beyazperde/?page=" + i;
                parseIndexPage(u);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CommentsFetcher.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            System.out.println("lastpage = " + lastpage);
        } catch (NumberFormatException ex) {
            System.out.println("parseIndexPages: " + ex.getMessage());
        }

    }

    public void parseIndexPage(String url) {
        try {
            Document filmList = WebPageDownloader.getPage(url);
            Elements hrefEls = filmList.getElementsByAttributeValueContaining("href", "/filmler/film-");

            for (Element e : hrefEls) {
                String filmAddress = e.attr("href");
                if (!filmAddress.endsWith("elestiriler-beyazperde/")) {

                    String userCommentsURL = "http://www.beyazperde.com" + filmAddress + "kullanici-elestirileri/";
                    String bpCommentsURL = "http://www.beyazperde.com" + filmAddress + "elestiriler-beyazperde/";
                    String filmNo = filmAddress.substring(filmAddress.indexOf("-") + 1);
                    filmNo = filmNo.replaceAll("/", "");

                    getTitleAndCast("http://www.beyazperde.com" + filmAddress + "oyuncular/", filmNo);

                    extractBeyazPerdeComments(bpCommentsURL, filmNo);
                    parseMoviePage(userCommentsURL, filmNo);
                }
            }

        } catch (Exception e) {
            System.out.println("parseIndexPage: " + e.getMessage());
        }
    }

    public void getTitleAndCast(String url, String filmNo) {
        try {
            System.out.println("url = " + url);
            Document doc = WebPageDownloader.getPage(url);
            //Film adı
            Element e = doc.getElementsByAttributeValueContaining("class", "titlebar-link").first();
            String filmAdi = e.text();
            System.out.println(filmAdi);
            this.movieNamesFW.write(filmAdi + "\n");
            this.movieNamesFW.flush();

            //Yönetmen ve Oyuncuları Çıkar
            Elements names = doc.getElementsByAttributeValueContaining("itemprop", "name");
            for (Element name : names) {
                String n = name.text();
                this.castFW.write(n + "\n");
                this.castFW.flush();
            }

//            Elements people = doc.getElementsByAttributeValueContaining("class", "card-person");
//            for (Element person : people) {
//                Elements hrefs = person.getElementsByAttribute("href");
//                for (Element href : hrefs) {
//                    String urlPart = href.attr("href").toString();  ///sanatcilar/sanatci-139654/
//                    String personUrl = "http://www.beyazperde.com" + urlPart;
//                    getPersonData(personUrl);
//                }
//            }

        } catch (Exception e) {
        }
    }

    public void parseMoviePage(String url, String filmNo) {
        try {
            Document doc = WebPageDownloader.getPage(url);
            //Internet bağlantısı yoksa locale kaydedilmiş dosyadan çalışmak için
            //Document doc = WebPageDownloader.getFile(url);
            Elements els = doc.getElementsByAttributeValueContaining("class", "pagination-item-holder");

//            String title = doc.title();
            System.out.println("\t" + filmNo);
            int lastPage = 0;
            
            if (els.size() > 0) {
                Element e = els.first();
                String text = e.toString();
                int i = text.lastIndexOf("</span>");
                text = text.substring(0, i);
                String last = text.substring(text.lastIndexOf(">") + 1, i);
                lastPage = Integer.parseInt(last);
            }
            for (int i = 1; i <= lastPage; i++) {
                String purl = "";
                if (i > 1) {
                    purl = url + "?page=" + i;
                    doc = WebPageDownloader.getPage(purl);
                } else {
                    purl = url;
                }
//                checkNewComments(purl, filmNo);
                extractUserComments(purl, filmNo);
            }

        } catch (Exception e) {
            System.out.println("parseMoviePage: " + e.getMessage());
        }
    }

    public void extractBeyazPerdeComments(String url, String filmNo) {
        try {
            Scanner s = new Scanner(new File(bpCommentsFilePath));
            boolean check = true;
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] arr = line.split(";");
                String filmNumber = arr[0];
                if (filmNumber.equals(filmNo)) {
                    check = false;
                    System.out.println("BP yorumu zaten kaydedilmiş " + filmNo);
                    break;
                }
            }
            if (check) {
                System.out.println("BP yorum : " + filmNo);
                Document doc = WebPageDownloader.getPage(url);

                Elements stars = doc.getElementsByAttributeValueContaining("itemprop", "ratingValue");
                Elements elm = doc.getElementsByAttributeValueContaining("class", "editorial-content cf");
                String yorum = elm.first().text();

                String ln = filmNo + ";" + stars.first().text() + ";" + yorum;
                this.bpCommentsFW.write(ln + "\n");
                this.bpCommentsFW.flush();

                yorum = cleanTurkishChars(yorum);
                ln = filmNo + ";" + stars.first().text() + ";" + yorum;
                this.bpCommentsLatinFW.write(ln);
                this.bpCommentsLatinFW.flush();
            }
        } catch (Exception e) {
            System.out.println("extractBeyazPerdeComments: " + e.getMessage());
        }
    }

    public void extractUserComments(String url, String filmNo) {
        try {
            System.out.println("*****  = " + url  );
            Document doc = WebPageDownloader.getPage(url);
            Elements ele = doc.getElementsByAttribute("data-totalreviews");
            String total = ele.attr("data-totalreviews");
            Scanner s = new Scanner(new File(userCommentsFilePath));
            boolean check = true;
            while (s.hasNext()) {
                if (check == false) {
                    break;
                }
                String line = s.nextLine();
                String[] arr = line.split(";");
                String filmNumber = arr[0];
                String commentCount = arr[1];
                if (filmNumber.equals(filmNo)) {
                    if (total.equals(commentCount)) {
                        check = false;
                        System.out.println(filmNo + ", yeni yorum yok, " + commentCount);
                    }
                }
            }
            if (check) {
                int lastPage = 0;
                Elements els = doc.getElementsByAttributeValueContaining("class", "pagination-item-holder");
                if (els.size() > 0) {
                    Element e = els.first();
                    String text = e.toString();
                    int i = text.lastIndexOf("</span>");
                    text = text.substring(0, i);
                    String last = text.substring(text.lastIndexOf(">") + 1, i);
                    lastPage = Integer.parseInt(last);
                }
                for (int i = 1; i <= lastPage; i++) {
                    String purl = "";
                    if (i > 1) {
                        purl = url + "?page=" + i;
                        doc = WebPageDownloader.getPage(purl);
                    } else {
                        purl = url;
                    }

                    System.out.println("\tKullanıcı Yorum - " + filmNo + ", sayfa: " + i);
                    Elements elements = doc.getElementsByAttributeValueContaining("class", "row item hred");
                    for (Element e : elements) {
                        Elements social = e.getElementsByAttribute("data-totalreviews");
                        String totalNoOfComments = social.attr("data-totalreviews");
                        String commentId = social.attr("data-opinionid");
                        Elements stars = e.getElementsByAttributeValueContaining("class", "stareval-note");
                        Elements cmnts = e.getElementsByAttributeValueContaining("itemprop", "description");
                        String yorum = cmnts.first().text();
                        String ln = filmNo + ";" + totalNoOfComments + ";" + commentId + ";"
                                + stars.first().text() + ";" + yorum;

                        this.userCommentsFW.write(ln + "\n");
                        this.userCommentsFW.flush();

                        yorum = cleanTurkishChars(yorum);

                        this.userCommentsLatinFW.write(ln + "\n");
                        this.userCommentsLatinFW.flush();
                    }
                }
            }
            s.close();
        } catch (Exception e) {
            System.out.println("extractUserComments: " + e.getMessage());
            System.out.println("url = " + url);
        }
    }

    public static String cleanTurkishChars(String in) {
        in = in.toLowerCase();
        in = in.replaceAll(";", ",").replaceAll("ç", "c").replaceAll("ğ", "g").replaceAll("ı", "i").replaceAll("ö", "o")
                .replaceAll("ü", "u").replaceAll("ş", "s").replaceAll("â", "a");
        return in;
    }

    public static void main(String[] args) {
        System.setProperty("http.agent", "Mozilla/5.0");
        CommentsFetcher c = new CommentsFetcher();
        c.parseIndexPages("http://www.beyazperde.com/filmler/elestiriler-beyazperde/", 1);
//        c.extractUserComments("http://www.beyazperde.com/filmler/film-239945/kullanici-elestirileri/", "239945");
    }
}
