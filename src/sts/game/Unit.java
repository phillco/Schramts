/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

/**
 *
 * @author AK90454
 */
public abstract class Unit extends GameObject
{
    private int dx,  dy;

    // where we're going, -1 if no destination
    private int destX,  destY;

    public Unit( int x, int y, int dx, int dy, Player player )
    {
        super( x, y, player );
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void act()
    {
        move();
    }

    /**
     * figures out what dx and dy to have to make it to the destination
     */
    private void calculateSpeed()
    {
        double angle = Math.atan2( getX() - destX, getY() - destY );
        dx = (int) ( getMaxSpeed() * Math.cos( angle ) );
        dy = (int) ( getMaxSpeed() * Math.sin( angle ) );
    }

    public void setDestination( int x, int y )
    {
        destX = x;
        destY = y;
    }

    /**
     * Returns how fast this object can travel.  Abstract so that each type of unit
     * can have its own speed
     */
    public abstract int getMaxSpeed();

    private void move()
    {
        if ( destX != -1 && destY != -1 )
        {
            calculateSpeed();
            if ( Math.sqrt( ( getX() - destX ) * ( getX() - destX ) +
                            ( getY() - destY ) * ( getY() - destY ) ) < getMaxSpeed() )
            {
                setLocation( destX, destY );
                destX = destY = -1;
            }
            else
            {
                x += dx;
                y += dy;
            }
        }


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

    public int getSpeed()
    {
        return (int) Math.sqrt( dx * dx + dy * dy );
    }
    
        @Override
    public String toString()
    {
        return super.toString();
    }
}