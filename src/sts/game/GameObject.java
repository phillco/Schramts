package sts.game;

import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public abstract class GameObject implements Locatable
{
    /**
     * Location 
     */
    protected double x,  y;

    /**
     * Who controls this?
     */
    private Player owningPlayer;

    /**
     * The unit's health and maxHealth.
     */
    private int maxHealth,  health;

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
        this.health += change;
        if ( health > maxHealth )
            health = maxHealth;
        if ( health <= 0 )
            owningPlayer.removeObject( this );


    }

    /**
     * AI-friendly.
     */
    public void changeHealth( int change, GameObject attacker )
    {
        changeHealth( change );
        if ( change < 0 )
            getOwningPlayer().getHelper().notifyAboutAttack( getX(), getY(), attacker );
    }

    protected void setHealth( int health )
    {
        this.health = health;
    }

    public abstract void draw( Graphics2D g );

    public abstract String getName();

    public int getX()
    {
        return (int)x;
    }

    public void setX( double x )
    {
        this.x = x;
    }

    public void addToX( double addX )
    {
        setX( x + addX );
    }

    public int getY()
    {
        return (int)y;
    }

    public void setY( double y )
    {
        this.y = y;
    }

    public void addToY( double addY )
    {
        setY( y + addY );
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

    public boolean isClickContained( int x, int y, int width, int height )
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
