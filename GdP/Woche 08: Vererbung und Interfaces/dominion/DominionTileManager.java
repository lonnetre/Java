package dominion;

import gdi.game.dominion.DominionTile;
import gdi.game.dominion.DominionTileManagerInterface;
import gdi.game.map.MapWorld;

public class DominionTileManager_L implements DominionTileManagerInterface {

    private final DominionTile[][] map;

    public DominionTileManager_L(int cols, int rows) {
        this.map = new DominionTile[cols][rows];
    }

    @Override
    public int getNumRows() {
        if (map.length == 0) {
            return 0;
        }
        return map[0].length;
    }

    @Override
    public int getNumColumns() {
        return map.length;
    }


    @Override
    public DominionTile getTile(int col, int row) {
        if (!isValid(col, row))
            return null;

        return map[col][row];
    }


    @Override
    public boolean isValid(int col, int row) {
        if (row < 0 || col < 0 || col >= getNumColumns() || row >= getNumRows())
            return false;
        return true;
    }


    @Override
    public DominionTile[] getColumn(int col) {
        if (col < 0 || col >= map.length)
            return null;
        return map[col];
    }


    @Override
    public void setupMapTiles(MapWorld world) {
        for (int c = 0; c < getNumColumns(); c++) {
            for (int r = 0; r < getNumRows(); r++) {
                map[c][r] = new DominionTile(world, c, r);
            }
        }
    }
}
