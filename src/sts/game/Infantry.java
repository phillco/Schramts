package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import sts.Local;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class Infantry extends Unit
{
    private static int range = 50;

    private static int damage = 1;//one per timestep

    public Infantry( int x, int y, int dx, int dy, Player owner )
    {
        super( x, y, dx, dy, 150, owner );
    }

    @Override
    public void act()
    {
        arrived = false;//never assume that you've made it, it could have moved

        super.act();//move, if necessary
        //we can't shoot at our target, see if anyone else is around...

        if ( goal == null )
        {
            idleBehavior();
            return;//nobody to attack

        }
        if ( !( Location.getDistance( this.getLoc(), goal.getLoc() ) < range ) )
        {
            shootAtAnyoneInRange();
        }
        if ( goal.getHealth() <= 0 )//we win!
        {
            findNewTarget();
            return;
        }
        if ( goal.getOwningPlayer() != getOwningPlayer() )//enemy
        {
            if ( Location.getDistance( this.getLoc(), goal.getLoc() ) < range )
                attack( goal );
        }
        else // guarding

        {
            shootAtAnyoneInRange();
        }
    }

    public void attack( GameObject other )
    {
        other.changeHealth( -damage );
    }

    @Override
    public void draw( Graphics2D g )
    {
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );
        ImageHandler.drawInfantry( g, getX(), getY(), c );
    }

    @Override
    public int getMaxSpeed()
    {
        return 6;
    }

    @Override
    public String getName()
    {
        return "Infantry";
    }

    private void findNewTarget()
    {
        System.out.println( "finding new target" );
        Player p = goal.getOwningPlayer();
        Class old = goal.getClass();
        ArrayList<GameObject> sameType = new ArrayList<GameObject>();
        for ( GameObject go : p.getOwnedObjects() )
        {
            if ( go.getClass() == old && Location.getDistance( getLoc(), go.getLoc() ) < 8 * range )
                sameType.add( go );
        }
        if ( !sameType.isEmpty() )
            setGoal( sameType.get( 0 ) );
        else
            goal = null;
        System.out.println( "Target is " + goal );
    }

    private void idleBehavior()
    {
        shootAtAnyoneInRange();//don't go anywhere, but if anyone strays too close...

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
                if ( Location.getDistance( this.getLoc(), go.getLoc() ) < range )
                    inRange.add( go );
            }
        }
        switch ( inRange.size() )
        {
            case 0://nobody in range

                return;
            case 1://only one choice, avoid the mess below

                attack( inRange.get( 0 ) );
                return;
        }
        //shoot at infantry first; they shoot back
        for ( GameObject go : inRange )
        {
            if ( go instanceof Infantry )
            {
                attack( go );
                return;
            }
        }
        //shoot at villagers second, they run away
        for ( GameObject go : inRange )
        {
            if ( go instanceof Villager )
            {
                attack( go );
                return;
            }
        }
        //shoot at production buildings third.  This should be all that's left
        for ( GameObject go : inRange )
        {
            if ( go instanceof ProductionBuilding )
            {
                attack( go );
                return;
            }
        }
        //shoot at anything else, just in case you missed something
        attack( inRange.get( 0 ) );
        return;
    }

    @Override
    public boolean isClickContained( int x, int y )
    {
        return isClickContainedInRectangle( this, x, y, 8, 13 );
    }
}
