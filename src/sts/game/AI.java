/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sts.game;

/**
 *
 * @author Owner
 */
public abstract class AI
{
    public abstract void act();
    
    public void notifyAboutAttack(int x, int y, GameObject attacker)
    {
    }
}
