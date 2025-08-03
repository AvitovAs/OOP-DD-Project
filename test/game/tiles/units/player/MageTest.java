package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_component.Empty;
import game.tiles.units.enemies.Monster;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MageTest {

    private Mage mage;
    private DummyMsg msg;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        @Override public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        mage = new Mage("Gandalf", 0, 0, 10, 10, 0, 0, msg,
                100, 30, 50, 1, 2);
    }

    @Test
    public void testSpecialAbilityNotEnoughMana() {
        mage.specialAbility(null);
        assertEquals("Gandalf tried to cast Blizzard but has only 25/30 mana.",
                msg.msgs.get(0));
    }

    @Test
    public void testSpecialAbilitySuccessMessage() {
        mage = new Mage("Gandalf", 0, 0, 10, 10, 0, 0, msg,
                120, 30, 50, 1, 2);
        msg.msgs.clear();
        mage.specialAbility(new GameBoard(new Tile[][]{{new Empty(0,0)}}));
        assertTrue(msg.msgs.get(1).contains("has used its special ability Blizzard."));
    }

    @Test
    public void testBlizzardRemovesHitEnemy() {
        mage = new Mage("Gandalf", 0, 0, 10, 10, 0, 0, msg,
                120, 30, 50, 1, 2);
        msg.msgs.clear();
        Monster m = new Monster("Orc", 0, 0, 1, 1, 'O', 0, 1, 5, 10, msg);
        Tile[][] arr = {
                { mage,      m },
                { new Empty(1,0), new Empty(1,1) }
        };
        GameBoard board = new GameBoard(arr);
        mage.specialAbility(board);
        assertTrue(board.getTile(0,1) instanceof Empty);
    }

    @Test
    public void testLevelUpMageBonusesMessage() {
        msg.msgs.clear();
        mage.addExperience(mage.getReqExp());
        assertEquals(2, msg.msgs.size());
        assertEquals(
                "Additional mage bonuses - Spell Damage: +20     Mana Capacity: +50",
                msg.msgs.get(1)
        );
    }

    @Test
    public void testStatusMessageIncludesManaAndSpellDamage() {
        msg.msgs.clear();
        mage.status();
        String status = msg.msgs.get(0);
        assertTrue(status.contains("Mana: 25/100"));
        assertTrue(status.contains("Spell Damage: 50"));
    }

    @Test
    public void testSpecialAbilityIgnoresEnemiesOutOfRange() {
        msg.msgs.clear();
        Monster m = new Monster("Orc", 0, 0, 1, 1, 'O', 0, 2, 5, 10, msg);
        Tile[][] arr = {
                { mage, m },
                { new Empty(1,0), new Empty(1,1) }
        };
        GameBoard board = new GameBoard(arr);
        mage.specialAbility(board);
        assertTrue(board.getTile(0,1) instanceof Monster);
    }
}
