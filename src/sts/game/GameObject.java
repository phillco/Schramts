package sts.game;

import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public abstract class GameObject
{
    /**
     * Location and velocity.
     */
    double x, y, dx, dy;

    /**
     * Who controls this?
     */
    Player owner;

    /**
     * The unit's health and maxHealth.
     */
    int maxHealth = 100, health = maxHealth;

    public GameObject( double x, double y, double dx, double dy, Player owner )
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.owner = owner;
    }

    public void act()
    {
        x += dx;
        y += dy;
    }

    public abstract void draw( Graphics2D g );
    
    public double getDx()
    {
        return dx;
    }

    public void setDx( double dx )
    {
        this.dx = dx;
    }

    public double getDy()
    {
        return dy;
    }

    public void setDy( double dy )
    {
        this.dy = dy;
    }

    public double getX()
    {
        return x;
    }

    public void setX( double x )
    {
        this.x = x;
    }

    public void addToX( double addX )
    {
        setX( getX() + addX );
    }

    public double getY()
    {
        return y;
    }

    public void setY( double y )
    {
        this.y = y;
    }

    public void addToY( double addY )
    {
        setY( getY() + addY );
    }

    public void setLocation( double x, double y )
    {
        setX( x );
        setY( y );
    }

    /**
     * Gives <code>this</code> a new velocity, disregarding the old velocity.
     * 
     * @param dx The new x velocity
     * @param dy The new y velocity
     */
    public void setVelocity( double dx, double dy )
    {
        setDx( dx );
        setDy( dy );
    }

    /**
     * Gives <code>this</code> a new speed, but preserves the sign of dx and dy.
     * If dx or dy is zero, their sign is treated as positive.
     * 
     * @param dx The new magnitude of the x velocity
     * @param dy The new magnitude of the y velocity
     */
    public void setSpeed( double dx, double dy )
    {
        int signX = 1, signY = 1;
        if ( getDx() < 0 )
            signX = -1;
        if ( getDy() < 0 )
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
    public void decelerate( double factor )
    {
        setVelocity( getDx() * factor, getDy() * factor );
    }

    public double getSpeed()
    {
        return Math.sqrt( getDx() * getDx() + getDy() * getDy() );
    }

    public Player getOwner()
    {
        return owner;
    }
    
    
}
