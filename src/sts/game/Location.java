/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

/**
 *
 * @author AK90454
 */
public class Location implements Locatable
{

    static double getDistance( Location closest, Location loc )
    {
        return Math.sqrt(Math.pow(closest.getX()-loc.getX(), 2)+ Math.pow(closest.getY()-loc.getY(),2));
    }
    private int x,  y;

    public Location( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public Location getLoc()
    {
        return this;
    }
    
    @Override
    public String toString()
    {
        return "("+x+", "+y+")";
    }
    
    public Location translate(int x, int y)
    {
        return new Location(this.x+x, this.y+y);
    }
}
