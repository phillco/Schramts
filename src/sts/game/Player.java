package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import sts.Local;

/**
 *
 * @author Phillip Cohen
 */
public class Player
{
    /**
     * How much gold this player has stockpiled.
     */
    private int goldAmount = 100;

    /**
     * Color of the player's units and buildings.
     */
    private Color color;

    /**
     * The player's name.
     */
    private String name;

    private AI helper;

    /**
     * All of the objects (villagers, buildings, etc) that belong to this player.
     */
    private ConcurrentLinkedQueue<GameObject> ownedObjects = new ConcurrentLinkedQueue<GameObject>();
    
    /**
     * If this player has lost the game
     */
    private boolean lost;

    public Player( Color color, String name )
    {
        this.color = color;
        this.name = name;
        lost = false;
        helper = new PlayingAI( this );
    }

    public Player( Color color, String name, AI helper )
    {
        this.color = color;
        this.name = name;
        lost = false;
        this.helper = helper;
    }

    public void act()
    {
        helper.act();
        for ( GameObject go : ownedObjects )
            go.act();
    }

    public void draw( Graphics2D g )
    {
        for ( GameObject go : ownedObjects )
            go.draw( g );
    }

    public void giveObject( GameObject g )
    {
        ownedObjects.add( g );
    }

    public void removeObject( GameObject g )
    {
        ownedObjects.remove( g );
        Local.getSelectedObjects().remove( g );
    }

    public String getName()
    {
        return name;
    }

    public Color getColor()
    {
        return color;
    }

    public int getGoldAmount()
    {
        return goldAmount;
    }

    public ConcurrentLinkedQueue<GameObject> getOwnedObjects()
    {
        return ownedObjects;
    }

    void addGold( int gold )
    {
        goldAmount += gold;
    }
    
    public void lose()
    {
        lost = true;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public AI getHelper()
    {
        return helper;
    }

    /**
     * Returns the player's HQ, or null if hasn't one.
     */
    public HQ getHQ()
    {
        for ( GameObject go : ownedObjects )
            if ( go instanceof HQ )
                return ( (HQ) go );
        return null;
    }

    /**
     * Returns a set of all of this player's villagers. Could be an empty set, but not null.
     */
    public Set<Villager> getVillagers()
    {
        Set<Villager> theSet = new HashSet<Villager>();

        for ( GameObject go : ownedObjects )
            if ( go instanceof Villager )
                theSet.add( (Villager) go );

        return theSet;
    }
    
    /**
     * Returns a set of all of this player's infantry. Could be an empty set, but not null.
     */
    public Set<Infantry> getInfantry()
    {
        Set<Infantry> theSet = new HashSet<Infantry>();

        for ( GameObject go : ownedObjects )
            if ( go instanceof Infantry )
                theSet.add( (Infantry) go );

        return theSet;
    }

    /**
     * Returns a set of all of this player's barracks. Could be an empty set, but not null.
     */
    public Set<Barracks> getBarracks()
    {
        Set<Barracks> theSet = new HashSet<Barracks>();

        for ( GameObject go : ownedObjects )
            if ( go instanceof Barracks )
                theSet.add( (Barracks) go );

        return theSet;
    }

    /**
     * Returns a set of all of this player's gold piles. Could be an empty set, but not null.
     * (Useful only for Nature).
     * @see Game#nature
     */
    public Set<GoldPile> getGoldPiles()
    {
        Set<GoldPile> theSet = new HashSet<GoldPile>();

        for ( GameObject go : ownedObjects )
            if ( go instanceof GoldPile )
                theSet.add( (GoldPile) go );

        return theSet;
    }

    public boolean hasLost() {
        return lost;
    }
}
