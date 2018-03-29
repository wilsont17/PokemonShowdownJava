import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

}