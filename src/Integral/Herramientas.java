/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Usuario;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.Random;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import org.hibernate.Session;

/**
 *
 * @author I.S.C Salvador
 */
public class Herramientas {
    Usuario usr;
    public int menu;
    
    public Herramientas(Usuario u, int m)
    {
        usr=u;
        menu=m;
    }
    /**
     * Desbloquea una orden utilizada en una ventana
     */
    public void desbloqueaOrden()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.getTransaction().begin();
            usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
            session.createQuery("Update Orden Set ventana=null, usuarioByBloqueada=null where usuarioByBloqueada.idUsuario='"+usr.getIdUsuario()+"' and ventana="+menu).executeUpdate();
            /*Orden[] bloqueadas = (Orden[]) usr.getOrdensForBloqueada().toArray(new Orden[0]);
            for(int a=0; a<bloqueadas.length; a++)
            {
                String v=""+menu;
                if(bloqueadas[a].getVentana().compareTo(v)==0)
                {
                    usr.eliminaOrdensForBloqueada(bloqueadas[a]);
                    bloqueadas[a].setUsuarioByBloqueada(null);
                    bloqueadas[a].setVentana(null);
                    session.update(bloqueadas[a]);
                    session.update(usr);
                }
            }*/
            session.getTransaction().commit();
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close(); 
        }
    }
    
    /**
     * Revisa el estado de una Orden si esta bloqueada indica quien la tiene, si no la bloquea 
     * con el usuario que solicito la consulta y en la ventana actual
     * @param miPedido El pedido del cual se va a consultar el estado
     * @return Si esta bloqueado envia el usuario de lo contrario envia ''
     */
    public String estadoOrden(Orden orden)
    {
        String val="";
        Orden orden_act=orden;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
            int bloqueo=0;
            if(orden_act.getUsuarioByBloqueada()==null)
            {
                orden_act.setUsuarioByBloqueada(usr);
                orden_act.setVentana(""+menu);
                session.update(orden_act);
                session.getTransaction().commit();
                session.close();
                val = "*bloqueada ok*";
            }
            else
            {
                String ventana="";
                if(orden_act.getVentana()!=null)
                    ventana=orden_act.getVentana();
                if(orden_act.getUsuarioByBloqueada().getIdUsuario().compareTo(usr.getIdUsuario())==0 && ventana.compareTo(""+menu)==0)
                {
                    val = "";
                }
                else
                {
                    bloqueo=1;
                    val = orden_act.getUsuarioByBloqueada().getIdUsuario();
                }
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close(); 
        }
        return val;
    }
    
    /**
     * Revisa el estado de una session, si ya esta asignada cierra el sistema
     * @param sessionPrograma la session a verificar
     */
    public void session(String sessionPrograma)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr=(Usuario) session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getSession().compareTo(sessionPrograma)!=0)
            {
                javax.swing.JOptionPane.showMessageDialog(null, "has iniciado session en otra maquina!");
                System.exit(0);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close(); 
        }
    }
    
    /**
     * Consulta si una orden ya fue cerrada
     * @param orden orden a consultar
     * @return 'true' si la orden esta cerrada de lo contrario retorna 'false'.
     */
    public boolean isCerrada(String orden)
    {
        Boolean a=false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            Orden ord=(Orden) session.get(Orden.class, Integer.parseInt(orden));
            if(ord.getFechaCierre()!=null)
            {
                a = true;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
        return a;
    }
    
    // manejo de la session con los pedidos
    /**
     * Desbloquea un pedido utilizado en una ventana
     */
    public void desbloqueaPedido()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
            Pedido[] bloqueadas = (Pedido[]) usr.getPedidosForBloqueado().toArray(new Pedido[0]);
            for(int a=0; a<bloqueadas.length; a++)
            {
                String v=""+menu;
                if(bloqueadas[a].getVentana().compareTo(v)==0)
                {
                    usr.getPedidosForBloqueado().remove(bloqueadas[a]);
                    bloqueadas[a].setUsuarioByBloqueado(null);
                    bloqueadas[a].setVentana(null);
                    session.update(bloqueadas[a]);
                    session.update(usr);
                }
            }
            session.getTransaction().commit();
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                   session.close(); 
        }
    }
    
    /**
     * Revisa el estado de un pedido si esta bloqueado indica quien lo tiene si no lo bloquea 
     * con el usuario que solicito la consulta y en la ventana actual
     * @param miPedido El pedido del cual se va a consultar el estado
     * @return Si esta bloqueado envia el usuario de lo contrario envia ''
     */
    public String estadoPedido(Pedido miPedido)
    {
        String val="";
        Pedido ped=miPedido;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            ped=(Pedido)session.get(Pedido.class, miPedido.getIdPedido());
            if(ped.getUsuarioByBloqueado()==null)
            {
                ped.setUsuarioByBloqueado(usr);
                ped.setVentana(""+menu);
                session.update(ped);
                session.getTransaction().commit();
                session.close();
                val= "*bloqueada ok*";
            }
            else
            {
                String ventana="";
                if(ped.getVentana()!=null)
                    ventana=ped.getVentana();
                if(ped.getUsuarioByBloqueado().getIdUsuario().compareTo(usr.getIdUsuario())==0 && ventana.compareTo(""+menu)==0)
                {
                    val= "";
                }
                else
                {
                    val= ped.getUsuarioByBloqueado().getIdUsuario();
                }
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close(); 
        }
        return val;
    }
    
    /**
     * Envia un correo electronico
     * @param asunto asunto del correo
     * @param mensaje mensaje contenido en el correo
     * @param from direcciones de correo destinatario
     */
    public void enviaCorreo(String asunto, String mensaje, String from)
    {
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
            texto_mensaje.setContent(mensaje, "text/html");

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto_mensaje);

            // Se compone el correo, dando to, from, subject y el contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(envia));

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
        catch (Exception e){e.printStackTrace();}
    }
    
    public String randomString(int numero) {
    String vector = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int longitud = vector.length();
    String randomString = "";
     Random rnd = new Random();
    for (int i = 0; i < longitud; i++) {
        randomString += vector.charAt((int)(rnd.nextDouble() * numero + 1));
    }
    return randomString;
} 

}
