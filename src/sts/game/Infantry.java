package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
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

    private int timeUntilNextShot;

    public Infantry( int x, int y, int dx, int dy, Player owner )
    {
        super( x, y, dx, dy, 12, 16, 150, owner );
        timeUntilNextShot = 3;
    }

    @Override
    public void act()
    {
        arrived = false;//never assume that you've made it, it could have moved
        super.act();

        // Reload.
        if ( timeUntilNextShot > 0 )
            --timeUntilNextShot;

        // No assignment, or we're guarding one of our units.
        if ( commandQueue.isEmpty() || commandQueue.peek().getObject() == null || commandQueue.peek().getObject().getOwningPlayer() == getOwningPlayer() )
        {
            shootAtAnyoneInRange();
            return;
        }

        // Target is dead - get a new one!
        if ( commandQueue.peek().getObject().getHealth() <= 0 )
        {
            findNewTarget();
            return;
        }

        // In range of our target - fire!
        if ( Location.getDistance( this.getLocation(), commandQueue.peek().getLocation() ) < range )
            attack( commandQueue.peek().getObject() );
        else
            shootAtAnyoneInRange();
    }

    public void attack( GameObject other )
    {
        if ( other == null || timeUntilNextShot > 0 )
            return;

        getOwningPlayer().giveObject( new Bullet( getX(), getY(), this, other ) );
        other.changeHealth( -damage, this );
    }

    @Override
    public void draw( Graphics2D g )
    {
        super.draw( g );
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
        Player p = commandQueue.peek().getObject().getOwningPlayer();
        Class old = commandQueue.peek().getObject().getClass();
        ArrayList<GameObject> sameType = new ArrayList<GameObject>();
        for ( GameObject go : p.getOwnedObjects() )
        {
            if ( go.getClass() == old && Location.getDistance( getLocation(), go.getLocation() ) < 8 * range )
                sameType.add( go );
        }
        if ( !sameType.isEmpty() )
            commandQueue.set( 0, new Command( false, sameType.get( 0 ) ) );
        else
            commandQueue.removeFirst();
    }

    @Override
    protected void calculateSpeed()
    {
        if ( commandQueue.peek().getObject() == null || commandQueue.peek().getObject().getOwningPlayer() != getOwningPlayer() || commandQueue.peek().isGround() )
        {
            super.calculateSpeed();
            return;
        }

        // Goal is an object belonging to the same player, circle around it.
        if ( Location.getDistance( getLocation(), commandQueue.peek().getObject().getLocation() ) < range - 8 )
        {
            super.calculateSpeed();
            dx *= -1;
            dy *= -1;
            return;
        }

        if ( Location.getDistance( getLocation(), commandQueue.peek().getObject().getLocation() ) > range + 8 )
        {
            super.calculateSpeed();
            return;
        }

        // [PC] ??
        double angle = Math.atan2( getY() - commandQueue.peek().getLocation().getY(), getX() - commandQueue.peek().getLocation().getX() ) + Math.PI;
        dx = ( getMaxSpeed() * Math.cos( angle ) ) * mySpeedRate;
        dy = ( getMaxSpeed() * Math.sin( angle ) ) * mySpeedRate;
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

    public GameObject getBestTarget( Collection<GameObject> possible )
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
        //shoot at production buildings third.  This should be all that's left
        for ( GameObject go : possible )
        {
            if ( go instanceof ProductionBuilding )
            {
                return go;
            }
        }
        //don't shoot at anything else, otherwise you'd shoot bullets.
        return null;
    }

    @Override
    public boolean isClickContained( int x, int y, int width, int height )
    {
        return isClickContainedInRectangle( this, x, y, 8 + width, 13 + height );
    }

    @Override
    public void giveCommand( Command c, boolean urgent )
    {
        if ( c.isGround() || c.getObject().getOwningPlayer() != Game.getInstance().getNature() )
            super.giveCommand( c, urgent );
    }

    @Override
    public Command processGroupCommand( GroupCommand command )
    {
        // Just move there.
        if ( command.getObjects().isEmpty() )
            return super.processGroupCommand( command );
        
        GameObject goal = getBestTarget( command.getObjects() );
        
        // No military units, so pick a friendly unit to guard.
        if ( goal == null )
            goal = command.getObjects().iterator().next();
        
        return new Command( command.isGivenByPlayer(), goal);
    }
}
