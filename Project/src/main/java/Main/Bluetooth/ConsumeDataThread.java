/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Bluetooth;

import Main.GUI.UserControl;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 *
 * @author ferhat
 */
public class ConsumeDataThread extends Thread {

    private final BluetoothConnection bluetoothCon;
    private final BlockingQueue<WorkData> queue;
    private final UserControl userControl;

    public ConsumeDataThread(BluetoothConnection bluetoothCon, BlockingQueue<WorkData> queue, UserControl userControl) {

        this.bluetoothCon = bluetoothCon;
        this.queue = queue;
        this.userControl = userControl;
    }
    
    public double standardDeviation(int[] arr,double mean){
    
        double sum =0;
        double result;
        for(int i=0;i<arr.length;++i){
            sum +=Math.pow(arr[i]-mean,2);
        }
        result =sum/arr.length;
        result =Math.sqrt(result);
        
        return result;
    }

    @Override
    public void run() {

        WorkData work;
        CircularFifoQueue<Integer> leftDistanceData = new CircularFifoQueue<>(10);
       // CircularFifoQueue<Integer> centerDistanceData = new CircularFifoQueue<>(5);
        CircularFifoQueue<Integer> rightDistanceData = new CircularFifoQueue<>(10);
        for (int i = 0; i < 10; ++i) {
            leftDistanceData.add(0);
      //      centerDistanceData.add(0);
            rightDistanceData.add(0);
        }
        int[] leftArr;
    //    int[] centerArr;
        int[] rightArr;
        double leftMean = 0,leftDeviation;
        double rightMean = 0, rightDeviation;
        while (bluetoothCon.isConnected()) {

            try {
                work = queue.take();

                switch (work.getType()) {
                    case 20: { // go forward return
                        userControl.changeCarPosition();
                        break;
                    }
                    case 21: {  // mission ended
                        userControl.setMissionEnded(true);
                        break;
                    }
                    case 30: { // distance values

                        int leftD, centerD, rightD;
                        leftD = (int) (work.getData(0) & 0xFF);
                        centerD = (int) (work.getData(1) & 0xFF);
                        rightD = (int) (work.getData(2) & 0xFF);

                        leftMean = leftMean + (leftD / 10.0) - (leftDistanceData.remove()/10.0);
                        rightMean = rightMean + (rightD / 10.0) - (rightDistanceData.remove()/10.0);

                        leftDistanceData.add(leftD);
                        rightDistanceData.add(rightD);

                        leftArr =leftDistanceData.stream().mapToInt(i -> i).toArray();

                        leftDeviation =standardDeviation(leftArr,leftMean);
                        
                       // System.out.println("left De "+ leftDeviation + " leftD "+leftD);
                        
                        if(4 <=leftDeviation){
                            leftD =0;
                        }
                       
                        rightArr =rightDistanceData.stream().mapToInt(i -> i).toArray();
                        
                        rightDeviation =standardDeviation(rightArr,rightMean);
                        
                     //   System.out.println("right De "+rightDeviation +" rightD "+rightD);
                        
                        if(4 <= rightDeviation){
                            rightD =0;
                        }
                        
                        userControl.setObstacle(leftD, centerD, rightD);

                        break;
                    }
                    case 40: {  // car heading 

                        int pos = (((int) (work.getData(0) & 0xFF)) << 8) | (work.getData(1) & 0xFF);
                        userControl.setCarDirection(pos);
                        break;
                    }
                    case 50: { // flame sensor
                        if (work.getData(0) == 1) {
                            userControl.setFlame(true);
                        } else {
                            userControl.setFlame(false);
                        }
                        break;
                    }
                    case 60: { // temperature result

                        float temp;

                        short t = (short) ((work.getData(0) & 0xFF) << 8 | (work.getData(1) & 0xFF));

                        temp = ((float) t / 16.0f);

                        userControl.setTempText(Float.toString(temp));
                        break;
                    }
                    default:
                        break;
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(ConsumeDataThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
