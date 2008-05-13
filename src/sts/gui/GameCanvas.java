/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Phillip Cohen
 */
public class GameCanvas extends Canvas
{
    public GameCanvas()
    {
        setSize( 950, 700 );
        setVisible( true );
    }

    @Override
    public void paint( Graphics g )
    {
        this.createBufferStrategy( 2 );
        BufferStrategy strategy = getBufferStrategy();
        draw( strategy.getDrawGraphics() );
        strategy.show();
        repaint();
    }
    int x = 9;

    private void draw( Graphics g )
    {
        g.fillRect( ++x, 50, 150, 150 );
        g.dispose();
    }

    /**
     * Included to prevent the clearing of the screen between repaints.
     */
    @Override
    public void update( Graphics g )
    {
        paint( g );
    }
}
