package sts.game;

import java.awt.Graphics2D;

/**
 *
 * @author Phillip Cohen
 */
public abstract class GameObject
{
    /**
     * Location and velocity.
     */
    double x, y, dx, dy;
    
    /**
     * Who controls this? If null, owned by Nature.
     */
    Player owner;
    
    /**
     * The unit's health and maxHealth.
     */
    int maxHealth = 100, health = maxHealth;

    public GameObject( double x, double y, double dx, double dy, Player owner )
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.owner = owner;
    }
    
    
    public void act()
    {
        x += dx;
        y += dy;
    }
    
    public abstract void draw(Graphics2D g);
    
    
    
    
}
