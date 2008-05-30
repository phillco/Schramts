package sts.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import sts.gui.ImageHandler;

/**
 *
 * @author Phillip Cohen
 */
public class Corpse extends GameObject
{
    private BufferedImage image;

    private Color color;

    public Corpse( int x, int y, BufferedImage image, Color color )
    {
        super( x, y, Integer.MAX_VALUE, Game.getInstance().getNature() );
        this.image = image;
        this.color = color;
    }

    @Override
    public void draw( Graphics2D g )
    {
        ImageHandler.drawImage( g, x, y, color, image );
    }

    @Override
    public String getName()
    {
        return "Corpse";
    }
}
