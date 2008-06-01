/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.Graphics2D;
import java.util.LinkedList;
import sts.Local;
import sts.Util;
import sts.gui.ImageHandler;

/**
 *
 * @author AK90454
 */
public abstract class Unit extends GameObject
{
    protected LinkedList<Command> commandQueue = new LinkedList<Command>();

    boolean arrived;

    protected double dx,  dy;

    protected double mySpeedRate = Util.getRandomGenerator().nextDouble() * 0.4 + 0.8;

    public Unit( int x, int y, int dx, int dy, int width, int height, int health, Player player )
    {
        super( x, y, width, height, health, player );
        this.dx = dx;
        this.dy = dy;
        arrived = false;
    }

    @Override
    public void act()
    {
        move();
    }

    @Override
    public void draw( Graphics2D g )
    {
        // Draw a flag if we're moving to a specific spot.
        if ( !commandQueue.isEmpty() && !arrived && getOwningPlayer() == Local.getLocalPlayer() && commandQueue.peek().isGivenByPlayer() )
            ImageHandler.drawDestination( g, commandQueue.peek().getLocation().getX(), commandQueue.peek().getLocation().getY(), getOwningPlayer().getColor() );
    }

    /**
     * figures out what dx and dy to have to make it to the destination
     */
    protected void calculateSpeed()
    {
        if ( commandQueue.isEmpty() )
        {
            dx = dy = 0;
            return;
        }
        double angle = Math.atan2( getY() - commandQueue.peek().getLocation().getY(), getX() - commandQueue.peek().getLocation().getX() ) + Math.PI;
        dx = ( getMaxSpeed() * Math.cos( angle ) ) * mySpeedRate;
        dy = ( getMaxSpeed() * Math.sin( angle ) ) * mySpeedRate;
    }

    public void clearCommands()
    {
        commandQueue.clear();
    }

    public void giveCommand( Command c, boolean urgent )
    {
        if ( c instanceof GroupCommand )
        {
            giveCommand( processGroupCommand( (GroupCommand) c ), urgent );
            return;
        }

        if ( urgent )
        {
            commandQueue.addFirst( c );
            arrived = false;
        }
        else
            commandQueue.addLast( c );
    }

    public Command getCurrentCommand()
    {
        return commandQueue.peek();
    }

    public void nextCommand()
    {
        commandQueue.removeFirst();
        arrived = false;
    }

    public Command processGroupCommand( GroupCommand command )
    {
        // Just go there.
        return new Command( command.isGivenByPlayer(), command.getLocation() );
    }

    /**
     * Returns how fast this object can travel.  Abstract so that each type of unit
     * can have its own speed
     */
    public abstract int getMaxSpeed();

    private void move()
    {
        if ( !arrived && !commandQueue.isEmpty() )
        {
            calculateSpeed();
            int destX = commandQueue.peek().getLocation().getX(), destY = commandQueue.peek().getLocation().getY();

            // Arrived yet?
            if ( Math.sqrt( ( getX() - destX ) * ( getX() - destX ) +
                            ( getY() - destY ) * ( getY() - destY ) ) < getMaxSpeed() + 1 )
            {
                setLocation( commandQueue.peek().getLocation().getX(), commandQueue.peek().getLocation().getY() );
                arrived = true;
            }
            else
            {
                x += dx;
                y += dy;
            }
        }
    }

    /**
     * Gives <code>this</code> a new velocity, disregarding the old velocity.
     */
    public void setVelocity( int dx, int dy )
    {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gives <code>this</code> a new speed, but preserves the sign of dx and dy.
     * If dx or dy is zero, their sign is treated as positive.
     */
    public void setSpeed( int dx, int dy )
    {
        int signX = 1,  signY = 1;
        if ( dx < 0 )
            signX = -1;
        if ( dy < 0 )
            signY = -1;
        setVelocity( signX * Math.abs( dx ), signY * Math.abs( dy ) );
    }

    /**
     * Multiplies this <code>GameObject</code>'s dx and dy by the given factor.
     * A parameter of 0 will stop <code>this</code> immediately; a parameter of 1.0
     * will have no effect, and any number above 1 will be an increase in speed.
     * 
     * @param factor a number between 0.0 and 1.0 representing the amount to slow down.
     */
    public void decelerate( int factor )
    {
        dx *= factor;
        dy *= factor;
    }

    public double getSpeed()
    {
        return Math.sqrt( dx * dx + dy * dy );
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
