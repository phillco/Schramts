/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import sts.Local;
import sts.Util;
import sts.gui.ImageHandler;

/**
 *
 * @author AK90454
 */
public class Barracks extends ProductionBuilding
{
    public Barracks( int x, int y, Player player )
    {
        super( x, y, 48, 48, 300, 500, player );
    }

    @Override
    public void build()
    {
        super.build();
        if ( isBuilt() )
        {
            productionCommands = new ProductionCommand[1];
            productionCommands[0] = new ProductionCommand( "Create infantry", 50, 150, ImageHandler.getInfantry() );
        }
    }

    @Override
    public void draw( Graphics2D g )
    {
        super.draw( g );
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );
        if ( isBuilt() )
            ImageHandler.drawBarracks( g, getX(), getY(), c );
        else
            ImageHandler.drawConstructionSite( g, getX(), getY(), c );
    }

    @Override
    public String getName()
    {
        return "Barracks";
    }

    @Override
    protected void doCreation( ProductionCommand type )
    {
        if ( type == productionCommands[0] )
            createAndAssignUnit( new Infantry( getX() + Util.getRandomGenerator().nextInt( 48 ), getY() + 50, 0, 0, getOwningPlayer() ) );
    }

    @Override
    public boolean isClickContained( int x, int y, int width, int height )
    {
        return isClickContainedInRectangle( this, x, y, 48 + width, 48 + height );
    }
}
