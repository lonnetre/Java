import java.util.LinkedList;
import java.util.Random;

public class DiceHistory_L {

    public static void main(String[] args) {
        long seed = 42; // the seed may be any value
        Random random = new Random(seed);

        LinkedList<Integer> values = new LinkedList<>();
        int sum = 0;
        int newValue;

        do {
            newValue = random.nextInt(10) + 1;
            values.add(newValue);
            sum += newValue;
        } while(newValue != 10);

        System.out.println("Sum: " + sum);
        System.out.print("Values: ");
        for (int i = 0; i < values.size(); i++) {
            System.out.print(values.get(i) + " ");
        }
    }

}