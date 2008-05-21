package sts.game;

import java.awt.Graphics2D;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class Infantry extends Unit
{
    public Infantry( int x, int y, int dx, int dy, Player owner )
    {
        super( x, y, dx, dy, owner );
    }

    @Override
    public void act()
    {

    }

    public void attack( GameObject other )
    {

    }

    @Override
    public void draw( Graphics2D g )
    {
        ImageHandler.drawInfantry( g, getX(), getY(), getOwningPlayer().getColor() );
    }

    @Override
    public int getMaxSpeed()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
