/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import sts.Main;
import sts.Settings;

/**
 *
 * @author Phillip Cohen
 */
public class MainWindow extends Frame
{
    private GameCanvas panel;

    public MainWindow()
    {
        setTitle( "Schramts" );
        addWindowListener( new OurFrameAdapter() );
        setResizable( false );
        setVisible( true );
        panel = new GameCanvas();
        add( panel );
        pack();

        // [PC] This trick gives the canvas focus, so it immediately can receive key events.
        SwingUtilities.invokeLater( new Runnable()
                            {
                                public void run()
                                {
                                    panel.requestFocusInWindow();
                                }
                            } );

        updateFullscreen();
    }

    /**
     * A simple handler for the frame's window buttons.
     */
    private class OurFrameAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing( WindowEvent e )
        {
            dispose();
            Main.quit();
        }

        @Override
        public void windowGainedFocus( WindowEvent e )
        {
        }
    }

    private void updateFullscreen()
    {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Set fullscreen mode if we're not already.
        if ( Settings.isUseFullscreen() && graphicsDevice.getFullScreenWindow() != this )
        {
            dispose();
            setUndecorated( true );
            panel.setSize( graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight() );
            setSize( graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight() );
            // Hide the cursor.
//            Image cursorImage = Toolkit.getDefaultToolkit().getImage( "xparent.gif" );
//            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImage, new Point( 0, 0 ), "" );
//            setCursor( blankCursor );
            pack();
        /**
         * [PC] This degrades performance horribly.
         * 
         * graphicsDevice.setFullScreenWindow( this ); 
         */
        }
        // Set windowed mode if we're not already.
        else if ( ( panel.getSize().width != GameCanvas.DEFAULT_WIDTH ) || ( panel.getSize().height != GameCanvas.DEFAULT_HEIGHT ) )
        {
            setVisible( false );
            dispose();
            setUndecorated( false );
            // Show the cursor.
//            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
            setSize( GameCanvas.DEFAULT_WIDTH, GameCanvas.DEFAULT_HEIGHT );

        /**
         * [PC] Same thing.
         * 
         * graphicsDevice.setFullScreenWindow( null );
         */
        }
        setVisible( true );
    }
}
