import org.junit.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class SetupGuiTest {
    static {
        System.setProperty("java.awt.headless", "false");
        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
    }
    static PokemonShowdownMainGui h;
    static SetupGui s;
    Robot r;
    @BeforeClass
    public static void initializeGameValues() {
        try {
            h = new PokemonShowdownMainGui();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
   public void test1v1() {
        s = new SetupGui(h);
        s.p1NameInput.setText("Test");
        s.p2NameInput.setText("Test2");
        s.gameModes.setSelectedIndex(0);
        s.start.doClick();
        System.out.println("1v1 successfully Created!");
    }
    @Test
    public void test6v6() {
        s = new SetupGui(h);
        s.p1NameInput.setText("Test");
        s.p2NameInput.setText("Test2");
        s.gameModes.setSelectedIndex(1);
        s.start.doClick();
        System.out.println("6v6 successfully Created!");
    }
    @Test
    public void testEntertoStart() throws AWTException {
        s = new SetupGui(h);
        s.p1NameInput.setText("Test");
        s.p2NameInput.setText("Test2");
        r = new Robot();
        r.keyPress(KeyEvent.VK_ENTER);
    }
    @Test
    public void testDefaultCloseOperation(){
        Assert.assertEquals(3, h.jfrm.getDefaultCloseOperation());

    }
    @AfterClass
    public static void tearDown() {
        s = null;
        h = null;
    }
}
