package game.tiles.units.enemies;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.board_component.Empty;
import game.tiles.Tile;
import game.tiles.units.player.Player;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TrapTest {

    private Trap trap;
    private DummyMsg msg;
    private GameBoard dummyBoard;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        @Override public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        trap = new Trap("X", 1, 1, 10, 10, 'X', 0, 0, 2, 3, 5, msg);
        dummyBoard = new GameBoard(new Tile[][]{{new Empty(0,0)}});
    }

    @Test
    public void initialVisibility() {
        assertEquals("X", trap.toString());
    }

    @Test
    public void visibilityCycle() {
        for (int i = 0; i < 2; i++) {
            trap.onTurn(new Empty(10,10), dummyBoard);
            assertEquals("X", trap.toString());
        }
        trap.onTurn(new Empty(10,10), dummyBoard);
        assertEquals(".", trap.toString());
        for (int i = 0; i < 2; i++) {
            trap.onTurn(new Empty(10,10), dummyBoard);
            assertEquals(".", trap.toString());
        }
        trap.onTurn(new Empty(10,10), dummyBoard);
        assertEquals("X", trap.toString());
    }

    @Test
    public void attackWhenPlayerInRange() {
        Player player = new Player("P", 0, 0, 10, 10, 1, 1, msg);
        Tile[][] arr = new Tile[2][2];
        arr[0][0] = trap;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                if (arr[i][j] == null)
                    arr[i][j] = new Empty(i,j);
        arr[1][1] = player;
        GameBoard board = new GameBoard(arr);

        trap.onTurn(player, board);

        assertFalse(msg.msgs.isEmpty());
        assertTrue(msg.msgs.get(0).contains("X engaged"));
        assertEquals("X", trap.toString());
    }

    @Test
    public void noAttackOutOfRange() {
        Player far = new Player("P", 0, 0, 10, 10, 3, 3, msg);
        trap.onTurn(far, dummyBoard);
        assertTrue(msg.msgs.isEmpty());
    }

    @Test
    public void onTurnDeadDoesNothing() {
        trap.getHp().setAmount(0);
        trap.onTurn(new Empty(10,10), dummyBoard);
        assertEquals("X", trap.toString());
        assertEquals(0, trap.getHp().getAmount());
    }
}
