package sts.game;

import java.awt.Graphics2D;

/**
 * The player's chief building. Creates villagers.
 * @author Phillip Cohen
 */
public class HQ extends ProductionBuilding
{
    public enum ProductionOption
    {
        NEW_VILLAGER( 200, "Create one new villager" );

        final int ticksToCreate;
        
        final String description;

        private ProductionOption( int ticksToCreate, String description )
        {
            this.ticksToCreate = ticksToCreate;
            this.description = description;
        }
    }

    public HQ( double x, double y, Player player )
    {
        super( x, y, player );
    }

    @Override
    public void draw( Graphics2D g )
    {
    }

    @Override
    protected void doCreation( Enum type )
    {
        switch ( (ProductionOption) type )
        {
            case NEW_VILLAGER:
                getOwner().giveObject( new sts.game.Villager( 0, 0, null ) );
                break;
        }
    }

    public void addToQueue( ProductionOption type )
    {
        productionQueue.add( new ItemInQueue( type, type.ticksToCreate ) );
    }
}