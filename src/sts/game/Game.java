package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Phillip Cohen
 */
public class Game
{
    private ConcurrentLinkedQueue<Player> players = new ConcurrentLinkedQueue<Player>();

    private Player Nature = new Player( Color.white, "Nature" );

    final static int LEVEL_WIDTH = 1000,  LEVEL_HEIGHT = 1000;

    public Game()
    {
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

    public void addPlayer( Player p )
    {
        players.add( p );
    }

    public GameObject[] getObjectsWithinArea( int x, int y, int radius )
    {
        throw new UnsupportedOperationException( "Not yet implemented (by Phillip)." );
    }

    void prepareLevel()
    {
    }
}
