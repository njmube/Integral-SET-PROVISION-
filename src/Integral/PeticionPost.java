/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

/**
 *
 * @author Sistemas
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PeticionPost {
    private URL url;
    String data;

    public PeticionPost (String url) throws MalformedURLException{
        this.url = new URL(url);
        data="";
    }

    public void add (String propiedad, String valor) throws UnsupportedEncodingException{
    //codificamos cada uno de los valores
    if (data.length()>0)
        data+= "&"+ URLEncoder.encode(propiedad, "UTF-8")+ "=" +URLEncoder.encode(valor, "UTF-8");
    else
        data+= URLEncoder.encode(propiedad, "UTF-8")+ "=" +URLEncoder.encode(valor, "UTF-8");
    }

    public String getRespueta() throws IOException {
        String respuesta = "";
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String linea;
        while ((linea = rd.readLine()) != null) {
            respuesta+= linea;
        }
        return respuesta;
    }
}
