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
    
    private State state;
    
    private static State earlyEconomic = new State(10, 0, false, VillagerState.MINE);
    private static State defensive = new State(7, 3, false, VillagerState.REPAIR);
    private static State branchingOut = new State(10, 0, false, VillagerState.BUILD);
    private static State offensive = new State(8, 8, true, VillagerState.MINE);
    private static State endGame = new State(3, 20, true, VillagerState.REPAIR);
    private static State panicDefense = new State(-5, 20, false, VillagerState.REPAIR);

    public PlayingAI( Player owner )
    {
        this.owner = owner;
        state = earlyEconomic;
    }

    public void act()
    {
        if ( villagerCount() < state.villagerThreshhold )
            purchaseVillagers();
        if( infantryCount() < state.infantryThreshhold )
            purchaseInfantry();
        switch(state.villagerState)
        {
            case MINE:
                assignVillagersToGold();  
                break;
            case BUILD:
                assignVillagersToBuild();
                break;
            case REPAIR:
                assignVillagersToRepair();
                break;
        }
        if(state.infantryAgressive)
            infantryAttack();
        else
            infantryDefend();
    }

    private void assignVillagersToBuild() {
        throw new UnsupportedOperationException("Not yet implemented");
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

    private void assignVillagersToRepair() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void infantryAttack() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int infantryCount() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void infantryDefend() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void purchaseInfantry() {
        throw new UnsupportedOperationException("Not yet implemented");
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
        if(hq == null)//uh-oh, no hq
            return;
        hq.giveCommand(hq.getGiveableCommands()[0]);
    }

    private int villagerCount()
    {
        return owner.getVillagers().size();
    }
    
    private static enum VillagerState {MINE, BUILD, REPAIR};
    
    private static class State
    {
        public final int villagerThreshhold, infantryThreshhold;
        public final boolean infantryAgressive;
        public final VillagerState villagerState;
        public State(int villagerThreshhold, int infantryThreshhold, boolean infantryAgressive, VillagerState villagerState)
        {
            this.villagerThreshhold=villagerThreshhold;
            this.infantryThreshhold=infantryThreshhold;
            this.infantryAgressive=infantryAgressive;
            this.villagerState=villagerState;
        }
    }
}
