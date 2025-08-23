package furnace;

public class Main_L {

    public static void main(String[] args) {
        Furnace_L furnace = new Furnace_L();
        int targetTemp = 1000;

        try {
            furnace.setTemperature(targetTemp);
        } catch (TemperatureException_L e) {
            // This code example is too small to do something reasonable like run system tests
            // to see why an invalid temperature was set. So, in this example,
            // we simply throw a RuntimeException to see it :)
            throw new RuntimeException(e);
        }

        furnace.printInfo();
    }
}
