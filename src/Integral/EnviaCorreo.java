/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author I.S.C.Salvador
 */
public class EnviaCorreo implements Runnable {
    Thread t;
    String asunto, mensaje, from, responder;
    public EnviaCorreo(String asunto, String mensaje, String from, String responder) 
    {
        this.asunto=asunto;
        this.mensaje=mensaje;
        this.from=from;
        this.responder=responder;
        t=new Thread(this,"Conexion");
        t.start();
    }
        @Override
        public void run() {
            String smtp="";
            boolean ttl=false;
            String puerto="";
            String envia="";
            String clave="";
            //String from="";
            String cc="";
            String texto = null;

            try
            {
                FileReader f = new FileReader("correo.ml");
                BufferedReader b = new BufferedReader(f);
                int renglon=0;
                while((texto = b.readLine())!=null)
                {
                    switch(renglon)
                    {
                        case 1://smtp
                            smtp=texto.trim();
                            break;
                        case 2://ttl
                            if(texto.compareToIgnoreCase("true")==0)
                                ttl=true;
                            else
                                ttl=false;
                            break;
                        case 3://puerto
                            puerto=texto.trim();
                            break;
                        case 4://cuenta
                            envia=texto.trim();
                            break;
                        case 5://contraseña
                            clave=texto.trim();
                            break;
                    }
                    renglon+=1;
                }
                b.close();
            }catch(Exception e){e.printStackTrace();}

            try
            {
                // se obtiene el objeto Session.
                Properties props = new Properties();
                props.put("mail.smtp.host", smtp);
                props.setProperty("mail.smtp.starttls.enable", ""+ttl);
                props.setProperty("mail.smtp.port", puerto);
                props.setProperty("mail.smtp.user", envia);
                props.setProperty("mail.smtp.auth", "true");

                javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
                // session.setDebug(true);

                // Se compone la parte del texto
                BodyPart texto_mensaje = new MimeBodyPart();
                texto_mensaje.setText(mensaje);

                // Una MultiParte para agrupar texto e imagen.
                MimeMultipart multiParte = new MimeMultipart();
                multiParte.addBodyPart(texto_mensaje);

                // Se compone el correo, dando to, from, subject y el contenido.
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(envia));

                if(responder!= null)
                    message.setReplyTo(new javax.mail.Address[]{ new javax.mail.internet.InternetAddress(responder)});

                String [] direcciones=from.split(";");
                for(int x=0; x<direcciones.length; x++)
                {
                    if(direcciones[x].compareTo("")!=0)
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(direcciones[x].replace(" ","")));
                }

                String [] dirCC=cc.split(";");
                for(int y=0; y<dirCC.length; y++)
                {
                    if(dirCC[y].compareTo("")!=0)
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(dirCC[y].replace(" ","")));
                }

                message.setSubject(asunto);
                message.setContent(multiParte);

                Transport t = session.getTransport("smtp");
                t.connect(envia, clave);
                t.sendMessage(message, message.getAllRecipients());
                t.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            t.interrupt();
        }
}
