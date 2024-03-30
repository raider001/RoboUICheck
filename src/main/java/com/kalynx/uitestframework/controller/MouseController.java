package com.kalynx.uitestframework.controller;

import com.kalynx.lwdi.DI;
import com.kalynx.uitestframework.MouseEvent.MouseButtonDown;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.kalynx.uitestframework.exceptions.MouseException;

import java.awt.Point;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MouseController {

    private final RobotControl robot;
    private final MouseInfoControl mouseInfo;
    private final DisplayManager displayManager;
    private final Consumer<Point> performMove;
    private long distancePerSecond = 2000;

    @DI
    public MouseController(RobotControl robot, MouseInfoControl mouseInfo, DisplayManager displayManager) {
        this(robot, mouseInfo, displayManager, null);

    }

    public MouseController(RobotControl robot, MouseInfoControl mouseInfo, DisplayManager displayManager, Consumer<Point> performMove) {
        this.performMove = Objects.requireNonNullElseGet(performMove, () -> this::mouseMove);
        this.robot = Objects.requireNonNull(robot);
        this.mouseInfo = Objects.requireNonNull(mouseInfo);
        this.displayManager = displayManager;
    }

    /**
     * Moves the mouse based on the mouse original location.
     *
     * @param x The relative x distance to be moved.
     * @param y The relative y distance to be moved.
     */
    public void moveMouse(int x, int y) {
        int actualX = mouseInfo.getMousePosition().x + x;
        int actualY = mouseInfo.getMousePosition().y + y;
        performMove.accept(new Point(actualX, actualY));
    }

    public void moveMouseTo(String display, int x, int y) throws MouseException, DisplayNotFoundException {
        DisplayAttributes r = displayManager.getDisplay(display);
        if (x < 0 || x > r.width())
            throw new MouseException("Mouse location x: " + x + " must be equal to or between " + 0 + "-" + r.width());
        if (y < 0 || y > r.height())
            throw new MouseException("Mouse location y: " + y + " must be equal to or between " + 0 + "-" + r.height());
        performMove.accept(new Point(x + r.x(), y + r.y()));
    }

    public void moveMouseTo(int x, int y) throws MouseException {
        Point mousePos = mouseInfo.getMousePosition();
        DisplayAttributes currDisplay = getCurrentDisplay(mousePos);

        if (currDisplay == null) throw new MouseException("Can't find mouse on screen! Please report this issue and provide your configuration setup to help stop this from happening again!");

        if (x < 0 || x > currDisplay.width() || y < 0 || y > currDisplay.height())
            throw new MouseException("x %s and y %s out of bounds for current display width %s and height %s".formatted(x, y, currDisplay.width(), currDisplay.height()));
        performMove.accept(new Point(x + currDisplay.x(), y + currDisplay.y()));
    }

    public void mousePress(MouseButtonDown button) {
        robot.mousePress(button.getButton());
    }

    public void mouseRelease(MouseButtonDown button) {
        robot.mouseRelease(button.getButton());
    }

    public void mouseClick(MouseButtonDown button, int totalClicks) throws InterruptedException {
        for (int i = 0; i < totalClicks; i++) {
            mousePress(button);
            Thread.sleep(50);
            mouseRelease(button);
            Thread.sleep(50);
        }
    }

    public void mouseScroll(int scrollAmount) {
        robot.mouseWheel(scrollAmount);
    }

    public void setMouseMoveSpeed(long mouseMoveSpeed) throws MouseException {
        if (mouseMoveSpeed <= 0) throw new MouseException("Mouse Move Speed must be greater than 0");
        this.distancePerSecond = mouseMoveSpeed;
    }

    public long getMouseMoveSpeed() {
        return distancePerSecond;
    }

    public DisplayAttributes getCurrentDisplay(Point p) {
        Optional<DisplayAttributes> currDisplay = displayManager.getDisplays().stream().filter(display -> p.x >= display.x() &&
                p.x <= display.x() + display.width() &&
                p.y >= display.y() &&
                p.y <= display.y() + display.height()).findFirst();
        return currDisplay.orElse(null);
    }

    private void mouseMove(Point destination) {
        Point current = mouseInfo.getMousePosition();
        double distancePerMs = distancePerSecond / 1000.0;
        long lastTime = System.currentTimeMillis();
        while(!current.equals(destination)) {
            long currentTime = System.currentTimeMillis();
            double distanceToTravel = distancePerMs * (currentTime - lastTime);
            double angle = Math.atan2((double)destination.y - current.y, (double)destination.x - current.x);
            if(distanceToTravel >= current.distance(destination)) {
                robot.mouseMove(destination.x, destination.y);
                break;
            }
            int x = (int) (current.x + distanceToTravel * Math.cos(angle));
            int y = (int) (current.y + distanceToTravel * Math.sin(angle));
            robot.mouseMove(x, y);
        }
    }
}
