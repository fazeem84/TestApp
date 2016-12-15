/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp.hibernate.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import testapp.hibernate.pojo.DeviceInput;
import testapp.hibernate.pojo.DeviceSettings;

/**
 *
 * @author fazeem
 */
public class DAO {
    private static SessionFactory sessionFactory=null;
    public static SessionFactory getCreateSessionFactory(){
         if(DAO.sessionFactory==null){
            
        Configuration configuration = new Configuration().configure();
        Properties properties=configuration.getProperties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(properties);
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
         DAO.sessionFactory=sessionFactory;
        }
        return DAO.sessionFactory;
    }
    public static SessionFactory getSessionFactory() {
        if(DAO.sessionFactory==null){
            
        Configuration configuration = new Configuration().configure();
        Properties properties=configuration.getProperties();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(properties);
        SessionFactory sessionFactory = configuration
                .buildSessionFactory(builder.build());
         DAO.sessionFactory=sessionFactory;
        }
        return DAO.sessionFactory;
    }
    public static Integer create(DeviceInput deviceInput) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		session.save(deviceInput);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + deviceInput.toString());
		return deviceInput.getId();

	}
    public static List<DeviceInput> readLasthourData(Date date) {
		Session session = getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		Query query = session.createQuery("FROM DeviceInput di "
                        + "where di.created>:date order by di.created ");
                
                query.setParameter("date", date);
                
                List<DeviceInput> deviceInput=query.list();
		session.close();
		System.out.println("Found " + deviceInput.size() + " deviceInputs");
		return deviceInput;

	}
    
        public static Integer createDeviceSettings(DeviceSettings deviceSettings) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
                
		session.saveOrUpdate(deviceSettings);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + deviceSettings.toString());
		return deviceSettings.getId();

	}
        public static Integer updateDeviceSettings(DeviceSettings deviceSettings) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
                
		session.saveOrUpdate(deviceSettings);
		session.getTransaction().commit();
		session.close();
		System.out.println("Successfully created " + deviceSettings.toString());
		return deviceSettings.getId();

	}
        
         public static DeviceSettings fetchDeviceSettings() {
		Session session = getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		Query query = session.createQuery("FROM DeviceSettings ");
                
                
                List<DeviceSettings> deviceSettings=query.list();
		session.close();
		System.out.println("Found " + deviceSettings );
                if(deviceSettings.size()>0){
                    return deviceSettings.get(0);
                }
                return null;

	}
}
