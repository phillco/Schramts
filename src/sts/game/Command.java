/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author Phillip Cohen
 */
public class Command
{
    private String name;

    private int timeToMake;

    private BufferedImage queuedImage;

    public Command( String name, int timeToMake, BufferedImage queuedImage )
    {
        this.name = name;
        this.timeToMake = timeToMake;
        this.queuedImage = queuedImage;
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
}
