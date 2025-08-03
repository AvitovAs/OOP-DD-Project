package view.parser;

import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_component.Wall;
import game.tiles.board_component.Empty;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;
import game.tiles.units.player.Mage;
import game.tiles.units.player.Player;
import game.tiles.units.player.Rogue;
import game.tiles.units.player.Warrior;

import java.util.Scanner;

public class TileFactory {
    private static MessageCallback msg;

    public TileFactory(MessageCallback msg) {
        this.msg = msg;
    }

    public static Tile create(char c, int row, int col) {
        return switch (c) {
            case '#' -> new Wall(row, col);
            case '.' -> new Empty(row, col);

            //case '@' -> new Warrior("Jon Snow", 30, 4, 300, 300, row, col, msg, 3);
            //case '@' -> new Mage("Melisandre", 5, 1, 100, 100, row, col, msg, 300, 30, 15, 5, 6);
            //case '@' -> new Rogue("Bronn", 35, 3, 250, 250, row, col, msg, 50);

            case 's' -> new Monster("Lannister Solider", 8, 3, 80, 80, c, row, col, 3, 25, msg);
            case 'k' -> new Monster("Lannister Knight", 14, 8, 200, 200, c, row, col, 4, 50, msg);
            case 'q' -> new Monster("Queen’s Guard", 20, 15, 400, 400, c, row, col, 5, 100, msg);
            case 'z' -> new Monster("Wright", 30, 15, 600, 600, c, row, col, 3, 100, msg);
            case 'b' -> new Monster("Bear-Wright", 75, 30, 1000, 1000, c, row, col, 4, 250, msg);
            case 'g' -> new Monster("Giant-Wright", 100, 40, 1500, 1500, c, row, col, 5, 500, msg);
            case 'w' -> new Monster("White Walker", 150, 50, 2000, 2000, c, row, col, 6, 1000, msg);
            case 'M' -> new Monster("The Mountain", 60, 25, 1000, 1000, c, row, col, 6, 500, msg);
            case 'C' -> new Monster("Queen Cersei", 10, 10, 100, 100, c, row, col, 1, 1000, msg);
            case 'K' -> new Monster("Night’s King", 300, 150, 5000, 5000, c, row, col, 8, 5000, msg);

            case 'B' -> new Trap("Bonus Trap", 1, 1, 1, 1, c, row, col, 1, 5, 250, msg);
            case 'Q' -> new Trap("Queen’s Trap", 50, 10, 250, 250, c, row, col, 3, 7, 100, msg);
            case 'D' -> new Trap("Death Trap", 100, 20, 500, 500, c, row, col, 1, 10, 250, msg);

            default -> new Empty(row, col);
        };
    }
}
