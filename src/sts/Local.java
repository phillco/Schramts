package sts;

import java.util.HashSet;
import java.util.Set;
import sts.game.Game;
import sts.game.GameObject;
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
    
    private static Set<GameObject> selectedObjects = new HashSet<GameObject>();

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

    public static Set<GameObject> getSelectedObjects()
    {
        return selectedObjects;
    }
    
    /**
     * Returns the first of the user's selected objects.
     * @see Local#getSelectedObjects()
     */
    public static GameObject getSelectedObject()
    {
        return selectedObjects.iterator().next();
    }
    
    public static boolean isObjectSelected()
    {
        return (selectedObjects.size() > 0);
    }
    
    

    public static void setSelectedObjects( Set<GameObject> selectedObjects )
    {
        Local.selectedObjects = selectedObjects;
    }
    
    
}
