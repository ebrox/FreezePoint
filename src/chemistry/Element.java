/**
 * Programmers: Ed Broxson
 * Date: 04/01/2013 
 * Purpose: Build particles and handle collisions and movement.
 */
package chemistry;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;


public class Element extends VerletParticle2D{

        Element(Vec2D loc) {
            super(loc);
        }
}