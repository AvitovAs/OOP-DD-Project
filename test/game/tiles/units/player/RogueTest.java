package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.enemies.Monster;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class RogueTest {

    private Rogue rogue;
    private DummyMsg msg;
    private GameBoard board;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        @Override public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        rogue = new Rogue("Rogue", 10, 5, 100, 100, 1, 1, msg, 20);
        Tile[][] arr = new Tile[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arr[i][j] = new Empty(i, j);
            }
        }
        arr[1][1] = rogue;
        board = new GameBoard(arr);
    }

    @Test
    public void testSpecialAbilityNotEnoughEnergy() {
        rogue = new Rogue("Rogue", 10, 5, 100, 100, 1, 1, msg, 200);
        rogue.specialAbility(board);
        assertEquals("Rogue tried to cast Fan of Knives but has only 100/200 energy.",
                msg.msgs.get(0));
    }

    @Test
    public void testSpecialAbilitySuccessMessage() {
        msg.msgs.clear();
        rogue.specialAbility(board);
        assertTrue(msg.msgs.get(0).contains("has used its special ability Fan of Knives."));
    }

    @Test
    public void testFanOfKnivesAttacksAllAdjacentEnemies() {
        Monster m1 = new Monster("A", 0, 0, 1, 1, 'A', 1, 0, 1, 5, msg);
        Monster m2 = new Monster("B", 0, 0, 1, 1, 'B', 2, 1, 1, 5, msg);
        board.setTile(1, 0, m1);
        board.setTile(2, 1, m2);
        msg.msgs.clear();
        rogue.specialAbility(board);
        long combatMsgs = msg.msgs.stream()
                .filter(s -> s.contains("engaged in combat"))
                .count();
        assertEquals(2, combatMsgs);
    }

    @Test
    public void testSpecialAbilityRemovesEnergy() {
        msg.msgs.clear();
        // starting energy = 100, cost = 20
        rogue.specialAbility(board);
        msg.msgs.clear();
        rogue.status();
        String status = msg.msgs.get(0);
        assertTrue(status.contains("Energy: 80/100"));
    }

    @Test
    public void testOnTurnRegenDoesNotBlock() {
        // simulate the "e" command so Player.onTurn returns immediately
        InputStream oldIn = System.in;
        System.setIn(new ByteArrayInputStream("e\n".getBytes()));
        try {
            msg.msgs.clear();
            rogue.onTurn(board);
            msg.msgs.clear();
            rogue.status();
            String status = msg.msgs.get(0);
            assertTrue(status.contains("Energy: 90/100"));
        } finally {
            System.setIn(oldIn);
        }
    }

    @Test
    public void testFanOfKnivesDoesNotHitWallsOrEmpty() {
        Wall w = new Wall(1, 0);
        board.setTile(1, 0, w);
        board.setTile(2, 1, new Empty(2, 1));
        msg.msgs.clear();
        rogue.specialAbility(board);
        assertSame(w, board.getTile(1, 0));
        assertTrue(board.getTile(2, 1) instanceof Empty);
    }

    @Test
    public void testEnergyRegenAfterSpecialAndOnTurn() {
        System.setIn(new java.io.ByteArrayInputStream("z\n".getBytes()));
        rogue.specialAbility(board);
        msg.msgs.clear();
        rogue.onTurn(board);
        msg.msgs.clear();
        rogue.status();
        String status = msg.msgs.get(0);
        assertTrue(status.contains("Energy: 90/100"));
        System.setIn(System.in);
    }


    @Test
    public void testLevelUpResetsEnergy() {
        rogue.specialAbility(board);
        msg.msgs.clear();
        rogue.addExperience(rogue.getReqExp());
        msg.msgs.clear();
        rogue.status();
        String status = msg.msgs.get(0);
        assertTrue(status.contains("Energy: 100/100"));
    }
}
