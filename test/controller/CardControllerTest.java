package controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardControllerTest {


    @Before
    public void setUp() throws Exception {
        CardController.load_data();
    }

    @Test
    public void unitTest() {
        System.out.println(CardController.numNotPassed);
        assertEquals(0, CardController.numNotPassed);
    }

    @Test
    public void specialTest() {
        System.out.println(CardController.specials);
    }

    @Test
    public void leaderTest() {
    }


}