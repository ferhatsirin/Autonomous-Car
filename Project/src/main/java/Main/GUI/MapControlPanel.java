/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GUI;

import Main.CarControl.Car;
import Main.CarControl.CarControl;
import Main.CarControl.CarMap;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author ferhat
 */
public class MapControlPanel extends JPanel {

    private final int scale;
    private int scaleMap;
    private final int leftGap;
    private final int rightGap;
    private final int bottomGap;
    private final int topGap;
    private boolean showGridPanel;
    private final Point targetPosition;
    private Image arrowImg;
    private AffineTransform affine;
    private boolean showPath;
    private Point[] pathArr;
    private BasicStroke pathS;
    private int height;
    private int width;
    private final MapPanel mapPanel;
    private final Car car;
    private final CarMap carMap;
 

    public MapControlPanel(MapPanel mapPanel,CarControl carControl) {

        this.mapPanel =mapPanel;
        this.car =carControl.getCar();
        this.carMap =carControl.getCarMap();

        showGridPanel = true;

        targetPosition = new Point(0, 0);

        affine = new AffineTransform();

        pathArr = new Point[100];
        pathS = new BasicStroke(5);

        scale = 30; // 30 pixel per cm
        scaleMap = 1;

        leftGap = 40;
        rightGap = 20;
        bottomGap = 40;
        topGap = 40;

        showPath = true;

        try {
            arrowImg = ImageIO.read(getClass().getClassLoader().getResource("img" + File.separator + "arrow.png"));
        } catch (IOException ex) {
            Logger.getLogger(UserControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.addMouseListener(new MapAreaListener());
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D graph = (Graphics2D) g;

        // x axis
        graph.drawLine(leftGap, height - bottomGap, width - rightGap, height - bottomGap);
        graph.fillPolygon(new int[]{width - 20, width - 20, width - 15}, new int[]{height - 35, height - 45, height - 40}, 3);

        // y axis
        graph.drawLine(leftGap, topGap, leftGap, height - bottomGap);
        graph.fillPolygon(new int[]{35, 45, 40}, new int[]{40, 40, 35}, 3);

        graph.drawString("x -axis", width - 100, height - 5);
        graph.drawString("y-axis", 20, 20);

        // x axis scale
        for (int i = leftGap + scale, j = scaleMap; i < width - rightGap; i += scale, j += scaleMap) {
            graph.drawLine(i, height - bottomGap - 5, i, height - bottomGap + 5);
            graph.drawString(Integer.toString(j), i - 5, height - 20);
            if (showGridPanel) {
                for (int k = height - bottomGap; topGap < k; k -= 10) {
                    graph.drawLine(i, k, i, k - 5);
                }
            }
        }

        // y axis scale
        for (int i = height - bottomGap - scale, j = scaleMap; rightGap < i; i -= scale, j += scaleMap) {
            graph.drawLine(leftGap - 5, i, leftGap + 5, i);
            graph.drawString(Integer.toString(j), 10, i);
            if (showGridPanel) {
                for (int k = leftGap + 10; k < width - rightGap; k += 10) {
                    graph.drawLine(k, i, k + 5, i);
                }
            }
        }

        byte[][] map =carMap.map;
        // draw obstacle
        for (int i = 0; i < carMap.getSize(); ++i) {
            for (int j = 0; j < carMap.getSize(); ++j) {
                if (map[i][j] == 100) {
                    graph.fillRect(toMapX(j), toMapY(i) - scale, scale, scale);
                }
            }
        }

        // draw target point
        graph.fillOval(toMapX(targetPosition.x) + 10, toMapY(targetPosition.y) - 20, 10, 10);

        // draw car
        graph.drawImage(arrowImg, affine, null);

        Queue<Point> path =carMap.getPath();
        //draw path
        if (showPath && path != null) {
            pathArr = path.toArray(pathArr).clone();
            int i;
            for (i = 0; i < pathArr.length - 1 && pathArr[i] != null && pathArr[i + 1] != null; ++i) {
                graph.setStroke(pathS);
                graph.drawLine(toMapX(pathArr[i].y) + 15, toMapY(pathArr[i].x) - 15, toMapX(pathArr[i + 1].y) + 15, toMapY(pathArr[i + 1].x) - 15);
            }
            if (i < pathArr.length && pathArr[i] != null) {
                graph.fillOval(toMapX(pathArr[i].y) + 10, toMapY(pathArr[i].x) - 20, 10, 10);
            }
        }
    }

    public int toMapX(int v) {
        v = v / scaleMap;

        return scale * v + leftGap;
    }

    public int toMapY(int v) {

        v = v / scaleMap;

        return height - bottomGap - scale * v;
    }

    public void setMapSize(int size) {

        carMap.setSize(size);

        if (300 < size) {
            size = size / 5;
            scaleMap = 5;
            arrowImg = arrowImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

        } else {
            scaleMap = 1;
            arrowImg = arrowImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        }

        height = size * scale + bottomGap + topGap;
        width = size * scale + leftGap + rightGap;

        super.setPreferredSize(new Dimension(width, height));
        car.setCarPosition(size * scaleMap / 2, size * scaleMap / 2);
        
        super.repaint();
    }

    public void setGridVisible(boolean show) {
        showGridPanel = show;

        super.repaint();
    }

    public boolean isGridVisible() {
        return showGridPanel;
    }

    public void setPathVisible(boolean val) {

        showPath = val;
        super.repaint();
    }

    public boolean isPathVisible() {
        return showPath;
    }

    public void setCarTranslation() {
        affine.setToTranslation(carMapX(), carMapY());
        affine.rotate(Math.toRadians(car.getDirection()), arrowImg.getWidth(null) / 2, 0);

        super.repaint();
    }

    public int carMapX() {

        return toMapX(car.getX()) - arrowImg.getWidth(null) / 2 + 15;
    }

    public int carMapY() {
        return toMapY(car.getY()) - 15;

    }

    public Point getTargetPosition() {
        return targetPosition;
    }
    
    public Point getTargetArrayPosition(){
    
        return new Point(targetPosition.y,targetPosition.x);
    }

    private class MapAreaListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            Point clk = e.getPoint();
            targetPosition.x = ((clk.x - leftGap) / scale) * scaleMap;
            targetPosition.y = ((height - bottomGap - clk.y) / scale) * scaleMap;
            mapPanel.setTargetPosition(String.format(targetPosition.x + "," + targetPosition.y));

            repaint();
        }

    }

}
