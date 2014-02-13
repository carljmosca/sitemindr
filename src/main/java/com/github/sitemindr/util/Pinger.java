/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sitemindr.util;

import com.github.sitemindr.entity.Site;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author moscac
 */
public class Pinger {

    private final static int MAX_TIMEOUT = 120;

    public static PingResult doHttpGet(String address, int timeoutInSeconds) {
        PingResult result = new PingResult();
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        StringBuilder data = new StringBuilder();
        try {
            url = new URL(address.toLowerCase().startsWith("http") ? address : "http://" + address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * (timeoutInSeconds > 0 && timeoutInSeconds < MAX_TIMEOUT ? timeoutInSeconds : MAX_TIMEOUT));
            final long startTime = System.currentTimeMillis();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final long endTime = System.currentTimeMillis();
            while ((line = rd.readLine()) != null) {
                data.append(line);
            }
            result.setOk(true);
            result.setMessage("HTTP GET " + address +  " read " + data.length() + " bytes in " + (endTime - startTime) + "ms");
            rd.close();
            
        } catch (IOException e) {
            result.setMessage(e.getMessage());
        }
        return result;
    }
    
    private void saveSite(String address) {
        Site siteEntity = new Site();
        siteEntity.setFqdn(address);
        
    }
}
