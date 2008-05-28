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
        super( x, y, 0, 0, 100, owner );

        giveableCommands = new Command[1];
        giveableCommands[0] = new Command( "Build barracks", 90, ImageHandler.getBarracks() );
        gold = 0;
    }

    @Override
    public void act()
    {
        super.act();//moves if necessary

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
        if ( !( goal instanceof ProductionBuilding ) )
            return;
        if ( arrived )
            ( (ProductionBuilding) goal ).build();
    }

    private void dropOffGold()
    {
        getOwningPlayer().addGold( gold );
        gold = 0;

    }

    private boolean findNewGold()
    {
        GameObject goldPile = null;
        for ( GameObject go : Local.getGame().getNature().getOwnedObjects() )
        {
            if ( go instanceof GoldPile )
            {
                if ( goldPile == null || Location.getDistance( this.getLoc(), goldPile.getLoc() ) > Location.getDistance( this.getLoc(), go.getLoc() ) )
                    goldPile = (GoldPile) go;
            }
        }
        if ( goldPile == null )//E.T. go home
            return false;
        setGoal( goldPile );
        setDestination( goldPile );
        return true;
    }

    @Override
    public String toString()
    {
        return "Villager" + super.toString();
    }

    private void goldBehavior()
    {
        // :-0 we can't do this
        if ( !( goal instanceof GoldPile ) )
            return;
        if ( !arrived )//the super.act() took care of moving, we can't gather yet
            return;
        GoldPile goldPile = (GoldPile) goal;

        //We have gold and have arrived at HQ
        if ( this.gold > 0 && destination instanceof HQ )
        {
            //we need to drop off our gold
            dropOffGold();
            setDestination( goal );//go back to the gold

            return;

        }

        //we know now that we are at the gold pile and we have gold
        //Return to base
        if ( this.gold == 10 && destination instanceof GoldPile )
        {
            //return to base
            setDestination( getNearestDropoff() );
            return;
        }

        //The gold Pile is out of gold
        if ( goldPile.getGold() <= 0 )
        {
            if ( !findNewGold() )
                setDestination( getNearestDropoff() );
            return;//that's all we can do

        }

        //we are mining!
        mine();
    }

    private GameObject getNearestDropoff()
    {
        GameObject closest = null;
        for ( GameObject go : getOwningPlayer().getOwnedObjects() )
        {
            if ( go instanceof HQ )
            {
                if ( closest == null || Location.getDistance( this.getLoc(), closest.getLoc() ) > Location.getDistance( this.getLoc(), go.getLoc() ) )
                    closest = go;
            }
        }
        return closest;
    }

    private void idleBehavior()
    {
        findNewGold();
    }
    private int timeTillNextMine = 30;

    private void mine()
    {
        if ( !( goal instanceof GoldPile ) )
            return;
        if ( timeTillNextMine-- <= 0 )
        {
            gold += ( (GoldPile) goal ).removeGold() ? 1 : 0;
            timeTillNextMine = 30;
        }
    }

    private void repairBehavior()
    {
        if ( !( goal instanceof ProductionBuilding ) )
            return;
        if ( arrived )
            ( (ProductionBuilding) goal ).repair();
    }

    @Override
    public int getMaxSpeed()
    {
        return 4;
    }

    @Override
    public boolean isClickContained( int x, int y )
    {
        return isClickContainedInRectangle( this, x, y, 8, 13 );
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
        return "Villager with " + gold + " gold";
    }
}
