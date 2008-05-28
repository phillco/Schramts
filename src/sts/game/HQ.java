package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import sts.Local;
import sts.gui.ImageHandler;

/**
 * The player's chief building. Creates villagers.
 * @author Phillip Cohen
 */
public class HQ extends ProductionBuilding
{
    public HQ( int x, int y, Player player )
    {
        super( x, y, 1200, 750, player );
        giveableCommands = new Command[1];
        giveableCommands[0] = new Command( "Create villager" );
    }

    @Override
    public void draw( Graphics2D g )
    {
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );

        ImageHandler.drawHQ( g, getX(), getY(), c );
    }

    @Override
    protected void doCreation( Command type )
    {
        if ( type == giveableCommands[0] )
        {
            getOwningPlayer().giveObject( new Villager( getX(), getY() - 10, getOwningPlayer() ) );
        }
    }

    @Override
    public boolean isClickContained( int x, int y )
    {
        return isClickContainedInRectangle( this, x, y, 64, 64 );
    }

    @Override
    public String getName()
    {
        return "HQ";
    }
}
