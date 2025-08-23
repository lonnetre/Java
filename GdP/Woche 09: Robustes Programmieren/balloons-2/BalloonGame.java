package balloons;

import gdi.game.events.KeyEvent;
import gdi.game.overlay.Label;
import gdi.game.sprite.SpriteWorld;
import gdi.util.math.Vec2D;
import gdi.game.overlay.Panel;

import java.awt.*;
import java.util.Arrays;

public class BalloonGame extends SpriteWorld{

    private Dart dart;
    private Balloon[] balloons;

    public BalloonGame(String[] args) {
        super(args, 800, 600);
    }


    @Override
    protected void setupWorld() {
        super.setupWorld();
        this.setTitle("Balloon Game");

        this.balloons = new Balloon[3];

        // getWidth()/getHeight() or via getWindow() are both okay
        int winWidth = this.getWidth();
        int winHeight = this.getHeight();

        // Create random balloons at random positions
        for (int i = 0; i < this.balloons.length; i++) {
            double newX = (2.0/3.0 * winWidth) * Math.random() + (1.0/3.0 * winWidth);
            double newY = winHeight * Math.random();

            Balloon balloon;
            double randomNumber = Math.random();
            if (randomNumber < 0.25) {
                balloon = new TrojanBalloon(newX, newY, this);
            } else if (randomNumber < 0.5) {
                balloon = new ToughBalloon(newX, newY, this);
            } else {
                balloon = new Balloon(newX, newY, this);
            }

            this.addBalloon(balloon);
        }

        resetDart();
    }

    public void addBalloon(Balloon balloon) {
        if (balloon == null) {
            return;
        }

        // Find an empty slot on the array to add the new ballon
        for (int i = 0; i < this.balloons.length; i++) {
            if (balloons[i] == null) {
                balloons[i] = balloon;
                return;
            }
        }

        // Make the array bigger to make room for the new ballon
        this.balloons = Arrays.copyOf(this.balloons, this.balloons.length + 1);
        this.balloons[this.balloons.length - 1] = balloon;
    }

    public boolean removeBalloon(Balloon balloon) {
        if (balloon == null) {
            return false;
        }

        for (int i = 0; i < this.balloons.length; i++) {
            Balloon b = balloons[i];
            if (b != null && b == balloon) {
                balloons[i] = null;
                this.removeSprite(balloon);
                return true;
            }
        }
        return false;
    }


    @Override
    protected void update(double deltaTime, double time) {
        super.update(deltaTime, time);

        Vec2D dartPos = dart.getLocation();
        if (dartPos.getX() >= this.getWidth() || dartPos.getX() < 0 || dartPos.getY() >= this.getHeight()) {
            resetDart();

        } else {
            Vec2D dartTip = dart.getTip();

            // Check if a balloon is hit by the dart
            for (int i = 0; i < this.balloons.length; i++) {
                Balloon balloon = balloons[i];

                if (balloon != null) {
                    double dist = balloon.getLocation().sub(dartTip).length();

                    if (dist < balloon.getRadius()) {
                        balloon.onHit();
                        resetDart();

                        if (isGameWon()) {
                            String text = "You win!";
                            Panel p = new Panel(this, (this.getWidth() - 150) / 2, (getHeight() - 50) / 2, 150, 50);
                            gdi.game.overlay.Label winnerLabel = new Label(this, 0, 0, p.getWidth(), p.getHeight(), text);
                            p.add(winnerLabel);
                            getOverlayManager().add(p);

                            stop();
                        }

                        break;
                    }
                }
            }
        }
    }

    private void resetDart() {
        if (this.dart != null) {
            this.removeSprite(this.dart);
        }

        dart = new Dart(0, (int)(getHeight() / 2), this);
    }

    private boolean isGameWon() {
        for (int i = 0; i < balloons.length; i++) {
            if (balloons[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void renderBackground(Graphics2D g) {
        if (!this.getSettings().isSlowMachine()) {
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }

        g.setColor(new Color(177,255,255));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void keyDown(KeyEvent key) {
        super.keyDown(key);

        switch(key.getKeyCode()) {
            case 38 -> dart.rotateCounterClockwise();   // up
            case 40 -> dart.rotateClockwise();          // down
        }
    }

    @Override
    protected void keyUp(KeyEvent key) {
        super.keyUp(key);

        switch(key.getKeyCode()) {
            case 38, 40 -> dart.stopRotation(); // up, down
            case 32 -> dart.shoot();            // empty space
        }
    }
}
