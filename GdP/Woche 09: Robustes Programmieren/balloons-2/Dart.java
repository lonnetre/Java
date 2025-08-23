package balloons;

import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;

public class Dart extends Sprite {

    private final static int LENGTH = 60;           // length of the line depicting the dart, in px

    private enum State {                            // state of the dart; either it is in the preparation state,
        PREPARATION,                                // where the user can rotate it, or it is already sent flying
        FLYING
    }
    private State state;

    private double angle;                           // current angle in radians of the dart
    private int rotationDirection = 0;              // direction of rotation motion (1 clockwise, -1 counterclockwise)

    private double vx;                              // horizontal velocity
    private double vy;                              // vertical velocity


    public Dart(double x, double y, BalloonGame game) {
        super(x, y, game);
        this.angle = 0;
        state = State.PREPARATION;
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        if (state == State.PREPARATION) {
            // Adjust the rotation based on the rotation speed
            double rotationSpeed = 0.5;
            double da = rotationDirection * rotationSpeed * deltaTime;
            this.angle = (angle + da) % (2 * Math.PI);

            // Prevent the dart from pointing to the left
            if (this.angle > Math.PI / 2) {
                this.angle = Math.PI / 2;
            } else if (this.angle < -Math.PI / 2 ) {
                this.angle = -Math.PI / 2;
            }

            // Framework solution:
            //this.rotateTo(-angle / Math.PI * 180, deltaTime, false, null);

        } else if (state == State.FLYING) {

            // Update the vertical velocity according to the given formula
            this.vy -= 490.5 * deltaTime;

            // Adjust the position based on the velocity
            double dx = this.vx * deltaTime;
            double dy = this.vy * deltaTime;
            this.setLocation(getX() + dx, getY() - dy);

            // Update the angle according the current motion
            this.angle = Math.atan(dy / dx);
        }
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        Vec2D tip = getLocalTip();
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, (int)tip.getX(), (int)tip.getY());
    }

    private Vec2D getLocalTip() {
        double endX = Math.cos(angle) * LENGTH;
        double endY = Math.sin(angle) * LENGTH;

        // If framework solution with rotateTo() is used:
        //double endX = LENGTH;
        //double endY = 0;

        return new Vec2D(endX, -endY);
    }

    public Vec2D getTip() {
        return getLocation().add(getLocalTip());
    }

    public void rotateCounterClockwise() {
        if (state != State.FLYING) {
            this.rotationDirection = 1;
        }
    }

    public void rotateClockwise() {
        if (state != State.FLYING) {
            this.rotationDirection = -1;
        }
    }

    public void stopRotation() {
        this.rotationDirection = 0;
    }

    public void shoot() {
        stopRotation();

        int initialSpeed = 600; // 6m / s
        this.vx = Math.cos(angle) * initialSpeed;
        this.vy = Math.sin(angle) * initialSpeed;
        this.state = State.FLYING;
    }
}
