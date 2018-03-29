import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import org.junit.Assert.*;

public class PokemonShowdownMainGuiTest {

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
