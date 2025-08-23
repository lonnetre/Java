package balloons;

import java.awt.*;
import java.util.Random;

public class ToughBalloon extends Balloon {

    private int remainingLives = 2; // must be hit multiple times

    public ToughBalloon(double x, double y, BalloonGame game) {
        super(x, y, game);
    }

    @Override
    public void onHit() {

        remainingLives -= 1;
        Color oldColor = this.color;

        // Change the color until the new color is different from the current one
        while (this.color.equals(oldColor)) {
            Random random = new Random();
            this.color = new Color(
                    random.nextInt(0, 256),
                    random.nextInt(0, 256),
                    random.nextInt(0, 256)
            );
        }

        if (remainingLives <= 0) {
            super.onHit();
        }

    }
}
