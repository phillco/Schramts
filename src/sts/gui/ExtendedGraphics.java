package sts.gui;

import java.awt.Canvas;
import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public class ExtendedGraphics
{
    public enum HorizontalAlign
    {
        LEFT, CENTER, RIGHT;

    }

    public enum VerticleAlign
    {
        TOP, MIDDLE, BOTTOM;

    }

    public static void drawText( Graphics2D g, String text, int x, int y, Canvas boundary )
    {
        HorizontalAlign ha = HorizontalAlign.LEFT;

        if ( x + g.getFont().getStringBounds( text, g.getFontRenderContext() ).getWidth() > boundary.getWidth() )
            ha = HorizontalAlign.CENTER;

        if ( g.getFont().getStringBounds( text, g.getFontRenderContext() ).getWidth() / 2 > boundary.getWidth() &&
                ( x + g.getFont().getStringBounds( text, g.getFontRenderContext() ).getWidth() / 2 - boundary.getWidth() > g.getFont().getStringBounds( text, g.getFontRenderContext() ).getWidth() / 2 - x ) )
            ha = HorizontalAlign.RIGHT;

        drawText( g, text, x, y, HorizontalAlign.LEFT, VerticleAlign.TOP );
    }

    public static void drawText( Graphics2D g, String text, int x, int y, HorizontalAlign horizontalAlign, VerticleAlign verticalAlign )
    {
        if ( horizontalAlign != HorizontalAlign.LEFT )
            x -= (int) g.getFont().getStringBounds( text, g.getFontRenderContext() ).getWidth() / ( horizontalAlign == HorizontalAlign.CENTER ? 2 : 1 );

        if ( verticalAlign != VerticleAlign.TOP )
            y -= (int) g.getFont().getStringBounds( text, g.getFontRenderContext() ).getHeight() / ( verticalAlign == VerticleAlign.MIDDLE ? 2 : 1 );

        // drawString doesn't support linebreaks, so we do that here.
        String[] lines = text.split( "\n" );
        if ( lines.length > 1 )
        {
            for ( String line : lines )
            {
                drawText( g, line, x, y, horizontalAlign, verticalAlign );
                y += (int) g.getFont().getStringBounds( line, g.getFontRenderContext() ).getHeight();
                return;
            }
            return;
        }
        g.drawString( text, x, y );
    }
}
