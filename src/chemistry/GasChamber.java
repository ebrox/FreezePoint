/**
 * Programmers: Ed Broxson
 * Date: 04/01/2013 
 * Purpose: Create and draw multiple particles for use in Chemistry Freeze Point Depression example.
 */
package chemistry;

import java.awt.Shape;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import processing.core.PApplet;
import processing.core.PShape;
import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.constraints.CircularConstraint;
import toxi.physics2d.constraints.RectConstraint;

public class GasChamber extends PApplet {

    ArrayList<Element> negA, negB, negC; // negative
    ArrayList<Element> posA, posB, posC; // positive
    ArrayList<Element> solB, solC;
//ArrayList<ConstantForceBehavior> cfb;
    private boolean bStop;
    private int numParts = 20;
    private int numSolutes = 5;
    private GravityBehavior grav;
    private ConstantForceBehavior move;
    private AttractionBehavior soluteAtt;
    private AttractionBehavior negAtt;
    private AttractionBehavior posAtt;
    private float radius, flit = .01f; // was .01
    private float soluteStr = .8f; // was .3
    private float negStr = .4f; // was .1
    private float posStr = .04f; // was .05
    private Vec2D normGrav;
    private int baseX = 25, baseY = 15, bottom = 190, between = 75;
    private int bWidth = 200, bWidth1 = 200, bWidth2 = 300, bWidth3 = 575;


// ***** For now work on freezing and heating things up - may be able to do this by changing value of flit (move of negative element)
// *** look into setting beaker area to circle
// ***** Need to stop solutes from attracting each other - perhaps just have attractions on negatives and positives ???
    
    
    VerletPhysics2D physics1;
    VerletPhysics2D physics2;
    VerletPhysics2D physics3;

    @Override
    public void setup() {
        size(800, 200);
        smooth();
        physics1 = new VerletPhysics2D();
        physics2 = new VerletPhysics2D();
        physics3 = new VerletPhysics2D();
        physics1.setDrag(.4f); // was .01
        physics2.setDrag(.4f);
        physics3.setDrag(.4f);                                                 // this may be usefull when trying to freeze the water
//        physics.setWorldBounds(new Rect(0, 0, width, height));
        physics1.setWorldBounds(new Rect(30, 30, 190, 155));
//        box1Rect = new Rect(25, 15, 200, 175);
        physics2.setWorldBounds(new Rect(305, 30, 190, 155));
        physics3.setWorldBounds(new Rect(580, 30, 190, 155));
        
        negA = new ArrayList<Element>();
        negB = new ArrayList<Element>();
        negC = new ArrayList<Element>();
        posA = new ArrayList<Element>();
        posB = new ArrayList<Element>();
        posC = new ArrayList<Element>();
        solB = new ArrayList<Element>();
        solC = new ArrayList<Element>();
        
        radius = 4;
        
        
        
        

        normGrav = new Vec2D(0.0f, 0.1f);
        grav = new GravityBehavior(normGrav);
        move = new ConstantForceBehavior(new Vec2D(random(-flit, flit), random(-flit, flit)));  // sets the force to be only one thing, never changing unless I reinitialize it below


        //negA list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            negA.add(new Element(new Vec2D(random(35, 220), random(35, 185))));
            
            Element nA = negA.get(i);
            
            negAtt = new AttractionBehavior(nA, radius * 4, negStr);
            
            physics1.addParticle(nA);
            physics1.addBehavior(negAtt);
            nA.addBehavior(move);
        }
        
        //negB list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            negB.add(new Element(new Vec2D(random(310, 490), random(35, 185))));
            
            Element nB = negB.get(i);
            
            negAtt = new AttractionBehavior(nB, radius * 4, negStr);
            
            physics2.addParticle(nB);
            physics2.addBehavior(negAtt);
            nB.addBehavior(move);
            
        }
        
        //negC list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
                negC.add(new Element(new Vec2D(random(585, 770), random(35, 185))));
            
            Element nC = negC.get(i);
            
            negAtt = new AttractionBehavior(nC, radius * 4, negStr);
            
            physics3.addParticle(nC);
            physics3.addBehavior(negAtt);
            nC.addBehavior(move);
            
        }
        
        //posA list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            posA.add(new Element(new Vec2D(random(35, 220), random(35, 185))));
            Element pA = posA.get(i);
          
            posAtt = new AttractionBehavior(pA, radius * 4, posStr);
            physics1.addParticle(pA);
            physics1.addBehavior(posAtt);


            // Connect negA and posA at same index numbers
            Element a = negA.get(i);
            Element b = posA.get(i);

            //[offset-down] Then we make a spring connection between the particle and the previous particle with a rest length and strength (both floats).
            VerletConstrainedSpring2D spring = new VerletConstrainedSpring2D(a, b, 8f, 1f);

            // We must not forget to add the spring to the physics world.
            physics1.addSpring(spring);


        }
        
        //posB list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            posB.add(new Element(new Vec2D(random(310, 490), random(35, 185))));
            Element pB = posB.get(i);
          
            posAtt = new AttractionBehavior(pB, radius * 4, posStr);
            physics2.addParticle(pB);
            physics2.addBehavior(posAtt);


            // Connect negB and posB at same index numbers
            Element a = negB.get(i);
            Element b = posB.get(i);

            //[offset-down] Then we make a spring connection between the particle and the previous particle with a rest length and strength (both floats).
            VerletConstrainedSpring2D spring = new VerletConstrainedSpring2D(a, b, 8f, 1f);

            // We must not forget to add the spring to the physics world.
            physics2.addSpring(spring);

        }
        
        //posC list fill and add behaviors
        for (int i = 0; i < numParts; i++) {

            posC.add(new Element(new Vec2D(random(585, 770), random(35, 185))));

            Element pC = posC.get(i);
          
            posAtt = new AttractionBehavior(pC, radius * 4, posStr);
            physics3.addParticle(pC);
            physics3.addBehavior(posAtt);


            // Connect negC and posC at same index numbers
            Element a = negC.get(i);
            Element b = posC.get(i);

            //[offset-down] Then we make a spring connection between the particle and the previous particle with a rest length and strength (both floats).
            VerletConstrainedSpring2D spring = new VerletConstrainedSpring2D(a, b, 8f, 1f);

            // We must not forget to add the spring to the physics world.
            physics3.addSpring(spring);


        }

        //set all of negA with a min distance spring from each other
        for (int i = 0; i < negA.size() - 1; i++) {
            VerletParticle2D ni = negA.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < negA.size(); j++) { //[bold]
                VerletParticle2D nj = negA.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics1.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }
        
        //set all of negB with a min distance spring from each other
        for (int i = 0; i < negB.size() - 1; i++) {
            VerletParticle2D ni = negB.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < negB.size(); j++) { //[bold]
                VerletParticle2D nj = negB.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics2.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }
        
        //set all of negC with a min distance spring from each other
        for (int i = 0; i < negC.size() - 1; i++) {
            VerletParticle2D ni = negC.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < negC.size(); j++) { //[bold]
                VerletParticle2D nj = negC.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics3.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }

        //set all of posA with a min distance spring from each other
        for (int i = 0; i < posA.size() - 1; i++) {
            VerletParticle2D ni = posA.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < posA.size(); j++) { //[bold]
                VerletParticle2D nj = posA.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics1.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }
        
        //set all of posB with a min distance spring from each other
        for (int i = 0; i < posB.size() - 1; i++) {
            VerletParticle2D ni = posB.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < posB.size(); j++) { //[bold]
                VerletParticle2D nj = posB.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics2.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }
        
        //set all of posC with a min distance spring from each other
        for (int i = 0; i < posC.size() - 1; i++) {
            VerletParticle2D ni = posC.get(i);

            // Look how we start j at i + 1.
            for (int j = i + 1; j < posC.size(); j++) { //[bold]
                VerletParticle2D nj = posC.get(j);

                // The Spring connects particles “ni” and “nj”.
                physics3.addSpring(new VerletMinDistanceSpring2D(ni, nj, 12f, 1f));
            }
        }

        //set all of negA with min distance spring from each of posA except for the one it is attached to
        for (int i = 0; i < negA.size(); i++) {
            VerletParticle2D aa = negA.get(i);

            for (int j = 0; j < posA.size(); j++) {
                if (j != i) {
                    VerletParticle2D bb = posA.get(j);
                    physics1.addSpring(new VerletMinDistanceSpring2D(aa, bb, 8.5f, 1f));
                }
            }
        }
        
        //set all of negB with min distance spring from each of posB except for the one it is attached to
        for (int i = 0; i < negB.size(); i++) {
            VerletParticle2D aa = negB.get(i);

            for (int j = 0; j < posB.size(); j++) {
                if (j != i) {
                    VerletParticle2D bb = posB.get(j);
                    physics2.addSpring(new VerletMinDistanceSpring2D(aa, bb, 8.5f, 1f));
                }
            }
        }
        
        //set all of negC with min distance spring from each of posC except for the one it is attached to
        for (int i = 0; i < negC.size(); i++) {
            VerletParticle2D aa = negC.get(i);

            for (int j = 0; j < posC.size(); j++) {
                if (j != i) {
                    VerletParticle2D bb = posC.get(j);
                    physics3.addSpring(new VerletMinDistanceSpring2D(aa, bb, 8.5f, 1f));
                }
            }
        }


        // setup solutes for solB and set springs to keep min distance from positive and negative elements
        for (int i = 0; i < numSolutes; i++) {
            solB.add(new Element(new Vec2D(random(310, 490), random(35, 185))));
            Element s2 = solB.get(i);
            
            soluteAtt = new AttractionBehavior(s2, radius * 4, soluteStr);
            
            physics2.addParticle(s2);
            physics2.addBehavior(soluteAtt);
            s2.addBehavior(grav);

            for (int j = 0; j < negB.size(); j++) {
                Element a = negB.get(j);
                Element b = posB.get(j);
                physics2.addSpring(new VerletMinDistanceSpring2D(s2, a, 8f, 1f));
                physics2.addSpring(new VerletMinDistanceSpring2D(s2, b, 13f, 1f));
            }
        }
        
        // setup solutes for solC and set springs to keep min distance from positive and negative elements
        for (int i = 0; i < numSolutes * 2; i++) {
            solC.add(new Element(new Vec2D(random(585, 770), random(35, 185))));
            Element s3 = solC.get(i);
            
            soluteAtt = new AttractionBehavior(s3, radius * 4, soluteStr);
            
            physics3.addParticle(s3);
            physics3.addBehavior(soluteAtt);
            s3.addBehavior(grav);

            for (int j = 0; j < negC.size(); j++) {
                Element a = negC.get(j);
                Element b = posC.get(j);
                physics3.addSpring(new VerletMinDistanceSpring2D(s3, a, 8f, 1f));
                physics3.addSpring(new VerletMinDistanceSpring2D(s3, b, 13f, 1f));
            }
        }
        
        // set springs for solB to keep solutes away from each other
        for (int i = 0; i < solB.size() - 1; i++) {
            VerletParticle2D ai = solB.get(i);
            for (int j = i + 1; j < solB.size(); j++) {
                VerletParticle2D aj = solB.get(j);
                physics2.addSpring(new VerletMinDistanceSpring2D(ai, aj, 25f, 1f));
            }
        }
        
        // set springs for solC to keep solutes away from each other
        for (int i = 0; i < solC.size() - 1; i++) {
            VerletParticle2D ai = solC.get(i);
            for (int j = i + 1; j < solC.size(); j++) {
                VerletParticle2D aj = solC.get(j);
                physics3.addSpring(new VerletMinDistanceSpring2D(ai, aj, 25f, 1f));
            }
        }

        //add grav behaviors to list
//  cfb = new ArrayList<ConstantForceBehavior>();                                 // AEB attempt to add CFB array list
//  for(int i = 0; i < solB.size(); i++){
//      cfb.add(grav);
//  }
        //add the grav behvior to each Element
//  for(int i = 0; i < solB.size(); i++){
//          solB.get(i).addBehavior(cfb.get(i));
//      
//  }


    }

    /**
     * check if Element is at the edge and reverse it's direction also trigger
     * Winning conditions based on first particles of each Element to reach
     * finish line
     */
    public void checkEdges(VerletParticle2D p, int left, int right) {

        float r = 10;
        float xDir = random(-flit, flit);
        float yDir = random(-flit, flit);

        if (p.x + r >= right) {
            xDir = -flit;
        } else if (p.x - r <= left) {
            xDir = flit;
        }
        if (p.y + r >= 190) {
            yDir = -flit;
        } else if (p.y - r <= 15) {
            yDir = flit;
        }
        Vec2D newDir = new Vec2D(xDir, yDir);
//        newDir.normalize();
        
        move.setForce(newDir);
        
    }
    

    @Override
    public void draw() {
        physics1.update();
        physics2.update();
        physics3.update();
        background(200);
        
//        ellipse(125, 100, 100, 100);
        
//        rect(25, 15, 200, 175);
        
        
//        frameRate(30);
        
        line(25, 15, 25, 190);
        line(25, 190, 225, 190);
        line(225, 190, 225, 15);
        
        line(300, 15, 300, 190);
        line(300, 190, 500, 190);
        line(500, 190, 500, 15);
        
        line(575, 15, 575, 190);
        line(575, 190, 775, 190);
        line(775, 190, 775, 15);
        
        //display negative particles of negA and check for proximity to solutes
        for (Element p : negA) {

            move = new ConstantForceBehavior(new Vec2D(random(-flit, flit), random(-flit, flit)));

            if (!p.behaviors.contains(move)) {
                p.addBehavior(move);
            }

//            checkEdges(p, 40, 200);

//            for (Element a : solB) {
//                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {
//                    p.lock();
//                    p.removeBehavior(move);
//                    p.unlock();
//                }
//            }
//            p.display();
            fill(255, 0, 0); // red
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }
        
        //display negative particles of negB and check for proximity to solutes
        for (Element p : negB) {

            move = new ConstantForceBehavior(new Vec2D(random(-flit, flit), random(-flit, flit)));

            if (!p.behaviors.contains(move)) {
                p.addBehavior(move);
            }

//            checkEdges(p, 40, 200);

            for (Element a : solB) {
                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {
                    p.lock();
                    p.removeBehavior(move);
                    p.unlock();
                }
            }
//            p.display();
            fill(255, 0, 0); // red
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }
        
         //display negative particles of negC and check for proximity to solutes
        for (Element p : negC) {

            move = new ConstantForceBehavior(new Vec2D(random(-flit, flit), random(-flit, flit)));

            if (!p.behaviors.contains(move)) {
                p.addBehavior(move);
            }

//            checkEdges(p, 40, 200);

            for (Element a : solC) {
                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {
                    p.lock();
                    p.removeBehavior(move);
                    p.unlock();
                }
            }
//            p.display();
            fill(255, 0, 0); // red
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }

        //display positive particles for posA
        for (Element p : posA) {
//            p.display();
            fill(0, 0, 255); // blue
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);

        }
        
        //display positive particles for posB
        for (Element p : posB) {
//            p.display();
            fill(0, 0, 255); // blue
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);

        }
        
        //display positive particles for posC
        for (Element p : posC) {
//            p.display();
            fill(0, 0, 255); // blue
//            stroke(0);
//            strokeWeight(2);
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);

        }

        //display solute particles for solB and check for proximity to negB particles
        for (Element a : solB) {

            if (!a.behaviors.contains(grav)) {
                a.addBehavior(grav);
            }
            
//            checkEdges(a, 25, 225);

            for (Element p : negB) {

                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {                              // AEB needs help
//                    a.lock();
                    a.removeBehavior(grav);
//                    a.unlock();
                }
            }
//            a.display();
            fill(255, 255, 0); // yellow
            stroke(0);
            strokeWeight(2);
//            noStroke();
            ellipse(a.x, a.y, radius * 2, radius * 2);
        }
        
        //display solute particles for solC and check for proximity to negC particles
        for (Element a : solC) {

            if (!a.behaviors.contains(grav)) {
                a.addBehavior(grav);
            }
            
//            checkEdges(a, 25, 225);

            for (Element p : negC) {

                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {                              // AEB needs help
//                    a.lock();
                    a.removeBehavior(grav);
//                    a.unlock();
                }
            }
//            a.display();
            fill(255, 255, 0); // yellow
            stroke(0);
            strokeWeight(2);
//            noStroke();
            ellipse(a.x, a.y, radius * 2, radius * 2);
        }

        //reset particles for testing purposes  AEB - pull this before distribution
        if (mousePressed) {
            
            for(int i = 0; i < negA.size(); i++){
                negA.get(i).lock();
                negA.get(i).set(random(35, 220), random(35, 185));
            }
            for (int i = 0; i < negB.size(); i++) {
                negB.get(i).lock();
                negB.get(i).set(random(310, 490), random(35, 185));
            }
            for (int i = 0; i < negC.size(); i++) {
                negC.get(i).lock();
                negC.get(i).set(random(585, 770), random(35, 185));
            }    
            for (int i = 0; i < solB.size(); i++) {
                solB.get(i).lock();
                solB.get(i).set(random(310, 490), random(35, 185));
            }
            for (int i = 0; i < solC.size(); i++) {
                solC.get(i).lock();
                solC.get(i).set(random(585, 770), random(35, 185));
            }
            
        } else {
            for (Element a : negA) {
                a.unlock();
            }
            for (Element a : negB) {
                a.unlock();
            }
            for (Element a : negC) {
                a.unlock();
            }
            for (Element a : solB) {
                a.unlock();
            }
            for (Element a : solC) {
                a.unlock();
            }
            
        }
//        physics.update();
    }

    @Override
    public void keyPressed() {
        bStop = !bStop;
        if (bStop) {
            noLoop();
        } else {
            loop();
        }
    }

//// class Element extends the class VerletParticle2D
//    class Element extends VerletParticle2D {
//
//        float r;
//
//        Element(Vec2D loc, int i) {
//            super(loc);
//            r = 4;
//            soluteAtt = new AttractionBehavior(this, r * 13, soluteStr);
////            this.addConstraint(cirCon);
////            this.addConstraint(b1);
//            if(i % 2 == 0){
//                physics.addParticle(this);
//                physics.addBehavior(soluteAtt);
//            }
//            else{
//                physics2.addParticle(this);
//                physics2.addBehavior(soluteAtt);
//            }
//            this.addBehavior(grav);
//            
//            
//            
////            this.addConstraint(rt);
////            this.addConstraint(r1);
////            this.addConstraint(r2);
////            this.addConstraint(r3);
////            this.addConstraint(r4);
////            this.addConstraint(rb);
//            
////            b1.apply(this);
//            
////            rt.apply(this);
////            r1.apply(this);
////            r2.apply(this);
////            r3.apply(this);
////            r4.apply(this);
////            rb.apply(this);
//        }
//
//        void display() {
//            fill(255, 255, 0); // yellow
//            stroke(0);
//            strokeWeight(2);
////            noStroke();
//            ellipse(x, y, r * 2, r * 2);
//        }
//    }
//
//// class Particle extends the class "VerletParticle2D"
//// negative
//    class Element extends VerletParticle2D {
//
//        float r;
//
//        Element(Vec2D loc, int i) {
//            super(loc);
//            r = 4;
//            negAtt = new AttractionBehavior(this, r * 4, negStr);
////            this.addConstraint(cirCon);
////            this.addConstraint(b1);
//            
//            if(i % 2 == 0){
//                physics.addParticle(this);
//                physics.addBehavior(negAtt);
//            }
//            else{
//                physics2.addParticle(this);
//                physics2.addBehavior(negAtt);
//            }
//            this.addBehavior(move);
//            
//            
//            
////            this.addConstraint(rt);
////            this.addConstraint(r1);
////            this.addConstraint(r2);
////            this.addConstraint(r3);
////            this.addConstraint(r4);
////            this.addConstraint(rb);
//            
////            b1.apply(this);
//            
////            rt.apply(this);
////            r1.apply(this);
////            r2.apply(this);
////            r3.apply(this);
////            r4.apply(this);
////            rb.apply(this);
//        }
//
//        void display() {
//            fill(255, 0, 0); // red
////            stroke(0);
////            strokeWeight(2);
//            noStroke();
//            ellipse(x, y, r * 2, r * 2);
//        }
//    }
//    
//// positive
//    class Element extends VerletParticle2D {
//
//        float r;
//
//        Element(Vec2D loc, int i) {
//            super(loc);
//            r = 4;
//            posAtt = new AttractionBehavior(this, r * 4, posStr);
////            this.addConstraint(cirCon);
////            this.addConstraint(b1);
//            if(i % 2 == 0){
//                physics.addParticle(this);
//                physics.addBehavior(posAtt);
//            }
//            else{
//                physics2.addParticle(this);
//                physics2.addBehavior(posAtt);
//            }
//            
//            
//            
////            this.addConstraint(rt);
////            this.addConstraint(r1);
////            this.addConstraint(r2);
////            this.addConstraint(r3);
////            this.addConstraint(r4);
////            this.addConstraint(rb);
//            
////            b1.apply(this);
//            
////            rt.apply(this);
////            r1.apply(this);
////            r2.apply(this);
////            r3.apply(this);
////            r4.apply(this);
////            rb.apply(this);
//        }
//
//        void display() {
//            fill(0, 0, 255); // blue
////            stroke(0);
////            strokeWeight(2);
//            noStroke();
//            ellipse(x, y, r * 2, r * 2);
//        }
//    }
}