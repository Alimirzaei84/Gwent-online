package controller;

import org.junit.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class CardControllerTest {


    @Test
    public void unitTest() {
        try {
            CardController.load_data();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(CardController.numNotPassed);
        assertEquals(0, CardController.numNotPassed);
    }

    @Test
    public void specialTest() {
        try {
            CardController.load_data();
        } catch (Exception e) {}
        System.out.println(CardController.specials);
    }


}