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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import sts.Local;
import sts.game.GameObject;

/**
 *
 * @author Phillip Cohen
 */
public class GameCanvas extends Canvas implements MouseListener
{
    private Font hudFont = new Font( "Tahoma", Font.PLAIN, 14 );

    private Font bigHudFont = new Font( "Tahoma", Font.BOLD, 16 );

    public GameCanvas()
    {
        setSize( 950, 700 );
        setVisible( true );
        addMouseListener( this );
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

    /**
     * Draws all the game's graphics.
     */
    private void draw( Graphics2D g )
    {
        Local.getGame().draw( g );

        g.setColor( Color.black );
        if ( null != Local.getLocalPlayer() )
        {
            g.setFont( bigHudFont );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getName(), 5, getHeight() - 15, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            g.setFont( hudFont );
            ExtendedGraphics.drawText( g, "Gold: " + Local.getLocalPlayer().getGoldAmount(), 5, getHeight() - 1, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
        }
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

    public void mouseClicked( MouseEvent e )
    {
    }

    public void mousePressed( MouseEvent e )
    {
        Local.setSelectedObjects( Local.getGame().getObjectsWithinArea( e.getX(), e.getY() ) );
        for ( GameObject go : Local.getSelectedObjects() )
        {
            System.out.println( go );
        }
    }

    public void mouseReleased( MouseEvent e )
    {
    }

    public void mouseEntered( MouseEvent e )
    {
    }

    public void mouseExited( MouseEvent e )
    {
    }
}
