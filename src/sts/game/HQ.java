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
        giveableCommands[0] = new Command( "Create villager", 20, ImageHandler.getVillager() );
        
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
            getOwningPlayer().giveObject( new Villager( getX() + 30, getY() + 54, getOwningPlayer() ) );
        }
        else if (type == giveableCommands[1])
            getOwningPlayer().giveObject( new Infantry( getX(), getY() - 10, 0, 0, getOwningPlayer() ) );
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

    @Override
    public Location getLoc()
    {
        return new Location( getX() + 32, getY() + 44 );
    }
}
