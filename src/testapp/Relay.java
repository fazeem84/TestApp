/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

/**
 *
 * @author fazeem
 */
public class Relay {
    
    String relay;
    
    String onTime;
    
    String offTime;
    
    String repeatTime;
    
    String repeatInterval;

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Override
    public String toString() {
        return "Relay{" + "relay=" + relay + ", onTime=" + onTime + ", offTime=" + offTime + ", repeatTime=" + repeatTime + ", repeatInterval=" + repeatInterval + '}';
    }
    
    
}
