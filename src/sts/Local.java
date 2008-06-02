package sts;

import java.util.HashSet;
import java.util.Iterator;
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
    
    private static int viewingX = 0,  viewingY = 0;

    public static void setUp( Game gameInstance, Player localPlayerInstance )
    {
        game = gameInstance;
        localPlayer = localPlayerInstance;
        window = new MainWindow();
    }

    public static Game getGame()
    {
        return Game.getInstance();
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
        Iterator<GameObject> i = selectedObjects.iterator();
        if ( i.hasNext() )
            return i.next();
        else
            return null;
    }

    public static boolean isObjectSelected()
    {
        return ( selectedObjects.size() > 0 );
    }

    public static void setSelectedObjects( Set<GameObject> selectedObjects )
    {
        Local.selectedObjects = selectedObjects;
    }

    public static int getViewingX()
    {
        return viewingX;
    }

    public static int getViewingY()
    {
        return viewingY;
    }

    public static void setViewingX( int viewingX )
    {
        Local.viewingX = viewingX;
    }

    public static void setViewingY( int viewingY )
    {
        Local.viewingY = viewingY;
    }
    
    
}
