/*
 * DISASTEROIDS
 * ImageLibrary.java
 */
package sts.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import sts.Main;

/**
 * Loads and stores the game's external graphics resources.
 * @author Andy Kooiman, Phillip Cohen
 */
public class ImageHandler
{
    /**
     * External images that game objects use.
     */
    private static BufferedImage hq,  barracks,  villager,  villagerWithGold,  infantry,  gold,  constructionSite;

    /**
     * The images that have been hue shifted already, so that they aren't recalculated.
     */
    private static HashMap<ShiftedImage, Image> found;

    /**
     * Imports all of the resources.
     */
    public static void init()
    {
        try
        {
            found = new HashMap<ShiftedImage, Image>();
            //  hq = Toolkit.getDefaultToolkit().createImage(ImageHandler.class.getResource("/hq.gif")); old way with JAR
            hq = ImageIO.read( new File( "res/hq.gif" ) );
            barracks = ImageIO.read( new File( "res/barracks.gif" ) );
            villager = ImageIO.read( new File( "res/villager.gif" ) );
            villagerWithGold = ImageIO.read( new File( "res/villagerWithGold.gif" ) );
            infantry = ImageIO.read( new File( "res/warrior.gif" ) );
            gold = ImageIO.read( new File( "res/gold.gif" ) );
            constructionSite = ImageIO.read( new File( "res/half_built_building.gif" ) );
        }
        catch ( IOException iOException )
        {
            Main.fatalError( "Failed to init the ImageHandler", iOException );
        }

    }

    public static Color getOppositeColor( Color c )
    {
        return new Color( 255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue() );
    }

    public static void drawImage( Graphics2D g, int x, int y, Color c, BufferedImage i )
    {
        drawImage( hueShift( i, c ), g, x, y );
    }

    public static void drawHQ( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( hq, c ), g, x, y );
    }

    public static void drawBarracks( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( barracks, c ), g, x, y );
    }

    public static void drawVillager( Graphics2D g, int x, int y, Color c, boolean hasGold )
    {
        drawImage( hueShift( hasGold ? villagerWithGold : villager, c ), g, x, y );
    }

    public static void drawInfantry( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( infantry, c ), g, x, y );
    }

    public static void drawGold( Graphics2D g, int x, int y )
    {
        drawImage( gold, g, x, y );
    }

    public static void drawConstructionSite( Graphics2D g, int x, int y, Color color )
    {
        drawImage( hueShift( constructionSite, color ), g, x, y );
    }

    private static void drawImage( Image i, Graphics2D g, int x, int y )
    {
        g.drawImage( i, x, y, null );
    }

    public static BufferedImage getVillager()
    {
        return villager;
    }

    public static BufferedImage getBarracks()
    {
        return barracks;
    }

    public static BufferedImage getGold()
    {
        return gold;
    }

    public static BufferedImage getInfantry()
    {
        return infantry;
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
        float targetHue, targetSat;
        {//I want these variables to go out of scope soon so I can reuse names

            int red = target.getRed();
            int green = target.getGreen();
            int blue = target.getBlue();
            float[] dummy = {0f,0f,0f};
            targetHue = Color.RGBtoHSB( red, green, blue, dummy )[0];
            targetSat = dummy[1];
        }

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
                //hsb now has the hsb representation of the old color
                Color.RGBtoHSB( red, green, blue, hsb );

                //change the hue, and draw the pixel
                int rgb;//the new argb representation of the current pixel

                if ( hsb[1] > .01 )//not grayscale, don't change
                    rgb = img.getRGB( x, y );
                else if ( red == 255 && red == green && green == blue )//don't change white
                    rgb = 0;
                else
                    rgb = ( Color.HSBtoRGB( targetHue, targetSat, hsb[2] ) );
                ret.setRGB( x, y, rgb );

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

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 89 * hash + ( this.img != null ? this.img.hashCode() : 0 );
            hash = 89 * hash + ( this.hue != null ? this.hue.hashCode() : 0 );
            return hash;
        }
    }
}
