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
            session.beginTransaction().begin();
            usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
            Orden[] bloqueadas = (Orden[]) usr.getOrdensForBloqueada().toArray(new Orden[0]);
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
            if(usr.getSession().compareTo(sessionPrograma)==0)
            {
            }
            else
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
}
