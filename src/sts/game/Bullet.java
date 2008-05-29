package sts.game;

import java.awt.Graphics2D;
import java.util.Set;

/**
 * 
 * @author Phillip Cohen
 */
public class Bullet extends Unit
{
    public Bullet( int x, int y, Infantry shooter, GameObject target )
    {
        super( x, y, 0, 0, 100, shooter.getOwningPlayer() );
        setDestination( target );
        setGoal( target );
    }

    @Override
    public void act()
    {
        super.act();

        if ( arrived )
            getOwningPlayer().removeObject( this );
    }

    @Override
    public void setGoal( Set<GameObject> possible )
    {
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
