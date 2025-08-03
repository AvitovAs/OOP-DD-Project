package game.callbacks;

import game.tiles.units.Unit;

public interface DeathCallback {
    void deathAnnoucement(Unit killer, Unit dead);
}
