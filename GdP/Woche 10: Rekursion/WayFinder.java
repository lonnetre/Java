import gdi.game.map.MapTile;
import gdi.maze.Maze;
import gdi.maze.MapPath;

public class WayFinder_L {

    private static MapPath path;

    /**
     * Finds the path from the player's position in the maze to the target
     * @param maze a maze with a target, obstacles and a player
     * @return the path from the player to the target, without detours,
     * or <code>null</code> if no path was found
     */
    public static MapPath findPathToTarget(Maze maze) {
        if (maze == null || maze.getPlayerPosition() == null) {
            return null;
        }

        // Add the start tile to the solution, since the player is already there
        MapTile startTile = maze.getPlayerPosition();
        maze.putDownMarker(startTile);
        path = new MapPath();
        path.add(startTile);

        boolean success = findPathToTarget(maze, startTile);
        if (!success) {
            return null;
        }

        // Prepare the return value for the method caller
        // and clean up the internal state of the class
        MapPath solution = WayFinder_L.path;
        WayFinder_L.path = null;

        return solution;
    }

    /**
     * Helper method to recursively find the path from the current tile to the maze's target.
     * Adjusts the class attribute accordingly.
     * @param maze a maze with a target, obstacles and a player
     * @param current the tile where the player is currently located
     * @return <code>true</code> if the target was found or <code>false</code> otherwise
     */
    private static boolean findPathToTarget(Maze maze, MapTile current) {

        // Base case: Found target
        if (maze.reachedTarget()) {
            return true;
        }

        // Try all neighboring tiles
        MapTile[] neighbors = maze.getNeighbours(current, true);
        for (int i = 0; i < neighbors.length; i++) {
            MapTile tile = neighbors[i];

            // Only visit tiles that haven't been marked yet
            if (maze.hasMarker(tile)) {
                continue;
            }

            // Send player to the tile
            boolean playerMovedSuccessfully = maze.sendPlayer(tile);
            if (!playerMovedSuccessfully) {
                continue;
            }

            // Mark tile and add to the current path solution
            maze.putDownMarker(tile);
            path.add(tile);

            // Search recursively
            boolean targetFound = findPathToTarget(maze, tile);
            if (targetFound) {
                // The search was successful
                return true;
            }

            // Backtracking: reset state
            // - send figure back to where it came from
            // - remove tile from path that led to a dead end
            maze.sendPlayer(current);
            path.remove(path.size() - 1);
        }

        // The target could not be found
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        Maze maze = new Maze(args);

        MapPath path = findPathToTarget(maze);
        System.out.println("Found target: " + (path != null));

        if (path != null) {
            maze.highlightPath(path);
        }
    }
}
