/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.CarControl;

import Main.GUI.UserControl;

/**
 *
 * @author ferhat
 */
public class CarControl {

    private final CarMap carMap;
    private final Car car;
    private final DriveControl driveControl;
    private final CarCommunication carComm;
    private UserControl userControl;

    public CarControl(CarCommunication carComm) {
        
        this.carComm =carComm;
        
        car =new Car();
        carMap =new CarMap(car,carComm);
        driveControl =new DriveControl(car,carMap,carComm);
        carMap.setDriveControl(driveControl);

    }
    
    public void setUserControl(UserControl userControl){
    
        this.userControl =userControl;
        driveControl.setMapPanel(userControl.getMapPanel());
    }
    
    public Car getCar(){
    
        return car;
    }
    
    public CarMap getCarMap(){
    
        return carMap;
    }
    
    public DriveControl getDriveControl(){
    
        return driveControl;
    }
}
