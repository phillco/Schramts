package sts.game;

import java.util.Set;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import sts.Main;

/**
 *
 * @author Phillip Cohen
 */
public class Game
{
    final static int LEVEL_WIDTH = 1000,  LEVEL_HEIGHT = 1000;

    private ConcurrentLinkedQueue<Player> players;

    private Player Nature = new Player( Color.white, "Nature" );

    private RunnerThread runnerThread = new RunnerThread();

    public Game( Set<Player> p )
    {
        this.players = new ConcurrentLinkedQueue<Player>( p );
        prepareLevel();
        runnerThread.startLoop();
    }

    public void act()
    {
        for ( Player p : players )
            p.act();
    }

    public void draw( Graphics2D g )
    {
        for ( Player p : players )
            p.draw( g );
    }

    /**
     * Returns a set of objects whole top-left coordinates are fully within a circle of the given radius.
     */
    public Set<GameObject> getObjectsWithinArea( int x, int y, int radius )
    {
        Set<GameObject> objects = new HashSet<GameObject>();
        for ( Player p : players )
        {
            for ( GameObject go : p.getOwnedObjects() )
            {
                if ( Math.sqrt( Math.pow( go.getX() - x, 2 ) + Math.pow( go.getY() - y, 2 ) ) < radius )
                    objects.add( go );
            }
        }

        return objects;
    }

    /**
     * Sets up the level for the players, including the placement of their HQs and resources.
     * @deprecated unfinished
     */
    private void prepareLevel()
    {
        players.peek().giveObject( new HQ( 90, 90, players.peek() ) );
//        throw new UnsupportedOperationException( "Not yet implemented (http://www.assembla.com/spaces/Schramts/tickets/5)." );
    }

    /**
     * The thread that runs the game.
     */
    private class RunnerThread extends Thread
    {
        /**
         * The time in milliseconds between each action loop.
         */
        private final static int PERIOD = 10;

        /**
         * Whether the thread is enabled. Always true unless the thread is being destroyed.
         */
        private boolean enabled = true;

        public RunnerThread()
        {
            super( "Game runner thread" );
            setPriority( Thread.MAX_PRIORITY );
        }

        /**
         * Starts/resumes the game loop.
         */
        public void startLoop()
        {
            enabled = true;
            start();
        }

        /**
         * Stops the game loop and thread nicely. Note: the current timestep will be completed first.
         */
        public void stopLoop()
        {
            enabled = false;
        }

        /**
         * Starts an infinite loop which acts the game, sleeps, and repeats.
         * 
         * The amount of time to sleep is set by <code>period></code>.
         * If the game is running behind, it uses this sleep time as a cushion.
         */
        @Override
        public void run()
        {
            long timeOfLast = System.currentTimeMillis();
            while ( enabled )
            {
                try
                {
                    timeOfLast = System.currentTimeMillis();

                    if ( shouldRun() )
                        act();

                    while ( enabled && System.currentTimeMillis() - timeOfLast < PERIOD )
                        Thread.sleep( 2 );

                }
                catch ( InterruptedException ex )
                {
                    Main.fatalError( "Game loop interrupted while sleeping.", ex );
                }
            }
        }

        /**
         * Returns whether the game should run in the next step or not.
         */
        public boolean shouldRun()
        {
            return enabled;
        }

        public boolean isRunning()
        {
            return enabled;
        }
    }
}
