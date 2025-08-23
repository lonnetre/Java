package balloons;

import gdi.game.sprite.Sprite;
import gdi.util.math.Vec2D;

import java.awt.*;
import java.util.Random;

public class Balloon extends Sprite {

    protected Color color;
    protected int radius = 50;
    private int vy = 100; // px per s
    private int direction = 1;

    public Balloon(double x, double y, BalloonGame game) {
        super(x, y, game);

        Random random = new Random();
        this.color = new Color(random.nextInt(0, 256), random.nextInt(0, 256), random.nextInt(0, 256));
    }

    @Override
    public void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        Vec2D pos = this.getLocation();
        Vec2D newPos = new Vec2D(pos.x, pos.y + direction * vy * deltaTime);

        int height = this.getAbstractSpriteWorld().getWindow().getHeight();
        if (newPos.y >= height) {
            double rest = newPos.y % height;
            direction = -1;
            newPos = new Vec2D(pos.x, (height - 1) + direction * rest);
        } else if (newPos.y < 0) {
            double rest = -newPos.y % height;
            direction = 1;
            newPos = new Vec2D(pos.x, 0 + direction * rest);
        }

        this.setLocation(newPos);
    }

    @Override
    protected void renderLocal(Graphics2D g) {
        g.setColor(color);
        g.fillOval(-this.radius, -this.radius, 2 * this.radius, 2 * this.radius);
        g.fillPolygon(new int[]{(int)(-this.radius * 0.2), (int)(this.radius * 0.2), 0}, new int[]{ (int)(this.radius + this.radius * 0.3), (int)(this.radius + this.radius * 0.3), (int)(this.radius - 0.05 * this.radius)}, 3);
    }

    public int getRadius() {
        return radius;
    }

    public void onHit() {
        BalloonGame game = (BalloonGame) this.getSpriteWorld();
        game.removeBalloon(this);
    }
}
