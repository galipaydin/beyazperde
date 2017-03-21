/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.beyazperde;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author galip
 */
public class PhotoFileProcessor {

    private PhotoDownloader pd;

    public PhotoFileProcessor() {
        pd = new PhotoDownloader();
    }

    public void readErrorFile(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            int count = 0;
            while (s.hasNext()) {
                System.out.println("Line: " + ++count);
                String line = s.nextLine();
                if (line.endsWith("/fotolar/")) {
                    pd.parsePhotoIndexPage(line);
                } else {
                    pd.parseLetterPage(line);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void downloadPics(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            int count = 0;
            while (s.hasNext()) {
                System.out.println("Line: " + ++count);
                String line = s.nextLine();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        PhotoFileProcessor pfp = new PhotoFileProcessor();
        System.out.println("Usage: PhotofileProcessor errors error.txt");
        String method = "";
        String path = "";
        if (args.length != 2) {
            System.out.println("Invalid no of parameters");
        } else {
            method = args[0];
            path = args[1];
            if (method.trim().equals("errors")) {
                pfp.readErrorFile(path);
            } else if (method.equals("download")) {
                pfp.downloadPics(path);
            }
        }
//        pfp.readErrorFile("/home/bulut/galip/beyazperde/error.txt");
//        pfp.downloadPics("/Users/galip/dev/data/beyazperde/names.txt");
    }
}
