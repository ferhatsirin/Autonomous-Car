/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ferhat
 */
public class GetDataThread extends Thread {

    private final BluetoothConnection bluetoothCon;
    private final DataInputStream in;
    private final BlockingQueue<WorkData> queue;

    public GetDataThread(BluetoothConnection bluetoothCon, DataInputStream in, BlockingQueue<WorkData> queue) {

        this.bluetoothCon = bluetoothCon;
        this.in = in;
        this.queue = queue;

    }

    @Override
    public void run() {
        while (bluetoothCon.isConnected()) {

            try {
                WorkData work = new WorkData();
                work.setType(in.readByte());

                switch (work.getType()) {
                    case 20: { // go forward return
                        break;
                    }
                    case 21: { // mission ended
                        break;
                    }
                    case 30: { // ultrasonic sensors
                        work.createDataArray(3);
                        for (int i = 0; i < 3; ++i) {
                            work.setData(in.readByte(), i);
                        }
                        break;
                    }
                    case 40: {  // car heading 
                        work.createDataArray(2);
                        for (int i = 0; i < 2; ++i) {
                            work.setData(in.readByte(), i);
                        }
                        break;
                    }
                    case 50: { // flame sensor
                        work.createDataArray(1);
                        work.setData(in.readByte(), 0);
                        break;
                    }
                    case 60: { // temperature result
                        work.createDataArray(2);
                        for (int i = 0; i < 2; ++i) {
                            work.setData(in.readByte(), i);
                        }
                        break;
                    }
                    default:
                        break;
                }
                queue.put(work);
            } catch (IOException ex) {
                bluetoothCon.setConnected(false);
            } catch (InterruptedException ex) {
                Logger.getLogger(GetDataThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
