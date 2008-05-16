/*
 * RTS
 * 
 */
package sts;

import java.awt.Color;
import sts.game.Game;
import sts.game.Player;
import sts.gui.MainWindow;

/**
 *
 * @author Phillip Cohen
 */
public class Main
{
    static Game game;

    static MainWindow window;   

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args )
    {
        Player local = new Player( Color.red, "Bob" );
        game = new Game();
        game.addPlayer( local );
        Local.setLocalPlayer( local );
        window = new MainWindow();
    }
    
    public static void quit()
    {
        System.exit( 0);
    }
}
