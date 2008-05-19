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
    private int x,  y,  dx,  dy;

    /**
     * Unit's size.
     */
    private int width,  height;

    /**
     * Who controls this?
     */
    private Player owningPlayer;

    /**
     * The unit's health and maxHealth.
     */
    private int maxHealth = 100,  health = maxHealth;

    public GameObject( int x, int y, int dx, int dy, Player owner )
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.owningPlayer = owner;
    }

    public void act()
    {
        x += dx;
        y += dy;
    }

    public abstract void draw( Graphics2D g );

    public int getDx()
    {
        return dx;
    }

    public void setDx( int dx )
    {
        this.dx = dx;
    }

    public int getDy()
    {
        return dy;
    }

    public void setDy( int dy )
    {
        this.dy = dy;
    }

    public int getX()
    {
        return x;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public void addToX( int addX )
    {
        setX( getX() + addX );
    }

    public int getY()
    {
        return y;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public void addToY( int addY )
    {
        setY( getY() + addY );
    }

    public void setLocation( int x, int y )
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
    public void setVelocity( int dx, int dy )
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
    public void setSpeed( int dx, int dy )
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
    public void decelerate( int factor )
    {
        setVelocity( getDx() * factor, getDy() * factor );
    }

    public int getSpeed()
    {
        return (int) Math.sqrt( getDx() * getDx() + getDy() * getDy() );
    }

    public Player getOwningPlayer()
    {
        return owningPlayer;
    }

    public int getHealth()
    {
        return health;
    }

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    @Override
    public String toString()
    {
        return super.toString() + " at location [" + getX() + "," + getY() + "]";
    }
}
