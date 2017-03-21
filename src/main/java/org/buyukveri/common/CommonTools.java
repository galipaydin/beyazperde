/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author galip
 */
public class CommonTools {

    public static String getTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hhmmss");
            String date = sdf.format(new Date());
            return date;
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) {
        CommonTools.getTime();
    }
}
