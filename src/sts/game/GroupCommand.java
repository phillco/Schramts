package sts.game;

import java.util.Set;

/**
 *
 * @author Phillip Cohen
 */
public class GroupCommand extends Command
{
    private Set<GameObject> objects;

    public GroupCommand( boolean givenByPlayer, Location location, Set<GameObject> objects )
    {
        super( givenByPlayer, location );
        this.objects = objects;
    }

    public Set<GameObject> getObjects()
    {
        return objects;
    }
}
