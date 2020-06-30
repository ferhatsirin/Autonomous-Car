/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

import java.io.IOException;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author ferhat
 */
public class BluetoothDevice {
    private RemoteDevice rd;
    private String url;
    private String name;

    public BluetoothDevice(RemoteDevice rd, String url) {
        this.rd = rd;
        this.url = url;
        try {
            this.name =rd.getFriendlyName(true);
        } catch (IOException ex) {
            //Logger.getLogger(BluetoothDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getName() {
        return name;
    }
   
    public RemoteDevice getRd() {
        return rd;
    }

    public String getUrl() {
        return url;
    }

    public void setRd(RemoteDevice rd) {
        this.rd = rd;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public boolean equals(Object o){
        return this.rd.equals(o);
    }
    
    @Override
    public String toString(){
    
        return name;
    }
    
}
