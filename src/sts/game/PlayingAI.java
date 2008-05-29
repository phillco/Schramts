/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import sts.Local;
import java.util.Stack;

/**
 *
 * @author Owner
 */
public class PlayingAI extends AI
{
    private Player owner;
    
    //The current state
    private State state;
    
    //The state that is pushed aside for panicDefense
    private State waiting;
    
    private static State earlyEconomic = new State(10, 0, false, VillagerState.MINE);
    private static State defensive = new State(7, 3, false, VillagerState.REPAIR);
    private static State branchingOut = new State(10, 0, false, VillagerState.BUILD);
    private static State offensive = new State(8, 8, true, VillagerState.MINE);
    private static State endGame = new State(3, 20, true, VillagerState.REPAIR);
    private static State panicDefense = new State(8, 20, false, VillagerState.REPAIR);

    private Stack<GameObject> enemies;//everyone we want to kill, because they attacked us first.
    
    private Player targetPlayer;
    
    public PlayingAI( Player owner )
    {
        this.owner = owner;
        waiting = state = earlyEconomic;
        enemies = new Stack<GameObject>();
        targetPlayer= null;
    }

    public void act()
    {
        checkState();
        updateEnemies();
        if ( villagerCount() < state.villagerThreshhold )
            purchaseVillagers();
        else if( villagerCount() - state.villagerThreshhold > 2 )
            sellVillagers();
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
    
    private Villager carpenter;

    private void assignVillagersToBuild()
    {
        if(owner.getGoldAmount()<750)//ok, so I hard coded the value.  So shoot me
            return;//don't bother trying to build, 
        if(carpenter==null)
        {
            carpenter=owner.getVillagers().iterator().next();
            carpenter.setGoal((GameObject)null);
            carpenter.setDestination(owner.getHQ().getLoc().translate(0, -75));
        }
        if(carpenter.arrived)
        {
            carpenter.executeCommand(carpenter.giveableCommands[0]);
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

    private void assignVillagersToRepair()
    {
        Set<ProductionBuilding> needsRepair = new HashSet<ProductionBuilding>();
        for(Barracks b : owner.getBarracks())
        {
            if(b.needsRepair())
                needsRepair.add(b);
        }
        if(owner.getHQ().needsRepair())
            needsRepair.add(owner.getHQ());
        if(needsRepair.isEmpty())
        {
            for(Villager v : owner.getVillagers())
                v.findNewGold();
        }
        int count = 0;
        ArrayList<ProductionBuilding> linearAccess = new ArrayList<ProductionBuilding>(needsRepair);
        for(Villager v: owner.getVillagers())
        {
            v.setGoal(linearAccess.get(count));
            count++;
            count%=needsRepair.size();
        }
       
    }

    private void checkState() 
    {
        if(state == earlyEconomic)
        {
            if(villagerCount()>=state.villagerThreshhold)
            {
                state = branchingOut;
                return;
            }
        }
        if(state == branchingOut )
        {
            for(Barracks b :owner.getBarracks())
            {
                if(b.isBuilt())
                {
                    state = offensive;
                    return;
                }
            }
        }
        if(state == offensive)
        {
            if(Local.getGame().getNature().getGoldPiles().isEmpty())
            {
                state = endGame;
                return;
            }
        }
        if(state == panicDefense)
        {
            if(enemies.isEmpty())
                state = this.waiting;
        }
    }
    
    @Override
    public void notifyAboutAttack(int x, int y, GameObject attacker)
    {
        enemies.push(attacker);
        if(enemies.size()>2)
        {
            waiting = state;
            state = panicDefense;
        }
    }

    private void chooseTargetPlayer()
    {
        ArrayList<Player> possible = new ArrayList<Player>(Local.getGame().getPlayers());
        possible.remove(owner);
        targetPlayer = possible.get((int)(Math.random()*possible.size()));
    }

    private GameObject getBestTarget(Player player)
    {
        if(!player.getInfantry().isEmpty())
            return player.getInfantry().iterator().next();
        if(!player.getVillagers().isEmpty())
            return player.getVillagers().iterator().next();
        if(!player.getBarracks().isEmpty())
            return player.getBarracks().iterator().next();
        return player.getHQ();
    }

    private void infantryAttack()
    {
        if(targetPlayer==null || targetPlayer.hasLost())
            chooseTargetPlayer();
        GameObject target = getBestTarget(targetPlayer);
        if(target==null)
        {
            infantryDefend();
            return;
        }
        for(Infantry i : owner.getInfantry())
        {
            i.setGoal(target);
        }
        
    }

    private int infantryCount()
    {
        return owner.getInfantry().size();
    }

    private void infantryDefend()
    {
        if(enemies.isEmpty())
        {
            int count=0;
            for(Infantry i : owner.getInfantry())
            {
                if(count%2==0)
                    i.setGoal(owner.getHQ());
                else
                    i.setGoal(owner.getBarracks().iterator().next());
                count ++;
            }
            return;
        }
        
        for(Infantry i : owner.getInfantry())
        {
            i.setGoal(enemies.peek());
        }
    }

    private void purchaseInfantry() 
    {
        if(owner.getGoldAmount()<150)
            return;
        Barracks b = null;
        for(Barracks bar : owner.getBarracks())
        {
            if(bar.isBuilt())
            {
                b=bar;
                break;
            }
        }
        if(b==null)
            return;//nowhere to build
        b.executeCommand(b.giveableCommands[0]);
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

    private void sellVillagers() {
        Villager v =owner.getVillagers().iterator().next();
        if(v!=null)
            v.executeCommand(v.giveableCommands[1]);
    }

    private void updateEnemies() {
        while(!enemies.isEmpty() && enemies.peek().getHealth()<=0)
            enemies.pop();
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
