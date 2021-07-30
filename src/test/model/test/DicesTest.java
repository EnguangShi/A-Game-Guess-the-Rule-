package model.test;

import model.Dices;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DicesTest {

    private Dices testDices = new Dices();

    @Test
    public void testRoll() {
        for (int i = 0; i < 100; i++) {
            testDices.roll();
            assertTrue(testDices.dice1 >= 1 && testDices.dice1 <= 9);
            assertTrue(testDices.dice2 >= 1 && testDices.dice2 <= 9);
        }
    }

    @Test
    public void testChooseDice() {
        int x = 0;
        for (int i = 0; i < 10; i++) {
            Dices dices = new Dices();
            assertEquals(dices.getDice1(), testDices.chooseDice("left"));
            assertEquals(dices.getDice2(), testDices.chooseDice("right"));
            assertEquals(0, testDices.chooseDice("no"));
            if (testDices.chooseDice("neither") != dices.getDice1()) {
                if (testDices.chooseDice("neither") != dices.getDice2()) {
                    x = 1;
                }
            }
        }
        assertEquals(1, x);
    }
}
