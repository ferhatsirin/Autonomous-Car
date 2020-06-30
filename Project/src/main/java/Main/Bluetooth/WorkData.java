/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

/**
 *
 * @author ferhat
 */
public class WorkData {

    private byte data[];
    private byte type;

    public void createDataArray(int size) {
        data = new byte[size];
    }

    public void setData(byte val, int index) {
        data[index] = val;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getData(int index) {
        if (0 <= index && index < data.length) {
            return data[index];
        }else{
        
            return 0;
        }
    }

    public byte getType() {
        return type;
    }

}
