package sts.gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public class ExtendedGraphics
{
    private Graphics2D g;

    public enum HorizontalAlign
    {
        LEFT, CENTER, RIGHT;

    };

    public enum VerticleAlign
    {
        TOP, MIDDLE, BOTTOM;

    };
    int width, height;

    public ExtendedGraphics( Canvas parent )
    {
        width = parent.getWidth();
        height = parent.getHeight();
    }

    public Graphics getG()
    {
        return g;
    }

    public void setG( Graphics g )
    {
        this.g = (Graphics2D) g;
    }

    public void drawText( String text, int x, int y, HorizontalAlign horizontalAlign, VerticleAlign verticalAlign )
    {
        if ( horizontalAlign != HorizontalAlign.LEFT )
            x -= (int) g.getFont().getStringBounds( text, ( (Graphics2D) g ).getFontRenderContext() ).getWidth() / ( horizontalAlign == HorizontalAlign.CENTER ? 2 : 1 );
        
        if ( y < 0)
            y = height + y - (int) g.getFont().getStringBounds( text, ( (Graphics2D) g ).getFontRenderContext() ).getHeight();

        // drawString doesn't support linebreaks, so we do that here.
        String[] lines = text.split( "\n" );
        if ( lines.length > 1 )
        {
            for ( String line : lines )
            {
                drawText( line, x, y, horizontalAlign, verticalAlign );
                y += (int) g.getFont().getStringBounds( line, g.getFontRenderContext() ).getHeight();
                return;
            }
            return;
        }
        g.drawString( text, x, y );
    }
}
