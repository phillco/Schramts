package sts.game;

/**
 * Any sort of command given to an object.
 * @author Phillip Cohen
 */
public class Command
{
    /**
     * Was this command directly given by the player, or automatically generated?
     */
    private boolean givenByPlayer;

    private Location location;

    private GameObject object;

    public Command( boolean givenByPlayer, GameObject object )
    {
        this.givenByPlayer = givenByPlayer;
        this.location = object.getLocation();
        this.object = object;
    }

    public Command( boolean givenByPlayer, Location location )
    {
        this.givenByPlayer = givenByPlayer;
        this.location = location;
        this.object = null;
    }

    public boolean isGivenByPlayer()
    {
        return givenByPlayer;
    }

    public Location getLocation()
    {
        return location;
    }

    public GameObject getObject()
    {
        return object;
    }

    public boolean isValid()
    {
        return object.getOwningPlayer().getOwnedObjects().contains( object );
    }
    
    public boolean isGround()
    {
        return (object == null);
    }
}
