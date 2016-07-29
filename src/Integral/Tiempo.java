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
import java.util.Timer;
import java.util.TimerTask;
import org.hibernate.Session;

/**
 *
 * @author salvador
 */
public class Tiempo  {

    private Timer timer = new Timer(); 
    private int segundos=0;
    private Usuario usr;
    private String sessionPrograma;

    //Clase interna que funciona como contador
    class Contador extends TimerTask 
    {
        public void run() 
        {
            segundos++;
            if(segundos>600)
            {
                eliminaBloqueos();
                System.exit(0);
            }
        }
    }
    //Crea un timer, inicia segundos a 0 y comienza a contar
    public void Contar(Usuario usr, String sessionPrograma)
    {
        this.usr=usr;
        this.sessionPrograma=sessionPrograma;
        this.segundos=0;
        timer = new Timer();
        timer.schedule(new Contador(), 0, 1000);
    }
    public void Reiniciar()
    {
        this.segundos=0;
    }
    //Detiene el contador
    public void Detener() {
        timer.cancel();
    }
    public void purgar()
    {
        timer.purge();
    }
    //Metodo que retorna los segundos transcurridos
    public int getSegundos()
    {
        return this.segundos;
    }
    
     private boolean eliminaBloqueos()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
            session.beginTransaction().begin();
            Orden[] bloqueadas = (Orden[]) usr.getOrdensForBloqueada().toArray(new Orden[0]);
            for(int a=0; a<bloqueadas.length; a++)
            {
                usr.eliminaOrdensForBloqueada(bloqueadas[a]);
                bloqueadas[a].setUsuarioByBloqueada(null);
                bloqueadas[a].setVentana(null);
                session.update(bloqueadas[a]);
            }
            Pedido[] bloqueados = (Pedido[]) usr.getPedidosForBloqueado().toArray(new Pedido[0]);
            for(int b=0; b<bloqueados.length; b++)
            {
                System.out.println(bloqueados[b].getIdPedido());
                usr.getPedidosForBloqueado().remove(bloqueados[b]);
                bloqueados[b].setUsuarioByBloqueado(null);
                bloqueados[b].setVentana(null);
                session.update(bloqueados[b]);
            }
            usr.setSession(sessionPrograma);
            session.update(usr);
            session.getTransaction().commit();
        }catch(Exception e)
        {
            session.getTransaction().rollback();
            return false;
        }
        finally 
        {
            if(session.isOpen())
                session.close(); 
            return true;
        }
    }
}
