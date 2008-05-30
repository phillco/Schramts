package sts;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author Phillip Cohen
 */
public class Util
{
    /**
     * Global array of random generators.
     * @see Util#getRandomGenerator()
     */
    private static ExtendedRandom[] instances =
    {
        new ExtendedRandom()
    };

    /**
     * The last used random generator in <code>instances</code>.
     * @see Util#instances
     */
    private static int lastGeneratorUsed = 0;

    /**
     * The formatter used in <code>insertThousandCommas</code>
     * @see Util#insertThousandCommas(int) 
     */
    private static DecimalFormat thousands = new DecimalFormat( "" );

    /**
     * Takes the given number and inserts comma seperators at each grouping.
     * "491911920518159419" becomes "491,911,920,518,159,419".
     */
    public static String insertThousandCommas( int number )
    {
        return thousands.format( number );
    }

    public static Color getRandomColor()
    {
        return new Color( getRandomGenerator().nextInt( 255 ), getRandomGenerator().nextInt( 255 ), getRandomGenerator().nextInt( 255 ) );
    }

    /**
     * Returns a global random generator, which gives better pseudorandomness than constantly making a new instance.
     * Multiple instances are cycled through to relieve bottlenecks.
     * 
     * @return  a static instance of <code>Random</code>
     */
    public static ExtendedRandom getRandomGenerator()
    {
        lastGeneratorUsed = ( lastGeneratorUsed + 1 ) % instances.length;
        return instances[lastGeneratorUsed];
    }

    /**
     * Java's random generator, with a few additional methods.
     * @author Phillip Cohen
     */
    public static class ExtendedRandom extends Random
    {
        /**
         * Randoms a random double between 0 and 2 * PI.
         */
        public double nextAngle()
        {
            return nextDouble() * 2 * Math.PI;
        }

        /**
         * Returns a random double from -n/2 to n/2.
         */
        public double nextMidpointDouble( double n )
        {
            return nextDouble() * n - n / 2;
        }
    }
}
