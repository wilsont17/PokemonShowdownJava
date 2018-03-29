import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class InstructionsGuiTest {
    static PokemonShowdownMainGui h;
    static InstructionsGui s;
    Robot r;
    @BeforeClass
    public static void initializeGameGui() {
        try {
            h = new PokemonShowdownMainGui();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        s = new InstructionsGui(h, "test");
    }
    @Test
    public void testEnterFunctionality() throws AWTException {
        s.done.doClick();
    }
    @Test
    public void testExitClause() {
        Assert.assertEquals(3,s.f.getDefaultCloseOperation());
    }
}
