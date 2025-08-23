package balloons;

public class TrojanBalloon extends Balloon {
    public TrojanBalloon(double x, double y, BalloonGame game) {
        super(x, y, game);
    }

    @Override
    public void onHit() {
        BalloonGame game = (BalloonGame) this.getSpriteWorld();

        int numNewBalloons = 2;

        // Create two new smaller ballons in close vicinity
        for (int i = 0; i < numNewBalloons; i++) {
            double newX = this.getX() + Math.random() * 2 * this.radius - this.radius;
            newX = Math.max(0, newX);
            newX = Math.min(newX, this.getSpriteWorld().getWidth());

            double newY = this.getY() + Math.random() * 2 * this.radius - this.radius;
            newY = Math.max(0, newY);
            newY = Math.min(newY, this.getSpriteWorld().getHeight());

            Balloon balloon = new Balloon(newX, newY, game);
            balloon.radius /= 2;
            game.addBalloon(balloon);
        }

        super.onHit();
    }
}
