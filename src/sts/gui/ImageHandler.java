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
import sts.Local;
import sts.Main;
import sts.game.Game;

/**
 * Loads and stores the game's external graphics resources.
 * @author Andy Kooiman, Phillip Cohen
 */
public class ImageHandler
{
    /**
     * External images that game objects use.
     */
    private static BufferedImage hq,  barracks,  barracksButton,  villager,  villagerWithGold,  infantry,  gold,  constructionSite,  rallyPoint,  destination,  razedBuilding,  deadVillager,  deadInfantry,  slaveryButton,  groundTexture;

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
            barracksButton = ImageIO.read( new File( "res/barracksButton.gif" ) );
            villager = ImageIO.read( new File( "res/villager.gif" ) );
            villagerWithGold = ImageIO.read( new File( "res/villagerWithGold.gif" ) );
            infantry = ImageIO.read( new File( "res/warrior.gif" ) );
            gold = ImageIO.read( new File( "res/gold.gif" ) );
            constructionSite = ImageIO.read( new File( "res/half_built_building.gif" ) );
            rallyPoint = ImageIO.read( new File( "res/rallyPoint.gif" ) );
            destination = ImageIO.read( new File( "res/destination.gif" ) );
            deadVillager = ImageIO.read( new File( "res/deadVillager.gif" ) );
            deadInfantry = ImageIO.read( new File( "res/deadWarrior.gif" ) );
            razedBuilding = ImageIO.read( new File( "res/razedBuilding.gif" ) );
            slaveryButton = ImageIO.read( new File( "res/slaveryButton.gif" ) );
            groundTexture = ImageIO.read( new File( "res/ground.gif" ) );
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

    public static void drawImage( Graphics2D g, int x, int y, Color c, BufferedImage i, boolean gameCoordinates )
    {
        drawImage( hueShift( i, c ), g, x, y, gameCoordinates );
    }

    public static void drawHQ( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( hq, c ), g, x, y, true );
    }

    public static void drawBarracks( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( barracks, c ), g, x, y, true );
    }

    public static void drawVillager( Graphics2D g, int x, int y, Color c, boolean hasGold )
    {
        drawImage( hueShift( hasGold ? villagerWithGold : villager, c ), g, x, y, true );
    }

    public static void drawInfantry( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( infantry, c ), g, x, y, true );
    }

    public static void drawRalleyPoint( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( rallyPoint, c ), g, x, y, true );
    }

    public static void drawDestination( Graphics2D g, int x, int y, Color c )
    {
        drawImage( hueShift( destination, c ), g, x, y, true );
    }

    public static void drawGold( Graphics2D g, int x, int y )
    {
        drawImage( gold, g, x, y, true );
    }

    public static void drawGround( Graphics2D g )
    {
        for ( int x = 0; x < Game.getInstance().getLevelWidth(); x += groundTexture.getWidth() )
            for ( int y = 0; y < Game.getInstance().getLevelHeight(); y += groundTexture.getHeight() )
                drawImage( groundTexture, g, x, y, true );
    }

    public static void drawConstructionSite( Graphics2D g, int x, int y, Color color )
    {
        drawImage( hueShift( constructionSite, color ), g, x, y, true );
    }

    private static void drawImage( Image i, Graphics2D g, int x, int y, boolean gameCoordinates )
    {
        if ( gameCoordinates )
        {
            x -= Local.getViewingX();
            y -= Local.getViewingY();
        }

        if ( x + i.getWidth( null ) > 0 && x <= Local.getViewingWidth() + i.getWidth( null ) )
            if ( y + i.getHeight( null ) > 0 && y < Local.getWiewingHeight() + i.getHeight( null ) )
                g.drawImage( i, x, y, null );
    }

    public static BufferedImage getRazedBuilding()
    {
        return razedBuilding;
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

    public static BufferedImage getBarracksButton()
    {
        return barracksButton;
    }

    public static BufferedImage getSlaveryButton()
    {
        return slaveryButton;
    }

    public static BufferedImage getDeadInfantry()
    {
        return deadInfantry;
    }

    public static BufferedImage getDeadVillager()
    {
        return deadVillager;
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
            float[] dummy =
            {
                0f, 0f, 0f
            };
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
