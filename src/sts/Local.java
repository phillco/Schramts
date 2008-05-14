package sts;

import sts.game.Player;

/**
 *
 * @author Phillip Cohen
 */
public class Local
{

    /**
     * The player at this computer.
     */
    private static Player localPlayer;

    public static Player getLocalPlayer()
    {
        return localPlayer;
    }

    static void setLocalPlayer( Player localPlayer )
    {
        Local.localPlayer = localPlayer;
    }
    
    
    
}
