/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.beyazperde;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author galip
 */
public class CommentFileParser {

    public void parseUserComments(String path) {
        try {
            File f = new File(path);
          
            String parent = f.getParent();
            System.out.println("f.getParent() = " + parent);
            FileWriter zeroFW = new FileWriter(parent + "/usercomments/usercomments_0.txt");
            FileWriter oneFW = new FileWriter(parent + "/usercomments/usercomments_1.txt");
            FileWriter twoFW = new FileWriter(parent + "/usercomments/usercomments_2.txt");
            FileWriter threeFW = new FileWriter(parent + "/usercomments/usercomments_3.txt");
            FileWriter fourFW = new FileWriter(parent + "/usercomments/usercomments_4.txt");
            FileWriter fiveFW = new FileWriter(parent + "/usercomments/usercomments_5.txt");

            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] vals = line.split(";");
                String filmNo = vals[0];
                String totalNoOfComments = vals[1];
                String commentId = vals[2];
                String stars = vals[3];
                String comment = vals[4];
                System.out.println(filmNo + " - " + stars);

                if (stars.startsWith("0")) {
                    zeroFW.write(line + "\n");
                    zeroFW.flush();
                }
                if (stars.startsWith("1")) {
                    oneFW.write(line + "\n");
                    oneFW.flush();
                }
                if (stars.startsWith("2")) {
                    twoFW.write(line + "\n");
                    twoFW.flush();
                }
                if (stars.startsWith("3")) {
                    threeFW.write(line + "\n");
                    threeFW.flush();
                }
                if (stars.startsWith("4")) {
                    fourFW.write(line + "\n");
                    fourFW.flush();
                }
                if (stars.startsWith("5")) {
                    fiveFW.write(line + "\n");
                    fiveFW.flush();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void parseEditorComments(String path) {
        try {
            File f = new File(path);
            String parent = f.getParent();
            System.out.println("f.getParent() = " + parent);
            FileWriter zeroFW = new FileWriter(parent + "/bpcomments/bpcomments_0.txt");
            FileWriter oneFW = new FileWriter(parent + "/bpcomments/bpcomments_1.txt");
            FileWriter twoFW = new FileWriter(parent + "/bpcomments/bpcomments_2.txt");
            FileWriter threeFW = new FileWriter(parent + "/bpcomments/bpcomments_3.txt");
            FileWriter fourFW = new FileWriter(parent + "/bpcomments/bpcomments_4.txt");
            FileWriter fiveFW = new FileWriter(parent + "/bpcomments/bpcomments_5.txt");

            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] vals = line.split(";");
                String filmNo = vals[0];
                String stars = vals[1];
                String comment = vals[2];
                System.out.println(filmNo + " - " + stars);

                if (stars.startsWith("0")) {
                    zeroFW.write(line + "\n");
                    zeroFW.flush();
                }
                if (stars.startsWith("1")) {
                    oneFW.write(line + "\n");
                    oneFW.flush();
                }
                if (stars.startsWith("2")) {
                    twoFW.write(line + "\n");
                    twoFW.flush();
                }
                if (stars.startsWith("3")) {
                    threeFW.write(line + "\n");
                    threeFW.flush();
                }
                if (stars.startsWith("4")) {
                    fourFW.write(line + "\n");
                    fourFW.flush();
                }
                if (stars.startsWith("5")) {
                    fiveFW.write(line + "\n");
                    fiveFW.flush();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
    public void cleanUserComments(String path) {
        try {
            File f = new File(path);

            String parent = f.getParent();
            System.out.println("f.getParent() = " + parent);
            FileWriter zeroFW = new FileWriter(parent + "/usercomments/clean/user_clean_0.txt");
            FileWriter oneFW = new FileWriter(parent + "/usercomments/clean/user_clean_1.txt");
            FileWriter twoFW = new FileWriter(parent + "/usercomments/clean/user_clean_2.txt");
            FileWriter threeFW = new FileWriter(parent + "/usercomments/clean/user_clean_3.txt");
            FileWriter fourFW = new FileWriter(parent + "/usercomments/clean/user_clean_4.txt");
            FileWriter fiveFW = new FileWriter(parent + "/usercomments/clean/user_clean_5.txt");

            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] vals = line.split(";");
//                String filmNo = vals[0];
//                String totalNoOfComments = vals[1];
//                String commentId = vals[2];
                String stars = vals[3];
                String comment = vals[4];
//                System.out.println(filmNo + " - " + stars);

                if (stars.startsWith("0")) {
                    zeroFW.write(comment + "\n");
                    zeroFW.flush();
                }
                if (stars.startsWith("1")) {
                    oneFW.write(comment + "\n");
                    oneFW.flush();
                }
                if (stars.startsWith("2")) {
                    twoFW.write(comment + "\n");
                    twoFW.flush();
                }
                if (stars.startsWith("3")) {
                    threeFW.write(comment + "\n");
                    threeFW.flush();
                }
                if (stars.startsWith("4")) {
                    fourFW.write(comment + "\n");
                    fourFW.flush();
                }
                if (stars.startsWith("5")) {
                    fiveFW.write(comment + "\n");
                    fiveFW.flush();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
    
    
 public void cleanEditorComments(String path) {
        try {
            File f = new File(path);
            String parent = f.getParent();
            System.out.println("f.getParent() = " + parent);
            FileWriter zeroFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_0.txt");
            FileWriter oneFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_1.txt");
            FileWriter twoFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_2.txt");
            FileWriter threeFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_3.txt");
            FileWriter fourFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_4.txt");
            FileWriter fiveFW = new FileWriter(parent + "/bpcomments/clean/bpcomments_5.txt");

            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] vals = line.split(";");
//                String filmNo = vals[0];
                String stars = vals[1];
                String comment = vals[2];
//                System.out.println(filmNo + " - " + stars);

                 if (stars.startsWith("0")) {
                    zeroFW.write(comment + "\n");
                    zeroFW.flush();
                }
                if (stars.startsWith("1")) {
                    oneFW.write(comment + "\n");
                    oneFW.flush();
                }
                if (stars.startsWith("2")) {
                    twoFW.write(comment + "\n");
                    twoFW.flush();
                }
                if (stars.startsWith("3")) {
                    threeFW.write(comment + "\n");
                    threeFW.flush();
                }
                if (stars.startsWith("4")) {
                    fourFW.write(comment + "\n");
                    fourFW.flush();
                }
                if (stars.startsWith("5")) {
                    fiveFW.write(comment + "\n");
                    fiveFW.flush();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
 

    public static void main(String[] args) {
        CommentFileParser c = new CommentFileParser();
//        c.parseUserComments("/Users/galip/dev/data/beyazperde/yorumlar/usercomments_tr.txt");
//        c.parseEditorComments("/Users/galip/dev/data/beyazperde/yorumlar/bpcomments_tr.txt");
        c.cleanUserComments("/Users/galip/dev/data/beyazperde/yorumlar/usercomments_tr.txt");
        c.cleanEditorComments("/Users/galip/dev/data/beyazperde/yorumlar/bpcomments_tr.txt");

    }
}
