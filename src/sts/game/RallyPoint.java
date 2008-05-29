package sts.game;

import java.awt.Graphics2D;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class RallyPoint extends GameObject
{
    public RallyPoint( int x, int y, Player owner )
    {
        super( x, y, Integer.MAX_VALUE, owner );
    }

    @Override
    public void draw( Graphics2D g )
    {
        ImageHandler.drawRalleyPoint( g, x, y, getOwningPlayer().getColor() );
    }

    @Override
    public String getName()
    {
        return "Rally point";
    }
}
