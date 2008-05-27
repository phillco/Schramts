package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import sts.Local;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class Villager extends Unit
{

    //GoldPile nearest to the villager
    GoldPile nearest = null;

    /**
     * How much gold this Villager is carrying
     */
    private int gold;

    public Villager( int x, int y, Player owner )
    {
        super( x, y, 1, 1, owner );
        gold = 0;

        setWidth( 8 );
        setHeight( 10 );
    }

    @Override
    public void act()
    {
//        super.act();//moves if necessary
        if ( goal == null )
        {
            idleBehavior();
            return;
        }
        if ( goal instanceof GoldPile )
        {
            goldBehavior();
            return;
        }
        if ( goal instanceof ProductionBuilding )
        {
            if ( ( (ProductionBuilding) goal ).isBuilt() )
            {
                repairBehavior();
            }
            else
            {
                buildBehavior();
            }
        }
    }

    private void buildBehavior()
    {
    }

    private void dropOffGold()
    {
        getOwningPlayer().addGold( gold );
        gold = 0;
        destination = goal;//go back to the gold

    }

    private void findNewGold()
    {
    }

    @Override
    public String toString()
    {
        return "Villager" + super.toString();
    }

    private void goldBehavior()
    {
        assert goal instanceof GoldPile;
        if ( !arrived )//the super.act() took care of moving, we can't gather yet
            return;
        GoldPile goldPile = (GoldPile) goal;
        if ( goldPile.getGold() <= 0 )
        {
            findNewGold();
            return;//that's all we can do

        }
        //we know now that we are at the gold pile and we have gold
        if ( this.gold == 10 && destination instanceof GoldPile )
        {
            //return to base
            setDestination( getNearestDropoff() );
            return;
        }
        if ( this.gold == 10 && destination instanceof HQ )
        {
            //we need to drop off our gold
            dropOffGold();
            return;
        }

        //we are mining!
        mine();
    }

    private Location getNearestDropoff()
    {
        Location closest = null;
        for ( GameObject go : getOwningPlayer().getOwnedObjects() )
        {
            if ( go instanceof HQ )
            {
                if ( closest == null || Location.getDistance( this.getLoc(), closest ) > Location.getDistance( this.getLoc(), go.getLoc() ) )
                    closest = go.getLoc();
            }
        }
        return closest;
    }

    private void idleBehavior()
    {
    }
    private int timeTillNextMine = 50;

    private void mine()
    {
        if ( timeTillNextMine-- <= 0 )
        {
            gold++;

        }

    }

    private void repairBehavior()
    {
    }

    @Override
    public int getMaxSpeed()
    {
        return 4;
    }

    @Override
    public void draw( Graphics2D g )
    {
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );
        ImageHandler.drawVillager( g, getX(), getY(), c, gold > 0 );
    }

    public String getName()
    {
        return "Villager";
    }
}
