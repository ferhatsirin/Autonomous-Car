/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.CarControl;

import java.awt.Point;

/**
 *
 * @author ferhat
 */
public class Car {

    private int x;
    private int y;
    private int direction;

    public Car() {
        x =0;
        y =0;
        direction =0;
    }

    public Direction getCarDirection() {
        int degree = direction;

        Direction dir = Direction.North;
        if (345 < degree || degree < 15) {  // north
            dir = Direction.North;
        } else if (15 <= degree && degree < 65) { // north east 
            dir = Direction.NorthEast;
        } else if (65 <= degree && degree < 115) {  // east
            dir = Direction.East;
        } else if (115 <= degree && degree < 165) {  // south east
            dir = Direction.SouthEast;
        } else if (165 <= degree && degree < 195) { // south 
            dir = Direction.South;
        } else if (195 <= degree && degree < 245) { // south west
            dir = Direction.SouthWest;
        } else if (245 <= degree && degree < 295) {  // west
            dir = Direction.West;
        } else if (295 <= degree && degree < 345) { // north west
            dir = Direction.West;
        }

        return dir;
    }

    public void goForward() {
        Direction dir = getCarDirection();

        if (dir == Direction.North) {  // north
            ++y;
        } else if (dir == Direction.NorthEast) { // north east 
            ++x;
            ++y;
        } else if (dir == Direction.East) {  // east
            ++x;
        } else if (dir == Direction.SouthEast) {  // south east
            ++x;
            --y;
        } else if (dir == Direction.South) { // south 
            --y;
        } else if (dir == Direction.SouthWest) { // south west
            --x;
            --y;
        } else if (dir == Direction.West) {  // west
            --x;
        } else if (dir == Direction.NorthWest) { // north west
            --x;
            ++y;
        }
    }

    public void setCarPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getCarPositionText() {
        return String.format(x + "," + y + "," + direction);

    }

    public Point getCarPosition() {
        return new Point(x, y);
    }
    
    public Point getCarArrayPosition(){
    
        return new Point(y,x);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int degree) {
        direction = degree;
    }
}
