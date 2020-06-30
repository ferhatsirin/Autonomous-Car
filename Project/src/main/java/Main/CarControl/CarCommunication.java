/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.CarControl;

import Main.Bluetooth.BluetoothConnection;

/**
 *
 * @author ferhat
 */
public class CarCommunication {

    private final BluetoothConnection bluetoothCom;
    private final byte[] dataArr;

    public CarCommunication(BluetoothConnection bluetooth) {
        bluetoothCom = bluetooth;
        dataArr = new byte[3];
    }

    public void sendTurnCommand(int degree) {

        if (0 <= degree && degree < 360) {
            degree = toCompassDegree(degree);
            dataArr[0] = 10; // communication code
            dataArr[1] = (byte) ((degree >> 8) & 0xFF);
            dataArr[2] = (byte) (degree & 0xFF);

            bluetoothCom.sendData(dataArr);
        }
    }

    public void sendForwardCommand(int val) {
        if (0 < val && val <= 800) {
            dataArr[0] = 20;  // communication code
            dataArr[1] = (byte) ((val >> 8) & 0xFF);
            dataArr[2] = (byte) (val & 0xFF);
            bluetoothCom.sendData(dataArr);
        }

    }

    public void sendReadTemperatureCommand() {

        dataArr[0] = 60; // communication code
        bluetoothCom.sendData(dataArr);
    }

    public void sendStopMissionCommand() {

        dataArr[0] = 25; // communication code
        bluetoothCom.sendData(dataArr);
    }

    public int toCompassDegree(int degree) {

        if (degree == 0) {
            degree = 355;
        } else if (0 < degree && degree < 45) {
        } else if (45 <= degree && degree < 90) {
            degree = mapInt(degree, 45, 90, 45, 70);
        } else if (90 <= degree && degree < 135) {
            degree = mapInt(degree, 90, 135, 70, 125);
        } else if (135 <= degree && degree < 180) {
            degree = mapInt(degree, 135, 180, 125, 190);
        } else if (180 <= degree && degree < 225) {
            degree = mapInt(degree, 180, 225, 190, 245);
        } else if (225 <= degree && degree < 270) {
            degree = mapInt(degree, 225, 270, 245, 285);
        } else if (270 <= degree & degree < 315) {
            degree = mapInt(degree, 270, 315, 285, 325);
        } else if (315 <= degree && degree < 360) {
            degree = mapInt(degree, 315, 360, 325, 359);
        }

        return degree;
    }

    public int fromCompassDegree(int degree) {

        if (0 <= degree && degree < 45) {
        } else if (45 <= degree && degree < 70) {
            degree = mapInt(degree, 45, 70, 45, 90);
        } else if (70 <= degree && degree < 125) {
            degree = mapInt(degree, 70, 125, 90, 135);
        } else if (degree <= 125 && degree < 190) {
            degree = mapInt(degree, 125, 190, 135, 180);
        } else if (190 <= degree && degree < 245) {
            degree = mapInt(degree, 190, 245, 180, 225);
        } else if (245 <= degree && degree < 285) {
            degree = mapInt(degree, 245, 285, 225, 270);
        } else if (285 <= degree && degree < 325) {
            degree = mapInt(degree, 285, 325, 270, 315);
        } else if (325 <= degree && degree < 345) {
            degree = mapInt(degree, 325, 360, 315, 359);
        } else if (345 <= degree) {
            degree = 0;
        }

        return degree;
    }

    public int mapInt(int num, int A, int B, int C, int D) {

        return (int) (((double) (num - A) / (double) (B - A)) * (D - C) + C);
    }

}
