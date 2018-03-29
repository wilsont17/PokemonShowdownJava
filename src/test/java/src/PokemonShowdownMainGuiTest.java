import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class PokemonShowdownMainGuiTest {
   static PokemonShowdownMainGui h;
    @BeforeClass
    public static void initializeGameValues() {
        try {
            h = new PokemonShowdownMainGui();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stabResult_test(){
        Move move = new Move();
        move.setDmgType(10);
        Pokemon pokemon = new Pokemon();
        pokemon.addType("fire");
        PokemonShowdownMainGui gui = new PokemonShowdownMainGui("test");
        Assert.assertEquals(1.5, gui.stabResult(pokemon, move), .1);
    }

    @Test
    public void testDefaultCloseOperation(){
        Assert.assertEquals(3, h.jfrm.getDefaultCloseOperation());

    }

    @Test
    public void
    givenNonRandom1v1_whenGivenPokemonIndexOf24_PokemonIsPikachu() throws AWTException {
        Robot robot = new Robot();
        RunThis.main(null);
        robot.delay(5000);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_1);
        robot.keyRelease(KeyEvent.VK_1);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(5000);
        Assert.assertEquals("pikachu", RunThis.gui.p1Pokemon.get(0).getName());
    }

}