/*
 * RTS
 * 
 */
package sts;

import sts.game.Game;
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
        game = new Game();
        window = new MainWindow();
    }
}
