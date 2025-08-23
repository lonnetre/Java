public class Car_L {

    final double CAPACITY_kWh;
    double chargeLevel_kWh;
    double powerConsumption_kWh_per_km;
    double mileage;


    Car_L(double capacity_kWh, double powerConsumption_kWh_per_100km) {
        this.CAPACITY_kWh = capacity_kWh;
        this.powerConsumption_kWh_per_km = powerConsumption_kWh_per_100km / 100.0;

        // chargeLevel_kWh and mileage are already 0, but setting them explicitly is often considered good practice,
        // since other people reading the code might not know that number attributes are 0 by default in Java
        this.chargeLevel_kWh = 0;
        this.mileage = 0;
    }

    Car_L(double capacity_kWh) {
        this(capacity_kWh, 159.0);
    }

    Car_L() {
        this(50);
    }

    double charge(double chargingPower_kW, double duration_h) {
        double oldChargeLevel = this.chargeLevel_kWh;

        double charge = chargingPower_kW * duration_h;
        this.chargeLevel_kWh = Math.min(this.chargeLevel_kWh + charge, this.CAPACITY_kWh);

        return this.chargeLevel_kWh - oldChargeLevel;
    }

    double charge() {
        return charge(this.CAPACITY_kWh, 1);
    }

    boolean drive(double distance_km) {
        double neededPower = distance_km * powerConsumption_kWh_per_km;

        if (neededPower > this.chargeLevel_kWh) {
            System.out.println("The car is not charged enough to manage a drive of " + distance_km + " km." +
                    " Needed charge: " + neededPower + " kWh, current charge: " + this.chargeLevel_kWh + " kWh");
            return false;
        }

        this.chargeLevel_kWh -= neededPower;
        this.mileage += distance_km;
        return true;
    }

    public static void main(String[] args) {
        Car_L car = new Car_L();

        car.drive(500);

        double power = 11;
        double duration = 4.2;
        System.out.println("Charging car with " + power + " kWh for " + duration + " hours");
        double charge = car.charge(power, duration);
        System.out.println("New charge: " + charge + " kWh");

        // For more sophisticated testing, the following statistics should be put into a method
        System.out.println("Before drive:");
        System.out.println("- chargeLevel_kWh: " + car.chargeLevel_kWh);
        System.out.println("- mileage: " + car.mileage);

        double distance = 270;
        System.out.println("Trying to drive " + distance + " km");
        boolean driving_successful = car.drive(distance);
        System.out.println("Drive was successful: " + driving_successful);

        System.out.println("After drive:");
        System.out.println("- chargeLevel_kWh: " + car.chargeLevel_kWh);
        System.out.println("- mileage: " + car.mileage);
    }
}
