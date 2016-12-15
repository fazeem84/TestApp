/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp.hibernate.dao;

import java.util.Date;
import testapp.hibernate.pojo.DeviceSettings;

/**
 *
 * @author fazeem
 */
public class DBInitializer {
    
    public static void main(String[] args) {
        DeviceSettings deviceSettings=new DeviceSettings();
        deviceSettings.setT1(0);
        deviceSettings.setT2(0);
        deviceSettings.setT3(0);
        deviceSettings.setAM(0);
        deviceSettings.setNI(0);
        deviceSettings.setPH(0);
        deviceSettings.setSL(0);
        deviceSettings.setUpdateDate(new Date());
        DAO.getCreateSessionFactory();
        DAO.createDeviceSettings(deviceSettings);
        System.exit(0);
        
    }
    
}
