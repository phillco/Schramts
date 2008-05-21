package sts.game;

import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public class Villager extends Unit
{
    GameObject goal;

    //GoldPile nearest to the villager
    GoldPile nearest = null;

    //True if the villager has already gathered gold from the mine, false if the villager is on the way to the mine, or nearest is empty
    boolean hasGold = false;
      
    public Villager( int x, int y, Player owner )
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
    public void draw( Graphics2D g )
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

    @Override
    public int getMaxSpeed()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
