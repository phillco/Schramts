package sts.game;

import java.awt.Graphics2D;
import sts.gui.ImageHandler;

/**
 * A pile of gold.
 * @author Phillip Cohen
 */
public class GoldPile extends GameObject
{
    int remainingGold = 50;

    private Villager miningVillager;

    public GoldPile( int x, int y, Player nature )
    {
        super( x, y, 8, 8, Integer.MAX_VALUE, nature );
    }

    @Override
    public void act()
    {
        if ( miningVillager != null )
        {
            if ( !miningVillager.getOwningPlayer().getOwnedObjects().contains( miningVillager ) )
                miningVillager = null;
        }

    }

    public Villager getMiningVillager()
    {
        return miningVillager;
    }

    public void setMiningVillager( Villager miningVillager )
    {
        this.miningVillager = miningVillager;
    }

    public boolean removeGold()
    {
        if ( remainingGold == 1 )//out of Gold
            getOwningPlayer().removeObject( this );
        return --remainingGold >= 0;
    }

    public int getGold()
    {
        return remainingGold;
    }

    @Override
    public void draw( Graphics2D g )
    {
        ImageHandler.drawGold( g, getX(), getY() + ( miningVillager == null ? 0 : 1 ) );
    }

    @Override
    public String getName()
    {
        return "Gold pile with " + remainingGold + " gold remaining";
    }

    @Override
    public boolean isClickContained( int x, int y, int width, int height )
    {
        return isClickContainedInRectangle( this, x, y, 8 + width, 8 + height );
    }
}
