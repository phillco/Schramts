package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import sts.Local;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class Villager extends Unit
{
    public final static int CARRYING_CAPACITY = 30;

    /**
     * How much gold this Villager is carrying.
     */
    private int gold;

    /**
     * Where to go after dropping off gold.
     */
    private Location deferredDestination = null;

    private int timeTillNextMine = 0;

    public Villager( int x, int y, Player owner )
    {
        super( x, y, 0, 0, 12, 16, 100, owner );

        productionCommands = new ProductionCommand[2];
        productionCommands[0] = new ProductionCommand( "Build barracks", 90, 750, ImageHandler.getBarracksButton() );
        productionCommands[1] = new ProductionCommand( "Sell into slavery", 20, 0, ImageHandler.getSlaveryButton() );

        gold = 0;
    }

    @Override
    public void act()
    {
        super.act();

        // Global slave trade. Update our price.
        productionCommands[1].setCost( -Game.getInstance().getSlaveryGold() );

        // Nothing to do.
        if ( commandQueue.isEmpty() )
        {
            findNewGold();
            return;
        }

        if ( commandQueue.peek().getObject() instanceof GoldPile && !couldUse( (GoldPile) commandQueue.peek().getObject() ) )
        {
            nextCommand();
            if ( !findNewGold() )
                giveCommand( new Command( false, getNearestDropoff() ), false );
            return;
        }

        if ( arrived )
        {
            // At a gold mine.
            if ( commandQueue.peek().getObject() instanceof GoldPile )
            {
                goldBehavior();
                return;
            }

            // At the HQ, with gold.
            if ( commandQueue.peek().getObject() instanceof HQ && gold > 0 )
            {
                dropOffGold();
                nextCommand();
                return;
            }

            // At a building.
            if ( commandQueue.peek().getObject() instanceof ProductionBuilding )
            {
                if ( ( (ProductionBuilding) commandQueue.peek().getObject() ).isBuilt() )
                    ( (ProductionBuilding) commandQueue.peek().getObject() ).repair();
                else
                    ( (ProductionBuilding) commandQueue.peek().getObject() ).build();

                return;
            }
        }
    }

    private void dropOffGold()
    {
        getOwningPlayer().addGold( gold );
        gold = 0;
    }

    public boolean findNewGold()//start mining
    {
        GoldPile nearest = null;
        for ( GameObject go : Local.getGame().getNature().getOwnedObjects() )
        {
            if ( go instanceof GoldPile && couldUse( (GoldPile) go ) )
            {
                if ( nearest == null || Location.getDistance( this.getLocation(), nearest.getLocation() ) > Location.getDistance( this.getLocation(), go.getLocation() ) )
                    nearest = (GoldPile) go;
            }
        }
        if ( nearest == null )//E.T. go home
            return false;
        giveCommand( new Command( false, nearest ), false );
        return true;
    }

    @Override
    public String toString()
    {
        return "Villager" + super.toString();
    }

    private void goldBehavior()
    {
        GoldPile goldPile = (GoldPile) commandQueue.peek().getObject();

        // We have enough - return to base.
        if ( gold >= CARRYING_CAPACITY )
        {
            nextCommand();

            giveCommand( new Command( false, getNearestDropoff() ), false );
            giveCommand( new Command( false, goldPile ), false );
            goldPile.setMiningVillager( null );
            return;
        }

        // No problems - mine normally.
        mine( goldPile );
    }

    @Override
    public void giveCommand( ProductionCommand c )
    {
        if ( getOwningPlayer().getGoldAmount() < c.getCost() )
            return;//can't afford

        if ( c == productionCommands[0] )
        {
            Barracks b = new Barracks( getX(), getY() - 30, getOwningPlayer() );
            getOwningPlayer().giveObject( b );
            getOwningPlayer().addGold( -c.getCost() );
            giveCommand( new Command( true, b ), true );
        }
        else
            getOwningPlayer().addGold( Game.getInstance().sellIntoSlavery( this ) );
    }

    private GameObject getNearestDropoff()
    {
        GameObject closest = null;
        for ( GameObject go : getOwningPlayer().getOwnedObjects() )
        {
            if ( go instanceof HQ )
            {
                if ( closest == null || Location.getDistance( this.getLocation(), closest.getLocation() ) > Location.getDistance( this.getLocation(), go.getLocation() ) )
                    closest = go;
            }
        }
        return closest;
    }

    private void mine( GoldPile goldPile )
    {
        if ( couldUse( goldPile ) )
        {
            goldPile.setMiningVillager( this );
            if ( timeTillNextMine-- <= 0 )
            {
                gold += ( (GoldPile) commandQueue.peek().getObject() ).removeGold() ? 1 : 0;
                timeTillNextMine = Game.getInstance().isTurbo() ? 1 : 5;
            }
        }
        else
        {
            findNewGold();
            nextCommand();
        }
    }

    private boolean couldUse( GoldPile goldPile )
    {
        return ( goldPile.getMiningVillager() == null || goldPile.getMiningVillager() == this ) && goldPile.getGold() > 0;
    }

    @Override
    public int getMaxSpeed()
    {
        return 4;
    }

    @Override
    public boolean isClickContained( int x, int y, int width, int height )
    {
        return isClickContainedInRectangle( this, x, y, 8 + width, 13 + height );
    }

    @Override
    public void draw( Graphics2D g )
    {
        super.draw( g );

        // Draw a flag if we're moving to a specific spot, even if we're dropping off gold first.
        if ( deferredDestination != null && getOwningPlayer() == Local.getLocalPlayer() )
            ImageHandler.drawDestination( g, deferredDestination.getLocation().getX(), deferredDestination.getLocation().getY(), getOwningPlayer().getColor() );
        Color c = getOwningPlayer().getColor();

        if ( Local.getSelectedObjects().contains( this ) )
            c = ImageHandler.getOppositeColor( c );
        ImageHandler.drawVillager( g, getX(), getY(), c, gold > 0 );
    }

    public String getName()
    {
        return "Villager with " + gold + " gold";
    }

    @Override
    public Command processGroupCommand( GroupCommand command )
    {
        Set<GameObject> possible = command.getObjects();
        Set<GameObject> goldMines = new HashSet<GameObject>();
        Set<GameObject> constructionSites = new HashSet<GameObject>();
        Set<GameObject> repair = new HashSet<GameObject>();

        // Split and categorize the list.
        for ( GameObject go : possible )
        {
            if ( go instanceof GoldPile )
            {
                goldMines.add( go );
                continue;
            }
            if ( go instanceof ProductionBuilding )
            {
                if ( !( (ProductionBuilding) go ).isBuilt() )
                {
                    constructionSites.add( go );
                    continue;
                }
                if ( ( (ProductionBuilding) go ).needsRepair() )
                {
                    repair.add( go );
                    continue;
                }
            }
        }

        // Pick options by priority.
        if ( !goldMines.isEmpty() )
            return new Command( command.isGivenByPlayer(), goldMines.iterator().next() );
        if ( !constructionSites.isEmpty() )
            return new Command( command.isGivenByPlayer(), constructionSites.iterator().next() );
        if ( !repair.isEmpty() )
            return new Command( command.isGivenByPlayer(), repair.iterator().next() );

        return super.processGroupCommand( command );
    }
}
