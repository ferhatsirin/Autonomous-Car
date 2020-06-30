/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import Main.CarControl.CarCommunication;
import Main.Bluetooth.BluetoothConnection;
import Main.CarControl.Car;
import Main.CarControl.CarControl;
import javax.swing.JFrame;


/**
 *
 * @author ferhat
 */
public class UserControl {

    private CarControl carControl;
    private MapPanel mapPanel;
    private ControlPanel controlPanel;
    private BluetoothPanel bluetoothPanel;
    private UserFrame userFrame;
    private final BluetoothConnection bluetoothCon;
    private final CarCommunication carComm;
    private final Car car;
    
    

    public UserControl(CarControl carControl,BluetoothConnection bluetoothCon,CarCommunication carComm) {

        this.carControl = carControl;
        this.bluetoothCon =bluetoothCon;
        this.carComm =carComm;
        this.car =carControl.getCar();
    }
    
    public void initUserInterface(){
    
        bluetoothPanel =new BluetoothPanel(bluetoothCon);
        controlPanel =new ControlPanel(carComm);
        mapPanel =new MapPanel(this,carControl,carComm);
        userFrame =new UserFrame(mapPanel,controlPanel,bluetoothPanel);
        userFrame.initMainFrame();
    }
    
    
    
    public JFrame getMainFrame(){
        return userFrame;
    }
    
    public BluetoothPanel getBluetoothPanel(){
    
        return bluetoothPanel;
    }
    
    public ControlPanel getControlPanel(){
    
        return controlPanel;
    }
    
    public MapPanel getMapPanel(){
    
        return mapPanel;
    }
   

    public void setConnectionText(String text) {
        bluetoothPanel.setConnectionText(text);
    }
    
    public void setMissionEnded(boolean val){
        carControl.getDriveControl().setMissionEnded(val);
    }

    public void setObstacle(int leftD, int centerD,int rightD){
        
        carControl.getCarMap().setObstacle(leftD, centerD, rightD);
        mapPanel.repaint();
    }

    public void setCarDirection(int degree) {
        degree = carComm.fromCompassDegree(degree);
        if (Math.abs(car.getDirection() - degree) >= 4) {
            car.setDirection(degree);
            updateCarPositionText();
        }
    }

    public void setCarPosition(int x, int y) {
        car.setCarPosition(x, y);
        updateCarPositionText();
    }

    public void changeCarPosition() {
        car.goForward();
        updateCarPositionText();
    }

    public void updateCarPositionText() {
        controlPanel.updateCarPositionText(car.getCarPositionText());
        mapPanel.updateCarPanelPosition();
    }
    
    public void setFlame(boolean var){
    
        controlPanel.setFlameIcon(var);
    }

    public void setTempText(String text) {

        controlPanel.setTempText(text);
    }

}
