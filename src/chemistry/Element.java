/**
 * Programmers: Ed Broxson
 * Date: 04/01/2013 
 * Purpose: Build particles and handle collisions and movement.
 * Uses: Toxiclibs - toxiclibscore.jar and verletphysics.jar and
 */
package chemistry;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;


public class Element extends VerletParticle2D{

    /**
     * Sets up an Element at Vector (Vec2D) indicated
     * @param loc 
     */
        public Element(Vec2D loc) {
            super(loc);
        }
}