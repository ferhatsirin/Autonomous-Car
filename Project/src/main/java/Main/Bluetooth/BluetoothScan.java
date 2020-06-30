/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 *
 * @author ferhat
 */
public class BluetoothScan {

    private LocalDevice localDevice;
    private DiscoveryAgent agent;
    private List<BluetoothDevice> remoteDevices;
    private BluetoothDevice tempDevice;
    private final Lock lock;
    private final Condition cond;
    private boolean completed;

    public BluetoothScan() {

        lock = new ReentrantLock();
        cond = lock.newCondition();
        remoteDevices = new ArrayList<>();

        init();
    }

    private void init() {

        localDevice = null;
        agent = null;

        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothScan.class.getName()).log(Level.SEVERE, null, ex);
        }

        agent = localDevice.getDiscoveryAgent();

    }

    public void scanDevices() {

        remoteDevices.clear();
        discoverDevices();

        for (BluetoothDevice rd : remoteDevices) {
            tempDevice = rd;
            discoverServices(rd);
        }
        remoteDevices.removeIf(n -> (n.getUrl() == null || n.getName() == null));
    }
    
    public List<BluetoothDevice> getDevices(){
        return remoteDevices;
    }

    private void discoverDevices() {

        lock.lock();
        completed = false;
        try {
            agent.startInquiry(DiscoveryAgent.GIAC, new BTListener());
            while (!completed) {
                cond.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(BluetoothScan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothScan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void discoverServices(BluetoothDevice bd) {

        UUID[] uuidSet = new UUID[1];
        uuidSet[0] = new UUID(0x0003); //RFCOMM service

        try {
            lock.lock();
            completed = false;
            agent.searchServices(null, uuidSet, bd.getRd(), new BTListener());
            try {
                while (!completed) {
                    cond.await();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(BluetoothScan.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothScan.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class BTListener implements DiscoveryListener {

        private final List<RemoteDevice> discoveredDevices;

        public BTListener() {

            discoveredDevices = new ArrayList<>();
        }

        @Override
        public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
            discoveredDevices.add(rd);
        }

        @Override
        public void servicesDiscovered(int i, ServiceRecord[] srs) {
            for (ServiceRecord record : srs) {
                String url = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
                if ((url != null) && (url.toLowerCase().startsWith("btspp://"))) {
                    tempDevice.setUrl(url);
                }
            }
        }

        @Override
        public void serviceSearchCompleted(int i, int i1) {
            lock.lock();
            try {
                completed = true;
                cond.signal();

            } finally {
                lock.unlock();
            }
        }

        @Override
        public void inquiryCompleted(int i) {

            lock.lock();
            try {
                for (int k = 0; k < discoveredDevices.size(); ++k) {
                    RemoteDevice d = discoveredDevices.get(k);
                    if (!remoteDevices.contains(d)) {
                        remoteDevices.add(new BluetoothDevice(d, null));
                    }
                }
                completed = true;
                cond.signal();
            } finally {
                lock.unlock();
            }
        }

    }

}
