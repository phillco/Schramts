package sts.game;

import java.awt.Graphics2D;
import sts.game.ProductionBuilding.ItemInQueue;

/**
 *
 * @author Phillip Cohen
 */
public abstract class GameObject implements Locatable
{
    /**
     * Location 
     */
    protected int x,  y;

    /**
     * Who controls this?
     */
    private Player owningPlayer;

    /**
     * The unit's health and maxHealth.
     */
    private int maxHealth ,  health;

    protected Command[] giveableCommands;

    public GameObject( int x, int y, int maxHealth, Player owner )
    {
        this.x = x;
        this.y = y;
        this.owningPlayer = owner;
        giveableCommands = new Command[0];
        this.maxHealth = health = maxHealth;
    }

    public void act()
    {
    }
    
    public void changeHealth( int change )
    {
        this.health+=change;
        if(health > maxHealth )
            health=maxHealth;
        if(health <= 0 )
            owningPlayer.removeObject(this);
    }

    public abstract void draw( Graphics2D g );

    public abstract String getName();

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

    @Override
    public String toString()
    {
        return getOwningPlayer().getName() + "'s " + getName() + ", at [" + getX() + "," + getY() + "], health " + health + "/" + maxHealth;
    }

    public Command[] getGiveableCommands()
    {
        return giveableCommands;
    }

    public boolean canExecute( Command c )
    {
        for ( Command giveableC : giveableCommands )
            if ( giveableC == c )
                return true;

        return false;
    }

    public void executeCommand( Command c )
    {
    }

    public Location getLoc()
    {
        return new Location( getX(), getY() );
    }

    public boolean isClickContained( int x, int y )
    {
        return false;
    }

    public static boolean isClickContainedInRectangle( GameObject go, int x, int y, int width, int height )
    {
        boolean okX = ( x <= go.getX() + width && x >= go.getX() );
        boolean okY = ( y <= go.getY() + height && y >= go.getY() );
        return ( okX && okY );
    }

    public void giveCommand( Command c )
    {
        
    }
}
