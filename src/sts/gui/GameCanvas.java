/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import sts.Local;

/**
 *
 * @author Phillip Cohen
 */
public class GameCanvas extends Canvas
{
    private Font hudFont = new Font( "Tahoma", Font.PLAIN, 14 );

    private Font bigHudFont = new Font( "Tahoma", Font.BOLD, 16 );

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
        draw( (Graphics2D) strategy.getDrawGraphics() );
        strategy.show();
        repaint();
    }
    int x = 9;

    /**
     * Draws all the game's graphics.
     */
    private void draw( Graphics2D g )
    {
        g.setColor( Color.black );
        if ( null != Local.getLocalPlayer() )
        {
            g.setFont( bigHudFont );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getName(), 5, -15, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            g.setFont( hudFont );
            ExtendedGraphics.drawText( g, "Gold: " + Local.getLocalPlayer().getGoldAmount(), 5, -1, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
        }
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
