/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.CarControl;

import Main.GUI.MapPanel;
import Main.GUI.UserControl;
import java.awt.Point;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ferhat
 */
public class DriveControl {

    private final ExecutorService executor;
    private Future<?> submit;
    private boolean stopMission;
    private final Lock lock;
    private final Condition cond;
    private boolean missionEnded;
    private final CarMap carMap;
    private final CarCommunication carComm;
    private final Car car;
    private MapPanel mapPanel;
    private boolean stayAway;
    
    public DriveControl(Car car,CarMap carMap,CarCommunication carComm) {
 
        this.car =car;
        this.carMap =carMap;
        this.carComm = carComm;

        executor = Executors.newSingleThreadExecutor();

        lock = new ReentrantLock();
        cond = lock.newCondition();
    }
    
    public boolean getStayAway(){
    
        return stayAway;
    }
    
    public void setMapPanel(MapPanel mapPanel){
    
        this.mapPanel =mapPanel;
    }

    public void setStopMission(boolean val) {
        lock.lock();
        try {
            stopMission = val;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    public void setMissionEnded(boolean val) {
        lock.lock();
        try {
            missionEnded = val;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }

    public boolean getStopMission() {
        return stopMission;
    }

    public void waitMissionEnded() {

        while (!stopMission && !missionEnded) {
            try {
                cond.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    int directionToDegree(Direction dir) {

        switch (dir) {
            case North:
                return 0;
            case East:
                return 90;
            case South:
                return 180;
            default:
                return 270;
        }
    }

    Direction findDirection(Point p1, Point p2) {

        if (p1.x - p2.x == -1) {
            return Direction.North;
        } else if (p1.x - p2.x == 1) {

            return Direction.South;
        } else if (p1.y - p2.y == -1) {

            return Direction.East;
        } else {

            return Direction.West;
        }
    }

    public void goCommand(int unit, Direction dir) {

        while (!stopMission && car.getCarDirection() != dir) {
            missionEnded = false;
            carComm.sendTurnCommand(directionToDegree(dir));
            waitMissionEnded();
        }
        if (!stopMission) {
            missionEnded = false;
            carComm.sendForwardCommand(unit);
            waitMissionEnded();
        }
    }

    public void goTarget() {

        if (submit == null || submit.isDone()) {
            submit = executor.submit(() -> {
                Queue<Point> path = carMap.getPath();
                if (path != null) {

                    Point p1, p2, p3;
                    int count;
                    Direction dir;

                    stopMission = false;
                    missionEnded = true;

                    while (path != null && !stopMission && 0 < path.size()) {

                        lock.lock();
                        try {

                            if (carMap.isOverPath() || carMap.getStayAwayFromRight() || carMap.getStayAwayFromLeft()) {
                                stayAway =true;
                                if (carMap.getStayAwayFromLeft()) {
                                    if (car.getCarDirection() == Direction.North) {
                                        goCommand(25, Direction.East);
                                    } else { // south
                                        goCommand(25, Direction.West);
                                    }
                                } else if (carMap.getStayAwayFromRight()) {
                                    if (car.getCarDirection() == Direction.North) {
                                        goCommand(25, Direction.West);
                                    } else { // south
                                        goCommand(25, Direction.East);
                                    }
                                }
                                carMap.shortestPath(car.getCarArrayPosition(), carMap.getLastDest());
                                path = carMap.getPath();
                                  
                                mapPanel.repaint();
                                
                                stayAway =false;
                            } else {
                                if (1 < path.size()) {

                                    count = 1;

                                    p1 = path.poll();

                                    p3 = car.getCarArrayPosition();
                                    if (!p3.equals(p1)) {
                                        p2 = p1;
                                        p1 = car.getCarArrayPosition();
                                    } else {
                                        p2 = path.poll();
                                    }
                                    dir = findDirection(p1, p2);

                                    while (0 < path.size() && count <= 100) {
                                        p3 = path.peek();
                                        if (findDirection(p2, p3) == dir) {
                                            path.poll();
                                            p2 = p3;
                                            ++count;
                                        } else {
                                            break;
                                        }
                                    }
                                    goCommand(count, dir);

                                } else {

                                    p1 = car.getCarArrayPosition();

                                    p2 = path.poll();

                                    dir = findDirection(p1, p2);

                                    goCommand(1, dir);
                                }
                            }
                        } finally {

                            lock.unlock();
                        }

                    }
                }
                stopMission = true;
            });
        }
    }

}
