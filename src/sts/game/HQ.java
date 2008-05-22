package sts.game;

import java.awt.Graphics2D;
import sts.Local;

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

    public HQ( int x, int y, Player player )
    {
        super( x, y, player );
        giveableCommands = new Command[3];
        giveableCommands[0] = new Command( "Create villager");
        giveableCommands[1] = new Command( "Create cow");
        giveableCommands[2] = new Command( "Self destruct");
        
        setWidth( 64 );
        setHeight( 64 );

    }

    @Override
    public void draw( Graphics2D g )
    {
        g.setColor( Local.getSelectedObjects().contains( this ) ? getOwningPlayer().getColor() : getOwningPlayer().getColor().darker() );
        g.fillRect( getX(), getY(), 64, 64 );
    }

    @Override
    protected void doCreation( Enum type )
    {
        switch ( (ProductionOption) type )
        {
            case NEW_VILLAGER:
                getOwningPlayer().giveObject( new Villager( 0, 0, null ) );
                break;
        }
    }

    public void addToQueue( ProductionOption type )
    {
        productionQueue.add( new ItemInQueue( type, type.ticksToCreate ) );
    }

    @Override
    public String getName()
    {
        return "HQ";
    }
    
}
