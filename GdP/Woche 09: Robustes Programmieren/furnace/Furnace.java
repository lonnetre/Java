package furnace;

public class Furnace_L {

    public final static int SAFETY_TEMPERATURE = 300;
    public final int maxTemperature;

    // Set attributes to private
    private int temperature;
    private boolean doorLocked;

    public Furnace_L(int maxTemperature) {
        this.maxTemperature = maxTemperature;
        this.temperature = 0;
        this.doorLocked = false;
    }

    public Furnace_L() {
        this(600);
    }

    public void printInfo() {
        System.out.println("Current temperature: " + temperature + "/" + maxTemperature + " C");
        System.out.println("Door locked: " + doorLocked);
    }

    // Getters
    public int getTemperature() {
        return temperature;
    }

    public boolean isDoorLocked() {
        return doorLocked;
    }

    // Setters
    /**
     * Configures a new temperature for the furnace and closes the door
     * if this new temperature is above the safety temeprature
     * @param temperature the new temperature
     * @throws TemperatureException_L if the given temperature is less than 0C
     * or higher than the max. allowed temperature
     */
    public void setTemperature(int temperature) throws TemperatureException_L {

        if(temperature < 0 || temperature > maxTemperature) {
            // By throwing an Exception (that is not a RuntimeException),
            // the method caller HAS TO prepare for the case that they pass an invalid value
            // to this method. In case of a furnace, where a catastrophe might happen if something is
            // not going as planned, an Exception is better than silently doing nothing (return;).
            throw new TemperatureException_L("The temperature cannot be set to " + temperature + " C");
        }

        // First lock the door...
        if(temperature >= Furnace_L.SAFETY_TEMPERATURE) {
            doorLocked = true;
        }

        //... and then change the temperature
        // Otherwise there is a short moment of time, when the temperature is higher
        // than the safety temperature and the door is still unlocked!
        this.temperature = temperature;
    }


    /**
     * Opens/closes the furnace door
     * @param doorLocked if the door is supposed to be open or closed
     * @throws TemperatureException_L if the door should be opened but the temperature
     * is above the safety temperature
     */
    public void setDoorLocked(boolean doorLocked) throws TemperatureException_L {
        if(!doorLocked && temperature >= Furnace_L.SAFETY_TEMPERATURE) {
            // By throwing an Exception (that is not a RuntimeException),
            // the method caller HAS TO prepare for the case that they pass an invalid value
            // to this method. In case of a furnace, where a catastrophe might happen if something is
            // not going as planned, an Exception is better than silently doing nothing (return;).
            throw new TemperatureException_L("Cannot unlock door during high temperatures!");
        }
        this.doorLocked = doorLocked;
    }

}