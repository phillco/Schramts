/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.image.BufferedImage;

/**
 *
 * @author Phillip Cohen
 */
public class ProductionCommand
{
    private String name;

    private int timeToMake,  cost;

    private BufferedImage queuedImage;

    public ProductionCommand( String name, int timeToMake, int cost, BufferedImage queuedImage )
    {
        this.name = name;
        this.timeToMake = timeToMake;
        this.queuedImage = queuedImage;
        this.cost = cost;
    }

    public String getName()
    {
        return name;
    }

    public BufferedImage getQueuedImage()
    {
        return queuedImage;
    }

    public int getTimeToMake()
    {
        return timeToMake;
    }

    public int getCost()
    {
        return cost;
    }

    public void setCost( int cost )
    {
        this.cost = cost;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
