package com.kalynx.snagtest.control;

import com.kalynx.snagtest.MouseEvent.MouseButtonDown;
import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.RobotControl;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MouseController {

    private long mouseMoveSpeed = 700;
    private final RobotControl robot;
    private final MouseInfoControl mouseInfo;
    private final List<Rectangle> displays;
    private final Consumer<Point> performMove;
    public MouseController(RobotControl robot, MouseInfoControl mouseInfo, List<Rectangle> displays) {
        this(robot, mouseInfo,displays, null);

    }

    public MouseController(RobotControl robot, MouseInfoControl mouseInfo, List<Rectangle> displays, Consumer<Point> performMove) {
       if(performMove != null) {
           this.performMove = performMove;
       } else {
           this.performMove = this::mouseMove;
       }
        this.robot = Objects.requireNonNull(robot);
        this.mouseInfo = Objects.requireNonNull(mouseInfo);
        this.displays = Objects.requireNonNull(displays);
    }

    /**
     * Moves the mouse based on the mouse original location.
     * @param x The relative x distance to be moved.
     * @param y The relative y distance to be moved.
     */
    public void moveMouse(int x, int y) {
        int actualX = mouseInfo.getMousePosition().x + x;
        int actualY = mouseInfo.getMousePosition().y + y;
        performMove.accept(new Point(actualX, actualY));
    }

    public void moveMouseTo(int display, int x, int y) throws Exception {
        Rectangle r = displays.get(display);
        if(x < 0 || x > r.width) throw new Exception("Mouse location x: " + x + " must be equal to or between " + 0 + "-" + r.width);
        if(y < 0 || y > r.height) throw new Exception("Mouse location y: " + y + " must be equal to or between " + 0 + "-" + r.height);
        performMove.accept(new Point(x + r.x, y + r.y));
    }

    public void moveMouseTo(int x, int y) throws Exception {
        Point mousePos = mouseInfo.getMousePosition();
        Rectangle currDisplay = getCurrentDisplay(mousePos);

        if(currDisplay == null) throw new Exception("Can't find mouse on screen! Please report this issue and provide your configuration setup to help stop this from happening again!");

        if(x < 0 || x > currDisplay.width || y < 0 || y > currDisplay.height)
            throw new Exception("x %s and y %s out of bounds for current display width %s and height %s".formatted(x,y, currDisplay.width, currDisplay.height));
        performMove.accept(new Point(x + currDisplay.x, y + currDisplay.y));
    }

    public void mousePress(MouseButtonDown button) {
        robot.mousePress(button.getButton());
    }

    public void mouseRelease(MouseButtonDown button) {
        robot.mouseRelease(button.getButton());
    }

    public void mouseClick(MouseButtonDown button, int totalClicks) {
        for(int i = 0; i < totalClicks; i++) {
            mousePress(button);
            mouseRelease(button);
        }
    }

    public void mouseScroll(int scrollAmount) {
        robot.mouseWheel(scrollAmount);
    }

    public void setMouseMoveSpeed(long mouseMoveSpeed) {
        if(mouseMoveSpeed < 0) throw new IllegalArgumentException("Mouse Move Speed must be greater than 0");
        this.mouseMoveSpeed = mouseMoveSpeed;
    }

    public Rectangle getCurrentDisplay(Point p) {
        Optional<Rectangle> currDisplay = displays.stream().filter(display -> p.x >= display.x &&
                p.x <= display.x + display.width &&
                p.y >= display.y &&
                p.y <= display.y + display.height).findFirst();
        return currDisplay.orElse(null);
    }
    private void mouseMove(Point destination) {
        long lastTime = System.currentTimeMillis();
        Point current = mouseInfo.getMousePosition();
        double startDistance = current.distance(destination);
        double distancePerMs = startDistance / mouseMoveSpeed;
        while(!destination.equals(current)) {
            current = mouseInfo.getMousePosition();
            long timeDifference = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            double calcDistanceOnTime = distancePerMs * timeDifference;
            double angle = Math.toRadians(Math.atan2(destination.getY() - current.getY(), destination.getX() - current.getX() ) * ( 180 / Math.PI ));

            double distanceToFinalLocation = current.distance(destination);
            if(distanceToFinalLocation > calcDistanceOnTime) {
                long xStep = Math.round(calcDistanceOnTime * cos(angle));
                long yStep = Math.round(calcDistanceOnTime * sin(angle));
                robot.mouseMove((int)(current.getX() + xStep), (int) (current.getY() + yStep));
            } else {
                robot.mouseMove(destination.x, destination.y);
                break;
            }
            try {
                //noinspection BusyWait
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
