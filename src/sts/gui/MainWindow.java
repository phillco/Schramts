/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Frame;
import java.awt.Graphics;

/**
 *
 * @author Phillip Cohen
 */
public class MainWindow extends Frame
{
    public MainWindow()
    {
        setName( "RTS!" );
        setResizable( false );
        setVisible( true );
        add( new GameCanvas() );
        pack();
    }
}
