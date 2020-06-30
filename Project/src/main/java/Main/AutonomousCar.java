/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Main.Bluetooth.BluetoothConnection;
import Main.CarControl.CarCommunication;
import Main.CarControl.CarControl;
import Main.GUI.UserControl;

/**
 *
 * @author ferhat
 */
public class AutonomousCar {

    public static void main(String[] args) {

        BluetoothConnection bluetoothCon = new BluetoothConnection();
        CarCommunication carComm = new CarCommunication(bluetoothCon);
        CarControl carControl = new CarControl(carComm);
        
        UserControl userControl = new UserControl(carControl,bluetoothCon,carComm);
        userControl.initUserInterface();
        
        bluetoothCon.setControlPanel(userControl);
        carControl.setUserControl(userControl);
        
    }
}
