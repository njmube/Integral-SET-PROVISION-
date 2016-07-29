/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author salvador
 */

public class Conexion {

    private Connection conexionMySQL;

    public Conexion()
    {
        conexionMySQL=null;
    }

    public String conectarMSQL (String usuario, String contrasena, String ip, String puerto, String catalogo)
    {
        if (conexionMySQL == null)
        {
            String urlConexionMySQL = "";
            if (catalogo != "")
                urlConexionMySQL = "jdbc:mysql://" + ip + ":" +	puerto + "/" + catalogo;
            else
                urlConexionMySQL = "jdbc:mysql://" + ip + ":" + puerto;
            if (usuario != "" & contrasena != "" & ip != "" & puerto != "")
            {
                try
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    DriverManager.setLoginTimeout(30);
                    conexionMySQL = DriverManager.getConnection(urlConexionMySQL, usuario, contrasena);
                }
                catch (ClassNotFoundException e)
                {
                    //Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return e.getMessage();
                }
                catch (SQLException e)
                {
                    //Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return e.getMessage();
                }
            }
            else
                return "Faltan datos";
        }
        return "";
    }

    public  void desconectarMSQL()
    {
        try
        {
            conexionMySQL.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList Consulta(String consulta)
    {
        ArrayList lista=new ArrayList();
        try
        {
            Statement st = conexionMySQL.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Integer numColumnas = rs.getMetaData().getColumnCount();

            while (rs.next())
            {
                ArrayList renglon=new ArrayList();
                for (int i = 1; i <= numColumnas; i++)
                {
                    renglon.add(rs.getObject(i));
                }
                lista.add(renglon);
            }
            st.close();
            rs.close();
            return lista;
        }
        catch (Exception e)
        {
            return new ArrayList();
        }
    }

    public int actualiza(String consulta)
    {
        try
        {
            Statement st = conexionMySQL.createStatement();
            int rs = st.executeUpdate(consulta);
            st.close();
            return rs;
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
