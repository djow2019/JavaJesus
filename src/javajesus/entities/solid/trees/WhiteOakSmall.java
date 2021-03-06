package javajesus.entities.solid.trees;

import javajesus.entities.Entity;
import javajesus.graphics.Sprite;
import javajesus.level.Level;

public class WhiteOakSmall extends Tree {
    /**
     * Small White Oak Tree ctor()
     * 
     * @param level - level it is on
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public WhiteOakSmall(Level level, int x, int y) {
        super(level, x, y, Sprite.WHITE_OAK_SMALL, 12, 8, 10);
    }

    @Override
    public byte getId(){
        return Entity.WHITE_OAK_SMALL;
    }

}
