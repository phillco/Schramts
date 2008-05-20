/*
 * DISASTEROIDS
 * ImageLibrary.java
 */
package sts.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Loads and stores the game's external graphics resources.
 * @author Andy Kooiman, Phillip Cohen
 */
public class ImageHandler
{
    /**
     * External images that game objects use.
     */
    private static Image hq,  barracks,  villager,  villagerWithGold,  infantry,  gold;

    /**
     * The images that have been hue shifted already, so that they aren't recalculated.
     */
    private static HashMap<ShiftedImage, Image> found;

    /**
     * Imports all of the resources.
     */
    public static void init()
    {
        found = new HashMap<ShiftedImage, Image>();
        hq = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/hq.gif" ) );
        barracks = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/barracks.gif" ) );
        villager = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/villager.gif" ) );
        villagerWithGold = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/villagerWithGold.gif" ) );
        infantry = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/warrior.gif" ) );
        gold = Toolkit.getDefaultToolkit().createImage( ImageHandler.class.getResource( "/gold.gif" ) );
    }

    public void drawHQ( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( hq, c ), g, x, y );
    }

    public void drawBarracks( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( barracks, c ), g, x, y );
    }

    public void drawVillager( Graphics2D g, int x, int y, Color c, boolean hasGold )
    {
        drawImage( hueShift( hasGold ? villagerWithGold : villager, c ), g, x, y );
    }

    public void drawInfantry( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( infantry, c ), g, x, y );
    }

    public void drawGold( Graphics2D g, int x, int y )
    {
        drawImage( gold, g, x, y );
    }

    private void drawImage( Image i, Graphics2D g, int x, int y )
    {

    }

    private static Image hueShift( Image img, Color target )
    {
        if ( found.containsKey( new ShiftedImage( img, target ) ) )
            return found.get( new ShiftedImage( img, target ) );
        BufferedImage orig = new BufferedImage( img.getWidth( null ), img.getHeight( null ), BufferedImage.TYPE_INT_ARGB );
        orig.getGraphics().drawImage( img, 0, 0, null );
        return hueShift( orig, target );
    }

    /**
     * Takes an image and shifts its hue, preserving the saturation and brightness.
     * @param img
     * @param target
     * @return
     */
    private static Image hueShift( BufferedImage img, Color target )
    {
        if ( found.containsKey( new ShiftedImage( img, target ) ) )
            return found.get( new ShiftedImage( img, target ) );
        //start making the image
        BufferedImage ret = new BufferedImage( img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB );

        //figure out what color you want
        float targetHue;
        {//I want these variables to go out of scope soon so I can reuse names

            int red = target.getRed();
            int green = target.getGreen();
            int blue = target.getBlue();
            targetHue = Color.RGBtoHSB( red, green, blue, new float[3] )[0];
        }
        //prepare to draw
        Graphics2D gimg = ret.createGraphics();

        //loop through pixels
        for ( int x = 0; x < img.getWidth(); x++ )
        {
            for ( int y = 0; y < img.getHeight(); y++ )
            {
                //get what's there, and split it up
                int present = img.getRGB( x, y );
                int blue = ( present & 0x0000FF );
                int green = ( ( present & 0x00FF00 ) >> 8 );
                int red = ( ( present & 0xFF0000 ) >> 16 );

                //convert to HSB
                float[] hsb =
                        {
                    0f, 0f, 0f
                };
                Color.RGBtoHSB( red, green, blue, hsb );

                //change the hue, and draw the pixel
                Color newCol = Color.getHSBColor( ( 9f * targetHue + hsb[0] ) / 10f, hsb[1], hsb[2] );
                gimg.setColor( newCol );
                gimg.drawRect( x, y, 1, 1 );
            }
        }
        found.put( new ShiftedImage( img, target ), ret );
        return ret;
    }

    private static class ShiftedImage
    {
        private Image img;

        private Color hue;

        public ShiftedImage( Image img, Color hue )
        {
            this.img = img;
            this.hue = hue;
        }

        @Override
        public boolean equals( Object other )
        {
            return other instanceof ShiftedImage && ( (ShiftedImage) other ).hue.equals( hue ) && ( (ShiftedImage) other ).img.equals( img );
        }
    }
}
