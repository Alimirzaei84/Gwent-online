package controller;

import model.role.Card;
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


    @Test
    public void UnknownCards() {
        /*
        * triss
        * vildkaarl
        * young vildkaarl
        * vesemir
        * */

        Card trisCard = CardController.createCardWithName("triss merigold");
        if (CardController.imagePath.containsKey(trisCard.getName())) {
            System.out.println("true");
        }
        String s = CardController.imagePath.get(trisCard.getName());
        System.out.println(s);
        System.out.println(trisCard);
    }
}