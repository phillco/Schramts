/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import sts.Local;
import sts.Main;
import sts.game.Command;
import sts.game.GameObject;

/**
 * The hackiness of this class compares favorably with the national debt.
 * @author Phillip Cohen
 */
public class GameCanvas extends Canvas implements MouseListener, MouseMotionListener
{
    private Font hudFont = new Font( "Verdana", Font.ITALIC, 12 );

    private Font bigHudFont = new Font( "Tahoma", Font.BOLD, 16 );

    private GradientPaint hudGradient = new GradientPaint( 0, 0, new Color( 250, 250, 250 ), 400, 100, new Color( 210, 210, 225 ), true );

    private int selectedButton = -1;

    public GameCanvas()
    {
        setSize( Local.getGame().getLevelWidth(), Local.getGame().getLevelHeight() + 100 );
        setVisible( true );
        addMouseListener( this );
        addMouseMotionListener( this );
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

        drawHUD( g, 0, getHeight() - 100 );
        g.dispose();
    }

    private void drawHUD( Graphics2D g, int x, int y )
    {
        g.setPaint( hudGradient );
        g.fillRect( x, y, 450, 100 );
        g.setColor( Color.lightGray );
        g.drawRect( x, y, 450, 100 );

        if ( null != Local.getLocalPlayer() )
        {
            x += 10;
            g.setColor( Color.black );
            g.setFont( bigHudFont );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getName(), x, y + 25, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            g.setFont( hudFont );
            g.setColor( Color.darkGray );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getGoldAmount() + " gold", x + 4, y + 42, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );

            if ( Local.getSelectedObjects().size() > 0 )
            {
                GameObject go = Local.getSelectedObjects().iterator().next();
                ExtendedGraphics.drawText( g, go.getOwningPlayer().getName() + "'s " + go.getName(), 430, getHeight() - 70, ExtendedGraphics.HorizontalAlign.RIGHT, ExtendedGraphics.VerticleAlign.TOP );

                // Draw command boxes.
                int buttonIndex = 0;
                for ( Command c : go.getGiveableCommands() )
                {
                    x = ( 440 - 53 * ( buttonIndex + 1 ) );
                    g.setColor( ( selectedButton == buttonIndex ? Color.white : Color.lightGray ) );
                    g.fillRect( x, y + 40, 45, 45 );
                    g.setColor( Color.darkGray );
                    g.drawRect( x, y + 40, 45, 45 );
                    buttonIndex++;
                }

                if ( go != null && selectedButton != -1 && go.getGiveableCommands().length > selectedButton )
                    ExtendedGraphics.drawText( g, go.getGiveableCommands()[selectedButton].getName(), 350, getHeight() - 1, ExtendedGraphics.HorizontalAlign.RIGHT, ExtendedGraphics.VerticleAlign.BOTTOM );
            }
        }
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
        if ( selectedButton == -1 )
        {
            Local.setSelectedObjects( Local.getGame().getObjectsWithinArea( e.getX(), e.getY() ) );
        }
    }

    public void mouseReleased( MouseEvent e )
    {
        if ( selectedButton != -1 )
            Main.fatalError( Local.getSelectedObject().getGiveableCommands()[selectedButton].getName() );
    }

    public void mouseEntered( MouseEvent e )
    {
    }

    public void mouseExited( MouseEvent e )
    {
    }

    public void mouseDragged( MouseEvent e )
    {
    }

    public void mouseMoved( MouseEvent e )
    {
        selectedButton = -1;

        // Selecting a button?
        if ( e.getY() > getHeight() - 60 && e.getY() < getHeight() - 15 && e.getX() > 440 - ( 53 * 4 ) && e.getX() < 440 )
        {
            selectedButton = ( 440 - e.getX() ) / 53;
            return;
        }
    }
}
