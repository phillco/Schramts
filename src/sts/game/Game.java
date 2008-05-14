package sts.game;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Phillip Cohen
 */
public class Game
{
    private ConcurrentLinkedQueue<Player> players = new ConcurrentLinkedQueue<Player>();
    final static int LEVEL_WIDTH = 1000, LEVEL_HEIGHT = 1000;

    public Game()
    {
    }
    
    
    public void addPlayer(Player p)
    {
        players.add( p );
    }

    void prepareLevel()
    {
        
    }
}
