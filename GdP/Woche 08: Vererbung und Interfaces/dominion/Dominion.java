package dominion;

import gdi.game.dominion.*;

import java.awt.*;

public class Dominion_L implements DominionInterface {

    private final DominionTileManagerInterface tileManager;
    private Figure player;
    private Figure npc;

    public Dominion_L(DominionTileManagerInterface manager) {
        this.tileManager = manager;
    }

    @Override
    public DominionTileManagerInterface getDominionTileManager() {
        return tileManager;
    }

    @Override
    public Figure getPlayer() {
        return player;
    }

    @Override
    public Figure getNpc() {
        return npc;
    }


    @Override
    public void setupWorld(BaseGame game) {
        player = new Figure(game, Color.BLUE, false);
        npc = new Figure(game, Color.RED, true);

        setupMap();
    }

    private void setupMap() {
        for (int c = 0; c < tileManager.getNumColumns(); c++) {
            for (int r = 0; r < tileManager.getNumRows(); r++) {
                DominionTile tile = tileManager.getTile(c, r);
                tile.setOwner(Math.random() < 0.5 ? player : npc);
            }
        }
    }


    @Override
    public void update(BaseGame game, double time) {
        if (!npc.isWalking() && time >= npc.getDepartureTime()) {
            chooseTarget(npc);
        }

        Figure winner = checkForWinner();
        if (winner != null) {
            game.setWinner(winner);
        }
    }

    /**
     * Checks if all tiles are owned by either the player or the NPC and
     * returns the respective winner.
     * @return the winner of the game or <code>null/code> if neither the
     * player nor the NPS is owning all tiles
     */
    private Figure checkForWinner() {
        boolean allTilesOwnedByPlayer = true;
        boolean allTilesOwnedByNPC = true;

        for (int c = 0; c < tileManager.getNumColumns(); c++) {
            for (int r = 0; r < tileManager.getNumRows(); r++) {
                if (tileManager.getTile(c, r).isPropertyOf(player)) {
                    allTilesOwnedByNPC = false;
                } else if (tileManager.getTile(c, r).isPropertyOf(npc)) {
                    allTilesOwnedByPlayer = false;
                }

                if (!allTilesOwnedByPlayer && !allTilesOwnedByNPC) {
                    return null;
                }
            }
        }

        if (allTilesOwnedByPlayer) {
            return player;
        } else if (allTilesOwnedByNPC) {
            return npc;
        }

        return null;
    }



    @Override
    public void chooseTarget(Figure figure) {
        DominionTile target = findClosestUnownedTile(figure);
        figure.moveTo(target);
    }

    private DominionTile findClosestUnownedTile(Figure figure) {
        final int figureRow = figure.getTile().getRow();
        final int figureColumn = figure.getTile().getColumn();

        DominionTile target = null;
        double minDist = Double.MAX_VALUE;

        // Find the closest tile to the figure that is not already owned by it
        for (int c = 0; c < tileManager.getNumColumns(); c++) {
            for (int r = 0; r < tileManager.getNumRows(); r++) {

                if (c == figureColumn && r == figureRow) {
                    // This is the tile at the figure's current position
                    continue;
                }

                DominionTile tile = tileManager.getTile(c, r);
                if (tile.isPropertyOf(figure)) {
                    // This tile is already owned by the figure
                    continue;
                }

                int dc = figureColumn - c;
                int dr = figureRow - r;
                double dist = Math.sqrt(dc * dc + dr * dr);

                if (dist < minDist) {
                    minDist = dist;
                    target = tile;
                }
            }
        }

        return target;
    }

    @Override
    public void clickedTile(DominionTile tile) {
        if (!player.isWalking()) {
            player.moveTo(tile);
        }
    }

    @Override
    public void reachedTarget(Figure figure, double time) {
        DominionTile tile = figure.getTile();
        if (tile != null) {
            tile.setOwner(figure);
        }

        if (figure.isNpc()) {
            figure.setDepartureTime(time + figure.getPauseDuration());
        }
    }
}