package balloons;

public class Main {

    public static void main(String[] args) {
        BalloonGame game = new BalloonGame(new String[]{"--slowMachine"});
        //game.getSettings().setPerformanceLog(true);
        game.run();
    }
}
