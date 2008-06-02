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

    private static State earlyEconomic = new State( 10, 0, InfantryState.DEFEND, VillagerState.MINE );

    private static State defensive = new State( 7, 3, InfantryState.DEFEND, VillagerState.REPAIR );

    private static State branchingOut = new State( 10, 0, InfantryState.DEFEND, VillagerState.BUILD );

    private static State buildingUp = new State( 10, 5, InfantryState.DEFEND, VillagerState.MINE );

    private static State offensive = new State( 8, 8, InfantryState.ATTACK_OUT, VillagerState.MINE );

    private static State endGame = new State( 3, 20, InfantryState.ATTACK_OUT, VillagerState.REPAIR );

    private static State panicDefense = new State( 8, 20, InfantryState.ATTACK_HOME, VillagerState.REPAIR );

    private final Stack<GameObject> enemies;//everyone we want to kill, because they attacked us first.

    private Player targetPlayer;

    public PlayingAI( Player owner )
    {
        this.owner = owner;
        waiting = state = earlyEconomic;
        enemies = new Stack<GameObject>();
        targetPlayer = null;
    }

    public void act()
    {
        if ( owner.hasLost() )
            return;// :-(
        checkState();
        updateEnemies();
        if ( villagerCount() < state.villagerThreshhold )
            purchaseVillagers();
        else if ( villagerCount() - state.villagerThreshhold > 2 )
            sellVillagers();
        if ( infantryCount() < state.infantryThreshhold )
            purchaseInfantry();
        switch ( state.villagerState )
        {
            case MINE:
                assignVillagersToGold();
                break;
            case BUILD:
                assignVillagerToBuild();
                break;
            case REPAIR:
                assignVillagersToRepair();
                break;
        }
        switch ( state.infantryAgressive )
        {
            case ATTACK_OUT:
                infantryAttack();
                break;
            case ATTACK_HOME:
                infantryAttackHome();
                break;
            case DEFEND:
                infantryDefend();
        }
        actForState();

    }
    private Villager carpenter;

    private void actForState()
    {
        if ( state == earlyEconomic )
            return;//everything is taken care of
        if ( state == buildingUp || state == offensive || state == endGame )
        {
            if ( owner.getBarracks().isEmpty() || !owner.getBarracks().iterator().next().isBuilt() )
                assignVillagerToBuild();
        }

    }

    private void assignVillagerToBuild()
    {
        if ( owner.getGoldAmount() < 800 )//ok, so I hard coded a value.  So shoot me
            return;//don't bother trying to build, 
        if ( carpenter == null || carpenter.getHealth() <= 0 )
        {
            carpenter = owner.getVillagers().iterator().next();
            carpenter.giveCommand( new Command( true, owner.getHQ().getLocation().translate( 0, -75 ) ), true );
        }
        if ( owner.getBarracks().isEmpty() )//no barracks, build one.
        {
            if ( carpenter.arrived )
                carpenter.giveCommand( carpenter.productionCommands[0] );
        }
        else
            carpenter.giveCommand( new Command( true, owner.getBarracks().iterator().next() ), true );
    }

    private void assignVillagersToGold()
    {
        for ( GameObject go : owner.getOwnedObjects() )
        {
            if ( go instanceof Villager )
            {
                Villager v = (Villager) go;
                if ( v.getCurrentCommand() == null || !( v.getCurrentCommand().getObject() instanceof GoldPile ) )
                    v.findNewGold();
            }
        }
    }

    private void assignVillagersToRepair()
    {
        Set<ProductionBuilding> needsRepair = new HashSet<ProductionBuilding>();
        for ( Barracks b : owner.getBarracks() )
        {
            if ( b.needsRepair() )
                needsRepair.add( b );
        }
        if ( owner.getHQ() != null && owner.getHQ().needsRepair() )
            needsRepair.add( owner.getHQ() );
        if ( needsRepair.isEmpty() )
        {
            for ( Villager v : owner.getVillagers() )
                v.findNewGold();
            return;
        }
        int count = 0;
        ArrayList<ProductionBuilding> linearAccess = new ArrayList<ProductionBuilding>( needsRepair );
        for ( Villager v : owner.getVillagers() )
        {
            v.giveCommand( new Command( true, linearAccess.get( count ) ), true );
            count++;
            count %= needsRepair.size();
        }
    }

    private void checkState()
    {
        if ( state == earlyEconomic )
        {
            if ( villagerCount() >= state.villagerThreshhold )
            {
                state = branchingOut;
                return;
            }
        }

        if ( state == branchingOut )
        {
            for ( Barracks b : owner.getBarracks() )
            {
                if ( b.isBuilt() )
                {
                    state = buildingUp;
                    return;
                }
            }
        }

        if ( state == buildingUp )
        {
            if ( owner.getInfantry().size() >= buildingUp.infantryThreshhold )
            {
                state = offensive;
            }
        }

        if ( state == offensive )
        {
            if ( Local.getGame().getNature().getGoldPiles().isEmpty() )
            {
                state = endGame;
                return;
            }
        }
        if ( state == panicDefense )
        {
            if ( enemies.isEmpty() )
                state = waiting;
        }
    }

    @Override
    public void notifyAboutAttack( int x, int y, GameObject attacker )
    {
        enemies.push( attacker );
        if ( state == panicDefense )
            return;//we're already fighting back
        //   if(enemies.size()>2)
        {
            waiting = state;
            state = panicDefense;
        }
    }

    private void chooseTargetPlayer()
    {
        ArrayList<Player> possible = new ArrayList<Player>( Local.getGame().getPlayers() );
        possible.remove( owner );
        if( possible.isEmpty() )
        {
            targetPlayer = null;
            return;
        }
        targetPlayer = possible.get( (int) ( Math.random() * possible.size() ) );
    }

    private GameObject getBestTarget( Player player )
    {
        if ( !player.getInfantry().isEmpty() )
            return player.getInfantry().iterator().next();
        if ( !player.getVillagers().isEmpty() )
            return player.getVillagers().iterator().next();
        if ( !player.getBarracks().isEmpty() )
            return player.getBarracks().iterator().next();
        return player.getHQ();
    }

    private void infantryAttack()
    {
        if ( targetPlayer == null || targetPlayer.hasLost() )
            chooseTargetPlayer();
        GameObject target = getBestTarget( targetPlayer );
        if ( target == null )
        {
            infantryDefend();
            return;
        }
        for ( Infantry i : owner.getInfantry() )
        {
            i.clearCommands();
            i.giveCommand( new Command( true, target ), true );
        }
    }

    private void infantryAttackHome()
    {
        if ( enemies.isEmpty() )
            infantryDefend();
        GameObject enemy = enemies.size() == 0 ? null : enemies.peek();
        for ( Infantry i : owner.getInfantry() )
        {
            i.clearCommands();
            if ( enemy != null )
                i.giveCommand( new Command( true, enemy ), true );
        }
    }

    private int infantryCount()
    {
        return owner.getInfantry().size();
    }

    private void infantryDefend()
    {
        if ( enemies.isEmpty() )
        {
            int count = 0;
            for ( Infantry i : owner.getInfantry() )
            {
                i.clearCommands();
                if ( count % 2 == 0 )
                    i.giveCommand( new Command( true, owner.getHQ() ), true );
                else
                    i.giveCommand( new Command( true, owner.getBarracks().iterator().next() ), true );
                count++;
            }
            return;
        }
    }

    private void purchaseInfantry()
    {
        if ( owner.getGoldAmount() < 150 )
            return;
        Barracks b = null;
        for ( Barracks bar : owner.getBarracks() )
        {
            if ( bar.isBuilt() )
            {
                b = bar;
                break;
            }
        }
        if ( b == null )
            return;//nowhere to build
        if ( b.queueLength() == 0 )
            b.giveCommand( b.productionCommands[0] );
    }

    private void purchaseVillagers()
    {
        if ( owner.getGoldAmount() < 100 )
            return;
        HQ hq = owner.getHQ();
        if ( hq == null )//uh-oh, no hq
            return;
        if ( hq.queueLength() < 1 )
            hq.giveCommand( hq.getProductionCommands()[0] );
    }

    private void sellVillagers()
    {
        Villager v = owner.getVillagers().iterator().next();
        if ( v != null )
            v.giveCommand( v.productionCommands[1] );
    }

    private void updateEnemies()
    {
        while ( !enemies.isEmpty() && enemies.peek().getHealth() <= 0 )
            enemies.pop();
    }

    private int villagerCount()
    {
        return owner.getVillagers().size();
    }

    private static enum VillagerState
    {
        MINE, BUILD, REPAIR

    };

    private static enum InfantryState
    {
        ATTACK_OUT, ATTACK_HOME, DEFEND

    };

    private static class State
    {
        public final  int villagerThreshhold,      infantryThreshhold;

        public final InfantryState infantryAgressive;

        public final VillagerState villagerState;

        public State( int villagerThreshhold, int infantryThreshhold, InfantryState infantryAgressive, VillagerState villagerState )
        {
            this.villagerThreshhold = villagerThreshhold;
            this.infantryThreshhold = infantryThreshhold;
            this.infantryAgressive = infantryAgressive;
            this.villagerState = villagerState;
        }
    }
}