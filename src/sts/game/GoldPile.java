package sts.game;

import java.awt.Graphics2D;

/**
 * A pile of gold.
 * @author Phillip Cohen
 */
public class GoldPile extends GameObject
{
    int remainingGold = 500;

    public GoldPile( double x, double y )
    {
        super( x, y, 0, 0, null );
    }

    @Override
    public void act()
    {
        
    }
    
    public void removeGold()
    {
        remainingGold-= 10;
    }
    
    public int getGold()
    {
        return remainingGold;
    }

    @Override
    public void draw( Graphics2D g )
    {
        
    }
}
