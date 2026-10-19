import java.util.Random;

class Dice {
    private int value;
    private final Random random;

    public Dice() {
        this.value = 1;
        this.random = new Random();
    }

    public int roll() {
        value = random.nextInt(6) + 1;
        return value;
    }

    public int getValue() {
        return value;
    }
}

public class Exercise5_DiceClass_ObjectStateAndRepeatedMethodCalls {
    public static void main(String[] args) {
        int n = 6000;
        int[] frequency = new int[7];
        Dice dice = new Dice();

        System.out.println("Initial value: " + dice.getValue());

        for (int i = 0; i < n; i++) {
            int face = dice.roll();
            frequency[face]++;
        }

        System.out.println("Number of rolls: " + n);
        for (int face = 1; face <= 6; face++) {
            double probability = (double) frequency[face] / n;
            System.out.printf("Face %d: count=%d, experimental probability=%.4f%n",
                    face, frequency[face], probability);
        }
        System.out.println("Last rolled value stored in object: " + dice.getValue());
    }
}
