import org.junit.Test;

import javax.swing.*;
import java.io.FileNotFoundException;

public class RunThisTest {
    static {
        System.setProperty("java.awt.headless", "false");
        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
    }
    @Test
    public void testBuild() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try
                {
                    new PokemonShowdownMainGui();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
