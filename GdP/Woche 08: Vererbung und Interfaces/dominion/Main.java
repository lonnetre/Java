package dominion;

import gdi.game.dominion.BaseGame;
import gdi.game.dominion.DominionInterface;
import gdi.game.dominion.DominionTileManagerInterface;

public class Main_L {
    public static void main(String[] args) {
        DominionTileManagerInterface manager = new DominionTileManager_L(5, 7);
        DominionInterface dominion = new Dominion_L(manager);
        BaseGame game = new BaseGame(args, dominion);

        game.run();
    }
}
