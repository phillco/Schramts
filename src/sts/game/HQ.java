package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import sts.Local;
import sts.Util;
import sts.gui.ImageHandler;

/**
 * The player's chief building. Creates villagers.
 * @author Phillip Cohen
 */
public class HQ extends ProductionBuilding
{
    private int timeUntilNextShot;

    private double range = 75;
    
    public HQ( int x, int y, Player player, boolean preBuilt )
    {
        super( x, y, 64, 64, 1200, 750, player );

        if ( preBuilt )
            timeToBuild = 0;
        productionCommands = new ProductionCommand[1];
        productionCommands[0] = new ProductionCommand( "Create villager", 20, 100, ImageHandler.getVillager() );
        
        timeUntilNextShot = 2;
        
        if ( preBuilt )
            setHealth( 700 );
    }
    
    @Override
    public void act()
    {
        super.act();
        if(timeUntilNextShot>0)
            --timeUntilNextShot;
        shootAtAnyoneInRange();
    }

    private void attack(GameObject other)
    {
        if ( other == null || timeUntilNextShot > 0)
            return;

        getOwningPlayer().giveObject( new Bullet( getLocation().getX(), getLocation().getY(), this, other ) );
        other.changeHealth( -1 );//don't tell AI, that's only for offensive moves
    }
    
    private void shootAtAnyoneInRange()
    {
        ArrayList<GameObject> inRange = new ArrayList<GameObject>();
        for ( Player p : Local.getGame().getPlayers() )
        {
            if ( p == getOwningPlayer() )
                continue;//don't shoot at friendly units

            for ( GameObject go : p.getOwnedObjects() )
            {
                if ( Location.getDistance( this.getLocation(), go.getLocation() ) < range )
                    inRange.add( go );
            }
        }
        attack( getBestTarget( inRange ) );
        return;
    }

    public GameObject getBestTarget( ArrayList<GameObject> possible )
    {
        if ( possible.isEmpty() )
            return null;//don't bother.
        //shoot at infantry first; they shoot back

        for ( GameObject go : possible )
        {
            if ( go instanceof Infantry )
            {
                return go;
            }
        }
        //shoot at villagers second, they run away
        for ( GameObject go : possible )
        {
            if ( go instanceof Villager )
            {
                return go;
            }
        }
        //don't shoot at anything else, otherwise you'd shoot at bullets.
        return null;
    }


    @Override
    public void draw( Graphics2D g )
    {
        super.draw( g );
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );

        ImageHandler.drawHQ( g, getX(), getY(), c );
    }

    @Override
    protected void doCreation( ProductionCommand type )
    {
        if ( type == productionCommands[0] )
            createAndAssignUnit( new Villager( getX() + 30 + Util.getRandomGenerator().nextInt( 30 ) - 15, getY() + 54, getOwningPlayer() ) );
    }

    @Override
    public boolean isClickContained( int x, int y, int width, int height )
    {
        return isClickContainedInRectangle( this, x, y, 64 + width, 64 + height );
    }

    @Override
    public String getName()
    {
        return "HQ";
    }

    @Override
    public Location getLocation()
    {
        return new Location( getX() + 32, getY() + 44 );
    }
}
