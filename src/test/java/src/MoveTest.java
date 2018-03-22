import org.junit.*;

public class MoveTest {
    @Test
    public void create_move_test() {
        Move move = new Move();
    }

    @Test
    public void get_move_effect_chance_test() {
        Move move = new Move();
        move.setEffectChance(50);
        Assert.assertEquals(move.getEffectChance(), 50);
    }
}
