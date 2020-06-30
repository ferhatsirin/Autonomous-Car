/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.CarControl;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ferhat
 */
public class CarMap {

    public byte[][] map;
    private final byte dx[] = {-1, 0, 1, 0};
    private final byte dy[] = {0, 1, 0, -1};
    private boolean[][] visited;
    private final Queue<Cell> queue;
    private int size;
    private Queue<Point> path;
    private boolean overPath;
    private Point lastDest;
    private final Lock lock;
    private final Condition cond;
    private byte[] order;
    private boolean awayFromLeft;
    private boolean awayFromRight;
    private final Car car;
    private final CarCommunication carComm;
    private DriveControl driveControl;

    public CarMap(Car car,CarCommunication carComm) {
        this.car =car;
        this.carComm =carComm;

        queue = new LinkedList<>();
        lock = new ReentrantLock();
        cond = lock.newCondition();
    }
    
    public void setDriveControl(DriveControl driveControl){
    
        this.driveControl =driveControl;
    }

    public void setPoint(int x, int y, int val) {
        if (isValid(x, y) && map[x][y] != 100) {

            map[x][y] = (byte) val;
            if (path != null && path.contains(new Point(x, y))) {
                overPath = true;
            }
        }
    }

    public void takeLock() {

        lock.lock();
    }

    public void releaseLock() {

        cond.signal();
        lock.unlock();
    }

    public boolean isOverPath() {

        return overPath;
    }

    public void setStayAwayFromLeft(boolean val) {
        awayFromLeft = val;
    }

    public boolean getStayAwayFromLeft() {

        return awayFromLeft;
    }

    public void setStayAwayFromRight(boolean val) {

        awayFromRight = val;
    }

    public boolean getStayAwayFromRight() {

        return awayFromRight;
    }

    public void setOverPath(boolean val) {

        overPath = val;
    }

    public Point getLastDest() {

        return lastDest;
    }

    public void setHorizontalRectangle(int x, int y) {

        x = x + 20;
        y = y - 30;

        for (int i = 0; i <= 60; ++i) {
            setPoint(x, y + i, 50);
            setPoint(x - 40, y + i, 50);
        }

        for (int i = 0; i <= 40; ++i) {
            setPoint(x - i, y, 50);
            setPoint(x - i, y + 60, 50);
        }
    }

    public void setVerticalRectangle(int x, int y) {

        x = x + 30;
        y = y - 20;

        for (int i = 0; i <= 60; ++i) {
            setPoint(x - i, y, 50);
            setPoint(x - i, y + 40, 50);
        }

        for (int i = 0; i <= 40; ++i) {
            setPoint(x, y + i, 50);
            setPoint(x - 60, y + i, 50);
        }
    }

    public void drawLeftObstacle(int x, int y, Direction dir, int dist) {

        if (dir == Direction.North) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y - dist, 100);
            }
            setVerticalRectangle(x, y - dist);
        } else if (dir == Direction.East) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + dist, y + i, 100);
            }
            setHorizontalRectangle(x + dist, y);
        } else if (dir == Direction.South) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y + dist, 100);
            }
            setVerticalRectangle(x, y + dist);
        } else if (dir == Direction.West) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x - dist, y + i, 100);
            }
            setHorizontalRectangle(x - dist, y);
        }
    }

    public void drawCenterObstacle(int x, int y, Direction dir, int dist) {

        if (dir == Direction.North) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + dist, y + i, 100);
            }
            setHorizontalRectangle(x + dist, y);
        } else if (dir == Direction.East) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y + dist, 100);
            }
            setVerticalRectangle(x, y + dist);
        } else if (dir == Direction.South) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x - dist, y + i, 100);
            }
            setHorizontalRectangle(x - dist, y);
        } else if (dir == Direction.West) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y - dist, 100);
            }
            setVerticalRectangle(x, y - dist);
        }

    }

    public void drawRightObstacle(int x, int y, Direction dir, int dist) {

        if (dir == Direction.North) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y + dist, 100);
            }
            setVerticalRectangle(x, y + dist);
        } else if (dir == Direction.East) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x - dist, y + i, 100);
            }
            setHorizontalRectangle(x - dist, y);
        } else if (dir == Direction.South) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + i, y - dist, 100);
            }
            setVerticalRectangle(x, y - dist);
        } else if (dir == Direction.West) {
            for (int i = -10; i < 10; ++i) {
                setPoint(x + dist, y + i, 100);
            }
            setHorizontalRectangle(x + dist, y);
        }
    }

    public void setObstacle(int leftD, int centerD, int rightD) {

        Direction dir = car.getCarDirection();
        if (dir == Direction.North || dir == Direction.East || dir == Direction.South || dir == Direction.West) {

            int x = car.getY();
            int y = car.getX();

            takeLock();

            if (5 < leftD) {
                if (leftD <= 15) {
                    setStayAwayFromLeft(true);
                }
                drawLeftObstacle(x, y, dir, leftD);
            }
            if (5 < centerD) {
                drawCenterObstacle(x, y, dir, centerD);
            }

            if (5 < rightD) {
                if (rightD <= 15) {
                    setStayAwayFromRight(true);
                }
                drawRightObstacle(x, y, dir, rightD);
            }

            if (!driveControl.getStopMission() && !driveControl.getStayAway() && (isOverPath() || getStayAwayFromLeft() || getStayAwayFromRight())) {
                carComm.sendStopMissionCommand();
            }

            releaseLock();

        }
    }

    // check whether given cell (row, col)  
    // is a valid cell or not. 
    public boolean isValid(int row, int col) {
        // return true if row number and  
        // column number is in range 
        return (row >= 0) && (row < map.length) && (col >= 0) && (col < map.length);
    }

    Direction findDirection(Point p1, Point p2) {

        if (p1.x - p2.x < 0) {
            return Direction.North;
        } else if (p1.x - p2.x > 0) {
            return Direction.South;
        } else if (p1.y - p2.y < -1) {
            return Direction.East;
        } else {
            return Direction.West;
        }
    }

    // function to find the shortest path between 
    // a given source cell to a destination cell. 
    public void shortestPath(Point src, Point dest) {

        lock.lock();

        path = null;
        overPath = false;
        awayFromLeft = false;
        awayFromRight = false;
        // check source and destination cell 
        // of the matrix have value 1 
        if (map[src.x][src.y] != 0 || map[dest.x][dest.y] != 0 || src.equals(dest)) {
            cond.signal();
            lock.unlock();
            return;
        }

        lastDest = dest;

        for (int i = 0; i < visited.length; ++i) {
            Arrays.fill(visited[i], false);
        }

        try {

            // Mark the source cell as visited 
            visited[src.x][src.y] = true;

            // Create a queue for BFS 
            queue.clear();

            queue.add(new Cell(src.x, src.y)); // Enqueue source cell 

            // Do a BFS starting from source cell 
            while (!queue.isEmpty()) {

                // Otherwise dequeue the front cell  
                // in the queue and enqueue 
                // its adjacent cells 
                Cell curr = queue.remove();

                Direction dir = findDirection(curr.getPoint(), dest);

                if (dir == Direction.North) {
                    order = new byte[]{2, 1, 3, 0};
                } else if (dir == Direction.East) {
                    order = new byte[]{1, 2, 0, 3};
                } else if (dir == Direction.South) {
                    order = new byte[]{0, 1, 3, 2};
                } else {
                    order = new byte[]{3, 2, 0, 1};
                }

                for (int i = 0; i < 4; i++) {
                    int row = curr.x + dx[order[i]];
                    int col = curr.y + dy[order[i]];

                    // if adjacent cell is valid, has path  
                    // and not visited yet, enqueue it. 
                    if (isValid(row, col) && !visited[row][col] && map[row][col] == 0) {

                        // If we have reached the destination cell, 
                        // we are done 
                        if (row == dest.x && col == dest.y) {
                            curr.path.add(new Point(row, col));
                            path = curr.path;
                            return;
                        }
                        // mark cell as visited and enqueue it 
                        visited[row][col] = true;
                        queue.add(new Cell(row, col, curr.path));
                    }
                }
            }
        } finally {
            cond.signal();
            lock.unlock();
        }
    }

    public void setSize(int size) {

        map = new byte[size][size];
        visited = new boolean[size][size];
        this.size = size;
    }

    public int getSize() {

        return size;
    }

    public Queue<Point> getPath() {

        return path;
    }

    public class Cell {

        private int x;
        private int y;
        Queue<Point> path;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
            path = new LinkedList<>();

            path.add(new Point(x, y));
        }

        public Cell(int x, int y, Queue<Point> p) {
            this.x = x;
            this.y = y;
            this.path = new LinkedList<>(p);
            path.add(new Point(x, y));
        }

        public void setPoint(int x, int y) {

            this.x = x;
            this.y = y;
        }

        public Point getPoint() {
            return new Point(x, y);
        }

        public int getX() {

            return x;
        }

        public int getY() {

            return y;
        }
    }

}
