/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Main.CarControl.CarMap;
import Main.CarControl.CarMap.Cell;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author ferhat
 */
public class MainTest {

    public static void main(String[] args) {

//        CarMap map = new CarMap();
//
//        map.map = new byte[][]{
//        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
//        {1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
//        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
//        {0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//        
//        map.shortestPath(new Point(0,0), new Point(5,0));
//        
//        Queue<Point> path =map.getPath();
//    
//        
//        for(Point e : path){
//            map.setPoint(e.x,e.y,(byte)2);
//        }
//        
//        for(int i=0;i<map.map.length;++i){
//            for(int j=0;j<map.map[i].length;++j){
//                System.out.print(map.map[i][j] +" ");
//            }
//            System.out.println();
//        }

        Point p1 =new Point(3,4);
        Point p2 =new Point(5,6);
        
        double  d =p1.distance(p2);
        
        System.out.println(d);

    }

}
