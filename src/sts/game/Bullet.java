package sts.game;

import java.awt.Graphics2D;

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
        g.fillOval( getX(), getY(), 2, 2 );
    }

    @Override
    public String getName()
    {
        return "Bullet";
    }
}
