/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author galip
 */
public class CastPhotoDownloader {

    public void castPhotoDownload(String folderPath, String linkFilePath) {
        try {
            File f = new File(folderPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            Scanner s = new Scanner(new File(linkFilePath));
            int count = 0;
            while (s.hasNext()) {
                String line = s.nextLine();
                if (line.contains(";")) {
                    String name = line.substring(0, line.indexOf(";"));
                    String url = line.substring(line.indexOf(";") + 1);

                    String folder = folderPath + "/" + name;
                    File ff = new File(folder);
                    if (!ff.exists()) {
                        ff.mkdirs();
                    }
                    String fileName = url.substring(url.lastIndexOf("/") + 1).trim().toLowerCase();
                    File img = new File(folder + "/" + fileName);
                    if (!img.exists()) {
                        saveImage(url, folder + "/" + fileName);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
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
        CastPhotoDownloader c = new CastPhotoDownloader();
        c.castPhotoDownload("/Users/galip/dev/data/beyazperde/sinemalar/img", "/Users/galip/dev/data/beyazperde/sinemalar/imglinks.txt");
    
    }

}
