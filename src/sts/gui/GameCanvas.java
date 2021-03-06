/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.Set;
import sts.Local;
import sts.Main;
import sts.game.Command;
import sts.game.ProductionCommand;
import sts.game.GameObject;
import sts.game.GroupCommand;
import sts.game.ProductionBuilding;
import sts.game.Unit;
import sts.game.Location;

/**
 * The hackiness of this class compares favorably with the national debt.
 * @author Phillip Cohen
 */
public class GameCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener
{
    private Font hudFont = new Font( "Verdana", Font.ITALIC, 12 );

    private Font bigHudFont = new Font( "Tahoma", Font.BOLD, 16 );

    private GradientPaint hudGradient = new GradientPaint( 0, 0, new Color( 250, 250, 250 ), 400, 100, new Color( 210, 210, 225 ), true );

    private int selectedButton = -1;

    private Location box1 = null,  box2 = null;

    private int youAreHereTimer = 130;

    public static final int DEFAULT_WIDTH = 1000;

    public static final int DEFAULT_HEIGHT = 800;

    /**
     * The current amount of FPS. Updated in <code>paint</code>.
     * @since December 18, 2007
     */
    int lastFPS = 0;

    /**
     * The number of times that the paint method has been called, for FPS.
     * @since January 10, 2008
     */
    int paintCount = 0;

    /**
     * The time stored at the beginning of the last call to paint; Used for FPS.
     * @since December 15, 2007
     */
    long timeOfLastRepaint;

    public GameCanvas()
    {
        setSize( DEFAULT_WIDTH, DEFAULT_HEIGHT );
        setVisible( true );
        addMouseListener( this );
        addMouseMotionListener( this );
        addKeyListener( this );
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
        // Calculate FPS.
        if ( ++paintCount % 10 == 0 )
        {
            long timeSinceLast = -timeOfLastRepaint + ( timeOfLastRepaint = System.currentTimeMillis() );
            if ( timeSinceLast > 0 )
                lastFPS = (int) ( 10000.0 / timeSinceLast );
        }
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

        if ( box1 != null && box2 != null )
        {
            g.setColor( Local.getLocalPlayer().getColor() );
            g.drawRect( Math.min( box1.getX(), box2.getX() ), Math.min( box1.getY(), box2.getY() ),
                        Math.abs( box2.getX() - box1.getX() ), Math.abs( box2.getY() - box1.getY() ) );
        }

        if ( --youAreHereTimer > 0 )
        {
            g.setColor( Local.getLocalPlayer().getColor() );
            g.setFont( bigHudFont );
            ExtendedGraphics.drawText( g, "You are here!", Local.getLocalPlayer().getHQ().getX(), Local.getLocalPlayer().getHQ().getY(), this );
        }

        // Draw HUD.
        if ( null != Local.getLocalPlayer() )
        {
            x += 10;
            g.setColor( Color.black );
            g.setFont( bigHudFont );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getName(), x, y + 25, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            g.setFont( hudFont );
            g.setColor( Color.darkGray );
            ExtendedGraphics.drawText( g, Local.getLocalPlayer().getGoldAmount() + " gold", x + 4, y + 42, ExtendedGraphics.HorizontalAlign.LEFT, ExtendedGraphics.VerticleAlign.TOP );
            g.drawString( lastFPS + " FPS", 8, getHeight() - 8 );

            // Draw the selected object's properties.
            if ( Local.getSelectedObjects().size() > 0 )
            {
                GameObject go = Local.getSelectedObject();
                ExtendedGraphics.drawText( g, go.getOwningPlayer().getName() + "'s " + go.getName(), 430, getHeight() - 70, ExtendedGraphics.HorizontalAlign.RIGHT, ExtendedGraphics.VerticleAlign.TOP );

                // Draw healthbar.
                g.fillRect( 300, getHeight() - 10, (int) ( 130 * ( (double) go.getHealth() ) / go.getMaxHealth() ), 8 );
                g.drawRect( 300, getHeight() - 10, 130, 8 );



                if ( go.getOwningPlayer() == Local.getLocalPlayer() && !( go instanceof ProductionBuilding && !( (ProductionBuilding) go ).isBuilt() ) )
                {
                    // Draw command buttons.
                    int buttonIndex = 0;
                    for ( ProductionCommand c : go.getProductionCommands() )
                    {
                        x = ( 440 - 53 * ( buttonIndex + 1 ) );
                        g.setColor( ( selectedButton == buttonIndex ? Color.white : Color.lightGray ) );
                        g.fillRect( x, y + 40, 45, 45 );
                        g.setColor( Color.darkGray );
                        g.drawRect( x, y + 40, 45, 45 );
                        ImageHandler.drawImage( g, x + 9, y + 49, go.getOwningPlayer().getColor(), c.getQueuedImage(), false );
                        buttonIndex++;
                    }

                    // Draw button's tooltip.
                    if ( go != null && selectedButton != -1 && go.getProductionCommands().length > selectedButton )
                    {
                        ExtendedGraphics.drawText( g, go.getProductionCommands()[selectedButton].getName() + ( Local.getSelectedObject().getProductionCommands()[selectedButton].getCost() > 0 ? " (costs " : " (gives " ) + Math.abs( Local.getSelectedObject().getProductionCommands()[selectedButton].getCost() ) + " gold)", 350, getHeight() - 1, ExtendedGraphics.HorizontalAlign.RIGHT, ExtendedGraphics.VerticleAlign.BOTTOM );
                    }

                    // Draw queued units.
                    if ( go instanceof ProductionBuilding )
                    {
                        x -= 15;
                        ProductionBuilding b = (ProductionBuilding) go;
                        for ( ProductionBuilding.ItemInQueue i : b.getProductionQueue() )
                        {
                            //figure out what color you want
                            float targetHue, targetSat;
                            float[] dummy =
                            {
                                0f, 0f, 0f
                            };
                            targetHue = Color.RGBtoHSB( go.getOwningPlayer().getColor().getRed(), go.getOwningPlayer().getColor().getGreen(), go.getOwningPlayer().getColor().getBlue(), dummy )[0];
                            targetSat = 1 - ( (float) i.timeLeft ) / i.type.getTimeToMake();

                            Color c = Color.getHSBColor( targetHue, targetSat, ( ( (float) i.timeLeft ) / i.type.getTimeToMake() ) );

                            ImageHandler.drawImage( g, x, y + 30, c, i.type.getQueuedImage(), false );
                            x -= 15;
                        }
                    }
                }

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
        if ( selectedButton != -1 )
            return;

        // What's there?
        Set<GameObject> targets = Local.getGame().getObjectsWithinArea( e.getX() + Local.getViewingX(), e.getY() + Local.getViewingY(), 8, 8 );

        // Left clicked on something; select it.
        if ( e.getButton() == MouseEvent.BUTTON1 )
        {
            Local.setSelectedObjects( targets );
            box1 = new Location( e.getX(), e.getY() );
            return;
        }


        // Right clicked.
        int x = e.getX() + Local.getViewingX();
        int y = e.getY() + Local.getViewingY();
        for ( GameObject go : Local.getSelectedObjects() )
        {
            // Ours to command?
            if ( go instanceof Unit && go.getOwningPlayer() == Local.getLocalPlayer() )
            {
                Unit u = (Unit) go;
                u.clearCommands();

                if ( targets.isEmpty() )
                    u.giveCommand( new Command( true, new Location( x, y ) ), true );
                else
                    u.giveCommand( new GroupCommand( true, new Location( x, y ), targets ), true );
                x += go.getWidth() + 1;
                if ( ( x - ( e.getX() + Local.getViewingX() ) ) / ( go.getWidth() + 1 ) > 6 )
                {
                    x = e.getX() + Local.getViewingX();
                    y += go.getHeight() + 1;
                }
            }
            else if ( go instanceof ProductionBuilding && go.getOwningPlayer() == Local.getLocalPlayer() )
            {
                ( (ProductionBuilding) go ).setRalleyPoint( e.getX(), e.getY() );
            }
        }
    }

    public void mouseReleased( MouseEvent e )
    {
        // Button pushed.
        if ( selectedButton != -1 && Local.getSelectedObjects().size() > 0 && Local.getSelectedObject().getProductionCommands().length > selectedButton )
        {
            //     if ( Local.getLocalPlayer().getGoldAmount() >= Local.getSelectedObject().getGiveableCommands()[selectedButton].getCost() )
            Local.getSelectedObject().giveCommand( Local.getSelectedObject().getProductionCommands()[selectedButton] );
        }

        // Box dragged!
        if ( box1 != null && box2 != null )
            Local.setSelectedObjects( Local.getGame().getObjectsWithinArea( Math.max( box1.getX(), box2.getX() ) + Local.getViewingX(), Math.max( box1.getY(), box2.getY() ) + Local.getViewingY(), Math.abs( box2.getX() - box1.getX() ), Math.abs( box2.getY() - box1.getY() ) ) );
        box1 = box2 = null;
    }

    public void mouseEntered( MouseEvent e )
    {
    }

    public void mouseExited( MouseEvent e )
    {
    }

    public void mouseDragged( MouseEvent e )
    {
        // Dragging a box.
        box2 = new Location( e.getX(), e.getY() );
    }

    public void mouseMoved( MouseEvent e )
    {
        selectedButton = -1;

        int dX = 0, dY = 0;

        if ( e.getX() > getWidth() - 200 )
            dX = (int) ( Math.pow( Math.max( 0, e.getX() - ( getWidth() - 200 ) ), 1 ) / 4 );
        else if ( e.getX() < 200 )
            dX = (int) ( Math.pow( Math.max( 0, 200 - e.getX() ), 1 ) / -4 );

        if ( e.getY() > getHeight() - 200 )
            dY = (int) ( Math.pow( Math.max( 0, e.getY() - ( getHeight() - 200 ) ), 1 ) / 4 );
        else if ( e.getY() < 200 )
            dY = (int) ( Math.pow( Math.max( 0, 200 - e.getY() ), 1 ) / -4 );


        Local.setViewingX( Local.getViewingX() + dX );
        Local.setViewingY( Local.getViewingY() + dY );


        // Selecting a button?
        if ( e.getY() > getHeight() - 60 && e.getY() < getHeight() - 15 && e.getX() > 440 - ( 53 * 4 ) && e.getX() < 440 )
        {
            selectedButton = ( 440 - e.getX() ) / 53;
            return;
        }
    }

    public void keyTyped( KeyEvent e )
    {
    }

    public void keyPressed( KeyEvent e )
    {
        switch ( e.getKeyCode() )
        {
            case KeyEvent.VK_ESCAPE:
                Main.quit();
                break;
            case KeyEvent.VK_T:
                Local.setSelectedObject( Local.getLocalPlayer().getHQ() );
                break;
        }
    }

    public void keyReleased( KeyEvent e )
    {
    }

    @Override
    public void setSize( int width, int height )
    {
        super.setSize( width, height );
        Local.setViewingArea( width, height );
    }

    @Override
    public void setSize( Dimension d )
    {
        super.setSize( d );
        Local.setViewingArea( d.width, d.height );
    }
}
