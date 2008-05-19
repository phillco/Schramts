package sts;

import sts.game.Game;
import sts.game.Player;
import sts.gui.MainWindow;

/**
 *
 * @author Phillip Cohen
 */
public class Local
{
    
    private static Game game;

    /**
     * The player at this computer.
     */
    private static Player localPlayer;

    private static MainWindow window;

    public static void setUp( Game gameInstance, Player localPlayerInstance )
    {
        game = gameInstance;
        localPlayer = localPlayerInstance;
        window = new MainWindow();
    }

    public static Game getGame()
    {
        return game;
    }

    public static Player getLocalPlayer()
    {
        return localPlayer;
    }

    public static MainWindow getWindow()
    {
        return window;
    }
}
