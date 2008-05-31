/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.Graphics2D;
import java.util.Set;
import sts.Local;
import sts.Util;
import sts.gui.ImageHandler;

/**
 *
 * @author AK90454
 */
public abstract class Unit extends GameObject
{
    /**
     * What this Unit is working on
     */
    GameObject goal;

    /**
     * Where this Unit is going
     */
    Locatable destination;

    boolean arrived;

    protected double dx,  dy;

    protected double mySpeedRate = Util.getRandomGenerator().nextDouble() * 0.4 + 0.8;

    public Unit( int x, int y, int dx, int dy, int width, int height, int health, Player player )
    {
        super( x, y, width, height, health, player );
        this.dx = dx;
        this.dy = dy;
        arrived = false;
    }

    @Override
    public void act()
    {
        move();

    // [PC] Funny but ugly code. Try it and then queue up a bunch of infantry.
        /*
    Set<GameObject> onMe = Game.getInstance().getObjectsWithinArea( getX(), getY(), 5, 5 );
    onMe.remove( this );
    if ( !onMe.isEmpty() )
    x += Util.getRandomGenerator().nextMidpointDouble( 16 );
     */
    }

    @Override
    public void draw( Graphics2D g )
    {
        // Draw a flag if we're moving to a specific spot.
        if ( destination != null && goal == null && !arrived && getOwningPlayer() == Local.getLocalPlayer() )
            ImageHandler.drawDestination( g, destination.getLoc().getX(), destination.getLoc().getY(), getOwningPlayer().getColor() );
    }

    /**
     * figures out what dx and dy to have to make it to the destination
     */
    protected void calculateSpeed()
    {
        if ( destination == null )
        {
            dx = dy = 0;
            return;
        }
        double angle = Math.atan2( getY() - destination.getLoc().getY(), getX() - destination.getLoc().getX() ) + Math.PI;
        dx = ( getMaxSpeed() * Math.cos( angle ) ) * mySpeedRate;
        dy = ( getMaxSpeed() * Math.sin( angle ) ) * mySpeedRate;
    }

    public void setDestination( int x, int y )
    {
        arrived = false;
        destination = new Location( x, y );
    }

    public void setDestination( Locatable l )
    {
        arrived = false;
        this.destination = l;
    }

    public void setGoal( GameObject go )
    {
        goal = go;
        //[PC] destination= go;
        arrived = false;
    }

    public abstract void setGoal( Set<GameObject> possible );

    /**
     * Returns how fast this object can travel.  Abstract so that each type of unit
     * can have its own speed
     */
    public abstract int getMaxSpeed();

    private void move()
    {
        if ( destination == null )
        {
            if ( goal == null )
                return;
            destination = goal;
        }
        if ( !arrived )
        {
            calculateSpeed();
            int destX = destination.getLoc().getX(), destY = destination.getLoc().getY();
            if ( Math.sqrt( ( getX() - destX ) * ( getX() - destX ) +
                            ( getY() - destY ) * ( getY() - destY ) ) < getMaxSpeed() + 1 )
            {
                setLocation( destination.getLoc().getX(), destination.getLoc().getY() );
                arrived = true;
            }
            else
            {
                x += dx;
                y += dy;
            }
        }


    }

    public GameObject getGoal()
    {
        return goal;
    }

    /**
     * Gives <code>this</code> a new velocity, disregarding the old velocity.
     * 
     * @param dx The new x velocity
     * @param dy The new y velocity
     */
    public void setVelocity( int dx, int dy )
    {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gives <code>this</code> a new speed, but preserves the sign of dx and dy.
     * If dx or dy is zero, their sign is treated as positive.
     * 
     * @param dx The new magnitude of the x velocity
     * @param dy The new magnitude of the y velocity
     */
    public void setSpeed( int dx, int dy )
    {
        int signX = 1, signY = 1;
        if ( dx < 0 )
            signX = -1;
        if ( dy < 0 )
            signY = -1;
        setVelocity( signX * Math.abs( dx ), signY * Math.abs( dy ) );
    }

    /**
     * Multiplies this <code>GameObject</code>'s dx and dy by the given factor.
     * A parameter of 0 will stop <code>this</code> immediately; a parameter of 1.0
     * will have no effect, and any number above 1 will be an increase in speed.
     * 
     * @param factor a number between 0.0 and 1.0 representing the amount to slow down.
     */
    public void decelerate( int factor )
    {
        dx *= factor;
        dy *= factor;
    }

    public double getSpeed()
    {
        return Math.sqrt( dx * dx + dy * dy );
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
