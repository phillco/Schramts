/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import sts.Main;

/**
 *
 * @author Phillip Cohen
 */
public class MainWindow extends Frame
{
    public MainWindow()
    {
        setTitle( "Schramts" );
        addWindowListener( new OurFrameAdapter() );
        setResizable( false );
        setVisible( true );
        add( new GameCanvas() );
        pack();
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
}
