package sts.game;

import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public abstract class GameObject
{
    /**
     * Location 
     */
    protected int x,  y;

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

    public GameObject( int x, int y, Player owner )
    {
        this.x = x;
        this.y = y;
        this.owningPlayer = owner;
    }

    public void act()
    {
    }

    public abstract void draw( Graphics2D g );

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

    public void setWidth( int width )
    {
        this.width = width;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }

    @Override
    public String toString()
    {
        return super.toString() + " at location [" + getX() + "," + getY() + "]";
    }
}
