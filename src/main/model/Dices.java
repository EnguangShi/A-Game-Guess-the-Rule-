package model;

import java.util.Random;

// The Dices Class represents two 9-sided dices and stores the methods for dices.

public class Dices {
    public int dice1;
    public int dice2;

    // EFFECTS: create 2 dices with initial value of 0
    public Dices() {
        dice1 = 0;  // unrolled
        dice2 = 0;  // unrolled
    }

    // MODIFIES: this
    // EFFECTS: give both dices random integers in [1, 9]
    public void roll() {
        Random random1 = new Random();
        Random random2 = new Random();
        dice1 = random1.nextInt(9) + 1;
        dice2 = random2.nextInt(9) + 1;
    }

    // EFFECTS: let the player choose the return value between dice1 or dice2
    //          or choose whatever number a third dice gives.
    //          if the player type in none of the "left", "right", or "neither"
    //          return 0 for wrong word input.
    public int chooseDice(String choose) {
        Random random3 = new Random();
        if ("left".equals(choose)) {
            return dice1;
        } else if ("right".equals(choose)) {
            return dice2;
        } else if ("neither".equals(choose)) {
            return random3.nextInt(9) + 1;
        }
        return 0;  // input not correct
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }
}
