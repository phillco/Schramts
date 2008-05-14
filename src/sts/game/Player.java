package sts.game;

import java.awt.Color;

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

    public Player( Color color, String name )
    {
        this.color = color;
        this.name = name;
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
