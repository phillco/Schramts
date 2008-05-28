package sts.game;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A building with a queue of production. Subclasses dictate the types of production availible (such as "Create one Villager").
 * @author Phillip Cohen
 */
public abstract class ProductionBuilding extends GameObject
{
    protected ConcurrentLinkedQueue<ItemInQueue> productionQueue = new ConcurrentLinkedQueue<ItemInQueue>();

    protected int timeToBuild;

    public ProductionBuilding( int x, int y, int timeToBuild, int health, Player player )
    {
        super( x, y, health, player );
        this.timeToBuild = timeToBuild;
    }

    public boolean isBuilt()
    {
        return timeToBuild <= 0;
    }

    public void build()
    {
        if ( timeToBuild > 0 )
            timeToBuild--;
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
    public void giveCommand( Command c )
    {
        productionQueue.add( new ItemInQueue( c, c.getTimeToMake() ) );
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

    protected abstract void doCreation( Command type );

    public class ItemInQueue
    {
        public Command type;

        public int timeLeft;

        public ItemInQueue( Command type, int timeLeft )
        {
            this.type = type;
            this.timeLeft = timeLeft;
        }
    }    
}
