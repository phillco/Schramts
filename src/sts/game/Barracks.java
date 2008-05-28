/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.Graphics2D;
import sts.gui.ImageHandler;

/**
 *
 * @author AK90454
 */
public class Barracks extends ProductionBuilding
{
    public Barracks( int x, int y, Player player )
    {
        super( x, y, 800, 500, player );
    }

    @Override
    protected void doCreation( Command type )
    {
        //switch ( (ProductionOption) type )
        {
            //  case NEW_WARRIOR:
            getOwningPlayer().giveObject( new Infantry( 0, 0, 0, 0, null ) );
        //break;
        }
    }

    @Override
    public void draw( Graphics2D g )
    {
        if ( isBuilt() )
            ImageHandler.drawBarracks( g, getX(), getY(), getOwningPlayer().getColor() );
        else
            ImageHandler.drawConstructionSite( g, getX(), getY(), getOwningPlayer().getColor() );
    }

    public enum ProductionOption
    {
        NEW_WARRIOR( 300, "Create one new warrior" );

        final int ticksToCreate;

        final String description;

        private ProductionOption( int ticksToCreate, String description )
        {
            this.ticksToCreate = ticksToCreate;
            this.description = description;
        }
    }

    @Override
    public String getName()
    {
        return "Barracks";
    }
}
