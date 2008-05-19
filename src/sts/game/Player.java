package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    /**
     * All of the objects (villagers, buildings, etc) that belong to this player.
     */
    private ConcurrentLinkedQueue<GameObject> ownedObjects = new ConcurrentLinkedQueue<GameObject>();

    public Player( Color color, String name )
    {
        this.color = color;
        this.name = name;
    }

    public void act()
    {
        for ( GameObject go : ownedObjects )
            go.act();
    }

    public void draw( Graphics2D g )
    {
        for ( GameObject go : ownedObjects )
            go.draw( g );
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
}
