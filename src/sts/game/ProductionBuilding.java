package sts.game;

import java.awt.Graphics2D;
import java.util.concurrent.ConcurrentLinkedQueue;
import sts.Local;

/**
 * A building with a queue of production. Subclasses dictate the types of production availible (such as "Create one Villager").
 * @author Phillip Cohen
 */
public abstract class ProductionBuilding extends GameObject
{
    protected ConcurrentLinkedQueue<ItemInQueue> productionQueue = new ConcurrentLinkedQueue<ItemInQueue>();

    protected int timeToBuild,  healthRate;

    protected RallyPoint rallyPoint = null;

    public ProductionBuilding( int x, int y, int width, int height, int timeToBuild, int health, Player player )
    {
        super( x, y, width, height, health, player );
        this.timeToBuild = timeToBuild;
        this.healthRate = (int) Math.ceil( ( (double) health ) / timeToBuild ); // [PC] Not totally accurate due to integer limitations...but oh well. Better early than late.
        setHealth( 1 );
    }

    public boolean isBuilt()
    {
        return timeToBuild <= 0;
    }

    public boolean needsRepair()
    {
        return isBuilt() && getHealth() < getMaxHealth();
    }

    public void build()
    {
        if ( timeToBuild > 0 )
            timeToBuild--;

        // "Build".
        changeHealth( healthRate );
    }

    public void repair()
    {
        if ( getMaxHealth() == getHealth() )
            return;//we don't need no stinking reparations!

        if ( getOwningPlayer().getGoldAmount() > 0 )
        {
            changeHealth( 1 );
            if ( getHealth() % 10 == 0 )//nothing in this world is free
                getOwningPlayer().addGold( -1 );
        }
    }

    public Iterable<ItemInQueue> getProductionQueue()
    {
        return productionQueue;
    }

    @Override
    public void giveCommand( ProductionCommand c )
    {
        // Not built yet.
        if ( !isBuilt() )
            return;

        if ( getOwningPlayer().getGoldAmount() < c.getCost() )
            return;//can't afford.

        productionQueue.add( new ItemInQueue( c, c.getTimeToMake() ) );
        getOwningPlayer().addGold( -c.getCost() );
    }

    public void setRalleyPoint( int x, int y )
    {
        rallyPoint = new RallyPoint( x, y, getOwningPlayer() );
    }

    public void createAndAssignUnit( Unit u )
    {
        if ( rallyPoint != null )
            u.giveCommand( new Command( false, rallyPoint ), false );

        getOwningPlayer().giveObject( u );
    }

    @Override
    public void act()
    {
        super.act();
        if ( productionQueue.peek() != null )
        {
            // Tick the first queued item. Is its time up?
            if ( productionQueue.peek().timeLeft-- <= 0 )
            {
                // Act on the item and remove it.
                doCreation( productionQueue.poll().type );
            }

        }
    }

    public int queueLength()
    {
        return this.productionQueue.size();
    }

    @Override
    public void draw( Graphics2D g )
    {
        if ( rallyPoint != null && Local.getSelectedObjects().contains( this ) )
            rallyPoint.draw( g );
    }

    protected abstract void doCreation( ProductionCommand type );

    public class ItemInQueue
    {
        public ProductionCommand type;

        public int timeLeft;

        public ItemInQueue( ProductionCommand type, int timeLeft )
        {
            this.type = type;
            this.timeLeft = timeLeft;
        }
    }
}
