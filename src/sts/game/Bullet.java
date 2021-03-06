package sts.game;

import java.awt.Graphics2D;
import sts.Local;

/**
 * 
 * @author Phillip Cohen
 */
public class Bullet extends Unit
{
    public Bullet( int x, int y, GameObject shooter, GameObject target )
    {
        super( x, y, 0, 0, 0, 0, 100, shooter.getOwningPlayer() );
        giveCommand( new Command( false, target ), true );
    }

    Bullet( Location loc, GameObject shooter, GameObject other )
    {
        this(loc.getX(), loc.getY(), shooter, other);
    }

    @Override
    public void act()
    {
        super.act();

        if ( arrived )
            getOwningPlayer().removeObject( this );
    }

    @Override
    public int getMaxSpeed()
    {
        return 9;
    }

    @Override
    public void draw( Graphics2D g )
    {
        g.setColor( getOwningPlayer().getColor() );
        g.fillOval( getX() - Local.getViewingX(), getY() - Local.getViewingY(), 2, 2 );
    }

    @Override
    public String getName()
    {
        return "Bullet";
    }
}
