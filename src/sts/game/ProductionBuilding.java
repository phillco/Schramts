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

    protected abstract void doCreation( Enum type );

    protected class ItemInQueue
    {
        Enum type;

        int timeLeft;

        public ItemInQueue( Enum type, int timeLeft )
        {
            this.type = type;
            this.timeLeft = timeLeft;
        }
    }
}
