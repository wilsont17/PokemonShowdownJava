import org.junit.Test;

import javax.swing.*;
import java.io.FileNotFoundException;

public class RunThisTest {

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
