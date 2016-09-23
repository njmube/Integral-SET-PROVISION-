/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Hibernate.Util;

import java.io.File;
/*import java.io.FileNotFoundException;
import java.io.IOException;*/
import java.net.URL;
//import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.Document;*/

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author I.S.C Salvador
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static URL xmlFileURL;
        
    static {
        //xmlFileURL = HibernateUtil.class.getClassLoader().getResource("hibernate.cfg.xml"); 
        
        try {
            File f= new File("hibernate.cfg.xml");
            xmlFileURL =f.toURL();
            
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            sessionFactory = new Configuration().configure(xmlFileURL).buildSessionFactory();
        } catch (Exception ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory error en la creación." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void reConnect(){
        try{
    	sessionFactory.close();}catch(Exception e){}
        try {
            File f= new File("hibernate.cfg.xml");
            xmlFileURL =f.toURL();
            sessionFactory = new Configuration().configure(xmlFileURL).buildSessionFactory();
        } catch (Exception ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory error en la creación." + ex);
            throw new ExceptionInInitializerError(ex);
        }
 
    }
}
