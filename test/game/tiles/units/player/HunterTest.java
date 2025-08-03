package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.board_component.Empty;
import game.tiles.Tile;
import game.tiles.units.enemies.Monster;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HunterTest {

    private Hunter hunter;
    private DummyMsg msg;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        @Override public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        // give hunter a small range=1 so tests are easier
        hunter = new Hunter("Ygritte", 30, 2, 220, 220, 0, 0, msg, 1);
    }

    @Test
    public void testSpecialAbilityNotEnoughArrows() throws Exception {
        // force arrows = 0
        Field arrowsField = Hunter.class.getDeclaredField("arrows");
        arrowsField.setAccessible(true);
        arrowsField.setInt(hunter, 0);

        hunter.specialAbility(null);
        assertEquals("Ygritte ran out of arrows so he cannot Shoot.",
                msg.msgs.get(0));
    }

    @Test
    public void testSpecialAbilityNoEnemyInRange() {
        msg.msgs.clear();
        // empty board, no monsters
        GameBoard board = new GameBoard(new Tile[][]{{ new Empty(0,0) }});
        hunter.specialAbility(board);
        assertEquals("Ygritte tried to use Shoot but no enemy was found in range.",
                msg.msgs.get(0));
    }

    @Test
    public void testSpecialAbilityRemovesHitMonster() {
        msg.msgs.clear();
        // place a single Monster at (0,1), within range=1
        Monster m = new Monster("Orc", 1, 0, 1, 1, 'O', 0, 1, 5, 10, msg);
        Tile[][] grid = {
                { hunter, m }
        };
        GameBoard board = new GameBoard(grid);

        hunter.specialAbility(board);
        // monster should be removed and replaced by Empty
        assertTrue(board.getTile(0,1) instanceof Empty);
    }

    @Test
    public void testLevelUpHunterBonusesMessage() {
        msg.msgs.clear();
        // grant exactly REQ_EXP so hunter levels 1→2 and gets +20 arrows
        hunter.addExperience(hunter.getReqExp());
        assertEquals(2, msg.msgs.size());
        assertTrue(msg.msgs.get(1).contains("+20 arrows"));
    }

    @Test
    public void testStatusMessageIncludesArrowsCount() {
        msg.msgs.clear();
        hunter.status();
        String status = msg.msgs.get(0);
        assertTrue(status.contains("Arrows: 10"));
    }

    @Test
    public void testSpecialAbilityIgnoresMonstersOutOfRange() {
        msg.msgs.clear();

        // monster at (0,2), range=1 so out of range
        Monster m = new Monster("Orc", 1, 0, 1, 1, 'O', 0, 2, 5, 10, msg);

        Tile[][] grid = {
                { hunter, new Empty(0,1), m }
        };
        GameBoard board = new GameBoard(grid);

        hunter.specialAbility(board);

        // col 0 is still your hunter
        assertTrue(board.getTile(0,0) instanceof Hunter);
        // col 2 is still your monster
        assertTrue(board.getTile(0,2) instanceof Monster);
    }

}
