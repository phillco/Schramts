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

    private int timeLeft;

    public Corpse( int x, int y, BufferedImage image, Color color )
    {
        super( x, y, image.getWidth(), image.getHeight(), Integer.MAX_VALUE, Game.getInstance().getNature() );
        this.image = image;
        this.color = color;
        timeLeft = 400;
    }

    @Override
    public void act()
    {
        super.act();
        if ( timeLeft-- <= 0 )
            getOwningPlayer().removeObject( this );
    }

    @Override
    public void draw( Graphics2D g )
    {
        ImageHandler.drawImage( g, getX(), getY(), color, image, true );
    }

    @Override
    public String getName()
    {
        return "Corpse";
    }
}
