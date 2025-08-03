package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_component.Empty;
import game.tiles.units.enemies.Monster;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarriorTest {

    private Warrior warrior;
    private DummyMsg msg;
    private GameBoard board;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        @Override public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        warrior = new Warrior("Conan", 10, 2, 30, 100, 1, 1, msg, 3);
        Tile[][] arr = new Tile[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                arr[i][j] = new Empty(i, j);
        arr[1][1] = warrior;
        board = new GameBoard(arr);
    }

    @Test
    public void testHealInSpecialAbility() {
        warrior.getHp().setAmount(10);
        msg.msgs.clear();
        warrior.specialAbility(board);
        assertEquals(10 + warrior.getDefense() * 10, warrior.getHp().getAmount());
    }

    @Test
    public void testSpecialAbilityNoEnemiesMessage() {
        msg.msgs.clear();
        warrior.specialAbility(board);
        assertTrue(msg.msgs.get(1).contains("special ability hit nothing"));
    }

    @Test
    public void testSpecialAbilityKillsWeakEnemy() {
        Monster m = new Monster("Orc", 0, 0, 10, 10, 'O', 1, 2, 3, 50, msg);
        board.setTile(1, 2, m);
        msg.msgs.clear();
        warrior.specialAbility(board);
        assertSame(warrior, board.getTile(1, 2));
        assertEquals('.', board.getTile(1, 1).getCharacter());
    }

    @Test
    public void testCooldownMessageWhenOnCooldown() {
        warrior.specialAbility(board);
        msg.msgs.clear();
        warrior.specialAbility(board);
        assertTrue(msg.msgs.get(0).startsWith("Conan tried to cast Avenger"));
        assertTrue(msg.msgs.get(0).contains("turns left"));
    }

    @Test
    public void testOnTurnDecrementsCooldown() {
        InputStream oldIn = System.in;
        System.setIn(new ByteArrayInputStream("e\n".getBytes()));
        try {
            warrior.specialAbility(board);
            assertEquals(3, warrior.getRemaining_CD());
            warrior.onTurn(board);
            assertEquals(2, warrior.getRemaining_CD());
        } finally {
            System.setIn(oldIn);
        }
    }

    @Test
    public void testToStringBoardRepresentationUnaffected() {
        String repr = board.toString();
        assertTrue(repr.contains("@"));
    }
}
