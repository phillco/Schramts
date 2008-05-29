/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

/**
 *
 * @author Owner
 */
public class PlayingAI extends AI
{
    private Player owner;

    public PlayingAI( Player owner )
    {
        this.owner = owner;
    }

    public void act()
    {
        if ( villagerCount() < 10 )
        {
            assignVillagersToGold();
            purchaseVillagers();
            return;
        }
    }

    private void assignVillagersToGold()
    {
        for ( GameObject go : owner.getOwnedObjects() )
        {
            if ( go instanceof Villager )
            {
                if ( !( ( (Unit) go ).getGoal() instanceof GoldPile ) )
                {
                    ( (Villager) go ).findNewGold();
                }
            }
        }
    }

    private void purchaseVillagers()
    {
        if(owner.getGoldAmount()<100)
            return;
        HQ hq = null;
        for ( GameObject go : owner.getOwnedObjects() )
        {
            if ( go instanceof HQ )
            {
                hq = (HQ) go;
                break;
            }
        }
        hq.giveCommand( hq.getGiveableCommands()[0] );

    }

    private int villagerCount()
    {
        int count = 0;
        for ( GameObject go : owner.getOwnedObjects() )
        {
            if ( go instanceof Villager )
                ++count;
        }
        return count;
    }
}
