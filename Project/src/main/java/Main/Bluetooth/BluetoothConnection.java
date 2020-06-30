/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

import Main.GUI.UserControl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author ferhat
 */
public class BluetoothConnection {

    private StreamConnection con;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean connected;
    private BluetoothDevice device;
    private UserControl userControl;
    private GetDataThread getDataThread;
    private ConsumeDataThread consumeDataThread;
    private final BlockingQueue<WorkData> queue;

    public BluetoothConnection() {
        connected = false;
        queue = new LinkedBlockingQueue<>();
    }

    public boolean connectDevice(BluetoothDevice dev) {
        if (device != null && device.equals(dev) && connected) {
            return true;
        } else {
            this.device = dev;
            con = null;
            connected = true;
            try {
                con = (StreamConnection) Connector.open(device.getUrl(), Connector.READ_WRITE);
            } catch (IOException ex) {
                connected = false;
            }
            try {
                if (con != null) {
                    out = con.openDataOutputStream();
                    in = con.openDataInputStream();
                    getDataThread = new GetDataThread(this,in,queue);
                    consumeDataThread = new ConsumeDataThread(this,queue,userControl);
                    getDataThread.start();
                    consumeDataThread.start();
                }
            } catch (IOException ex) {
                connected = false;
            }
        }
        return connected;
    }

    public boolean isConnected() {
        return connected;
    }
    
    public void setConnected(boolean val){
        connected =val;
        if(!val){
            userControl.setConnectionText("Disconnected");
        }
    }

    public BluetoothDevice getDevice() {

        return device;
    }
    public void setControlPanel(UserControl userControl){
        this.userControl =userControl;
    }

    public void closeConnection() {

        try {
            connected = false;
            userControl.setConnectionText("Disconnected");
            out.close();
            in.close();
            con.close();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendData(byte[] data) {

        if (connected) {
            try {
                out.write(data);
                out.flush();
            } catch (IOException ex) {
                closeConnection();
            }
        }
    }

}
