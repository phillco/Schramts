package sts.game;

import java.awt.Graphics;

/**
 *
 * @author Phillip Cohen
 */
public class Villager extends GameObject
{
    GameObject goal;

    //GoldPile nearest to the villager
    GoldPile nearest = null;

    //True if the villager has already gathered gold from the mine, false if the villager is on the way to the mine, or nearest is empty
    boolean hasGold = false;

    public Villager( double x, double y, Player owner )
    {
        super( x, y, 1, 1, owner );
    }

    @Override
    public void act()
    {
        if ( nearest != null && nearest.getGold() != 0 )
        {
            gatherGold( nearest );
        }
        else
        {
            findNewGold();
        }
    }

    @Override
    public void draw( Graphics g )
    {
        
    }

    private void findNewGold()
    {

    }

    private void gatherGold( GoldPile nearest )
    {
        if ( hasGold )
        {

        }
    }
}
