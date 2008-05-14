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

    private ExtendedGraphics extendedGraphics;

    public GameCanvas()
    {
        setSize( 950, 700 );
        extendedGraphics = new ExtendedGraphics( this );
        setVisible( true );
    }

    @Override
    public void paint( Graphics g )
    {
        this.createBufferStrategy( 2 );
        BufferStrategy strategy = getBufferStrategy();
        extendedGraphics.setG( strategy.getDrawGraphics() );
        draw( extendedGraphics );
        strategy.show();
        repaint();
    }
    int x = 9;

    /**
     * Draws all the game's graphics.
     */
    private void draw( ExtendedGraphics eg )
    {
        eg.getG().setColor( Color.black );
        if ( null != Local.getLocalPlayer() )
        {
            eg.getG().setFont( bigHudFont );
            eg.drawText( Local.getLocalPlayer().getName(), 5, -15, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            eg.getG().setFont( hudFont );
            eg.drawText( "Gold: " + Local.getLocalPlayer().getGoldAmount(), 5, -1, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );            
        }
        eg.getG().fillRect( ++x, 50, 150, 150 );

        eg.getG().dispose();
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
