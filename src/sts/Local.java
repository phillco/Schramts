package sts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import sts.game.Game;
import sts.game.GameObject;
import sts.game.HQ;
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

    private static int viewingX = 0,  viewingY = 0,  viewingWidth = 0,  wiewingHeight = 0;

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

    public static void setSelectedObject( GameObject go )
    {
        selectedObjects = new HashSet<GameObject>();
        selectedObjects.add( go );

      //  if ( go.getLocation().getX() - getViewingX() > getViewingWidth() || go.getLocation().getX() - getViewingX() <= 0 )
            setViewingX( go.getX() - getViewingWidth() / 2 );

      //  if ( go.getLocation().getY() - getViewingY() > getWiewingHeight() || go.getLocation().getY() - getViewingY() <= 0 )
            setViewingY( go.getY() - getWiewingHeight() / 2 );
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
        Local.viewingX = Math.max( 0, Math.min( viewingX, Game.getInstance().getLevelWidth() - getViewingWidth() ) );
    }

    public static void setViewingY( int viewingY )
    {
        Local.viewingY = Math.max( 0, Math.min( viewingY, Game.getInstance().getLevelHeight() - getWiewingHeight() ) );
    }

    public static int getViewingWidth()
    {
        return viewingWidth;
    }

    public static int getWiewingHeight()
    {
        return wiewingHeight;
    }

    public static void setViewingArea( int viewingWidth, int viewingHeight )
    {
        Local.viewingWidth = viewingWidth;
        Local.wiewingHeight = viewingHeight;
    }
}
