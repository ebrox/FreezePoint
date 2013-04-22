/**
 * Programmers: Ed Broxson Date: 04/01/2013 Purpose: Create and draw multiple
 * particles for use in Chemistry Freeze Point Depression example.
 */
package chemistry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import processing.core.PApplet;
import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;

public class GasChamber extends PApplet {

    ArrayList<Element> negA, negB, negC; // negative
    ArrayList<Element> posA, posB, posC; // positive
    ArrayList<Element> solB, solC;
    private boolean bStop;
    private int numParts = 20;
    private int numSolutes = 7;
    private GravityBehavior grav;
    private ConstantForceBehavior move;
    private AttractionBehavior soluteAtt;
    private AttractionBehavior negAtt;
    private AttractionBehavior posAtt;
    private AttractionBehavior cent1, cent2, cent3;
    private float radius, flit = .5f;
    private float soluteStr = .00f;
    private float negStr = .2f; 
    private float posStr = .00f; 
    private float centerStr = .035f; // attracts everything to middle
    private Vec2D normGrav, mv;
    private Timer forceTime, stopDelay;
    
    
    
// ***** For now work on freezing and heating things up - may be able to do this by changing value of flit (move of negative element)
    // ****** To freeze simply loop through particles and lock them all at a certain level of temp slider
    // ****** Turn timer and move behavior off at .9
    // ****** Increase/Decrease Center Strength and Jitter at same time - replaced jitter with move again, seems to work better
// *** look into setting beaker area to circle
// ***** Need to stop solutes from attracting each other - perhaps just have attractions on negatives and positives ???
    
    
    
    VerletParticle2D center1, center1a, center2, center3, top1, left1, right1, bottom1;
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
        physics1.setDrag(.4f); 
        physics2.setDrag(.4f);
        physics3.setDrag(.4f);                                                 // this may be usefull when trying to freeze the water
        physics1.setWorldBounds(new Rect(30, 30, 190, 155));
        physics2.setWorldBounds(new Rect(305, 30, 190, 155));
        physics3.setWorldBounds(new Rect(580, 30, 190, 155));

        // center attraction points to move everything toward center as it freezes
        center1 = new Element(new Vec2D(125, 102));
        center1.lock();
        cent1 = new AttractionBehavior(center1, width, centerStr * 2f);
        physics1.addParticle(center1);
        physics1.addBehavior(cent1);
        
        center2 = new Element(new Vec2D(400, 102));
        center2.lock();
        cent2 = new AttractionBehavior(center2, width, centerStr * .9f);
        physics2.addParticle(center2);
        physics2.addBehavior(cent2);

        center3 = new Element(new Vec2D(675, 102));
        center3.lock();
        cent3 = new AttractionBehavior(center3, width, centerStr * .8f);
        physics3.addParticle(center3);
        physics3.addBehavior(cent3);

        negA = new ArrayList<>();
        negB = new ArrayList<>();
        negC = new ArrayList<>();
        posA = new ArrayList<>();
        posB = new ArrayList<>();
        posC = new ArrayList<>();
        solB = new ArrayList<>();
        solC = new ArrayList<>();

        radius = 4;



        forceTime = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        // AEB not sure about this - need three of these, one for each beaker
                
            }
        });

        stopDelay = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

//        forceTime.start();

        mv = new Vec2D(random(-flit, flit), random(-flit, flit));
        normGrav = new Vec2D(0.0f, 0.00f);
        grav = new GravityBehavior(normGrav);
        move = new ConstantForceBehavior(mv);  // sets the force to be only one thing, never changing unless I reinitialize it below

        //negA list fill and add behaviors
        for (int i = 0; i < numParts; i++) {                                    // AEB break these into separate methods to reduce clutter in setup method
            negA.add(new Element(new Vec2D(random(35, 220), random(35, 185))));

            Element nA = negA.get(i);

            negAtt = new AttractionBehavior(nA, radius * 4, negStr);

            physics1.addParticle(nA);
            physics1.addBehavior(negAtt);
        }
        

        //negB list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            negB.add(new Element(new Vec2D(random(310, 490), random(35, 185))));

            Element nB = negB.get(i);

            negAtt = new AttractionBehavior(nB, radius * 4, negStr);
            physics2.addParticle(nB);
            physics2.addBehavior(negAtt);
        }

        //negC list fill and add behaviors
        for (int i = 0; i < numParts; i++) {
            negC.add(new Element(new Vec2D(random(585, 770), random(35, 185))));

            Element nC = negC.get(i);

            negAtt = new AttractionBehavior(nC, radius * 4, negStr);
            physics3.addParticle(nC);
            physics3.addBehavior(negAtt);
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
            solB.add(new Element(new Vec2D(random(350, 450), random(60, 130))));
            Element s2 = solB.get(i);

            soluteAtt = new AttractionBehavior(s2, radius * 2, soluteStr);
            
            physics2.addParticle(s2);
            physics2.addBehavior(soluteAtt);
            s2.addBehavior(grav);
            
            VerletConstrainedSpring2D s2s = new VerletConstrainedSpring2D(s2, center2, 50f, .00008f);
            physics2.addSpring(s2s);
            

            for (int j = 0; j < negB.size(); j++) {
                Element a = negB.get(j);
                Element b = posB.get(j);
                physics2.addSpring(new VerletMinDistanceSpring2D(s2, a, 8f, 1f));
                physics2.addSpring(new VerletMinDistanceSpring2D(s2, b, 13f, 1f));
            }
        }

        // setup solutes for solC and set springs to keep min distance from positive and negative elements
        for (int i = 0; i < numSolutes * 1.5; i++) {
            solC.add(new Element(new Vec2D(random(625, 725), random(60, 130))));
            Element s3 = solC.get(i);

            soluteAtt = new AttractionBehavior(s3, radius * 4, soluteStr);

            physics3.addParticle(s3);
            physics3.addBehavior(soluteAtt);
            s3.addBehavior(grav);
            
            VerletConstrainedSpring2D s3s = new VerletConstrainedSpring2D(s3, center3, 50f, .00008f);
            physics3.addSpring(s3s);

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
                physics2.addSpring(new VerletMinDistanceSpring2D(ai, aj, 12f, 1f));
            }
        }

        // set springs for solC to keep solutes away from each other
        for (int i = 0; i < solC.size() - 1; i++) {
            VerletParticle2D ai = solC.get(i);
            for (int j = i + 1; j < solC.size(); j++) {
                VerletParticle2D aj = solC.get(j);
                physics3.addSpring(new VerletMinDistanceSpring2D(ai, aj, 12f, 1f));
            }
        }
    }

    /**
     * check if Element is at the edge and reverse it's direction also trigger
     * Winning conditions based on first particles of each Element to reach
     * finish line
     */
    public void checkEdges(VerletParticle2D p, int left, int right) {           // AEB not being used

        float r = 10;

        if (p.x + r >= right) {           
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
        } else if (p.x - r <= left) {
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
        }
        if (p.y + r >= 185) {
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
        } else if (p.y - r <= 25) {
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
        }
        p.update();
    }

    @Override
    public void draw() {
        cent1.setStrength(centerStr);
        cent2.setStrength(centerStr);
        cent3.setStrength(centerStr);

        background(200);

        line(25, 15, 25, 190);
        line(25, 190, 225, 190);
        line(225, 190, 225, 15);

        line(300, 15, 300, 190);
        line(300, 190, 500, 190);
        line(500, 190, 500, 15);

        line(575, 15, 575, 190);
        line(575, 190, 775, 190);
        line(775, 190, 775, 15);

        fill(0);
        noStroke();
        ellipse(center1.x, center1.y, radius * 2, radius * 2);
        ellipse(center2.x, center2.y, radius * 2, radius * 2);
        ellipse(center3.x, center3.y, radius * 2, radius * 2);

        update1();
        update2();
        update3();

        //reset particles for testing purposes  AEB - pull this before distribution
        if (mousePressed) {

            for (int i = 0; i < negA.size(); i++) {
                negA.get(i).lock();
                negA.get(i).set(random(35, 220), random(35, 185));
                negA.get(i).addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
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
                solB.get(i).set(random(350, 450), random(60, 130));
            }
            for (int i = 0; i < solC.size(); i++) {
                solC.get(i).lock();
                solC.get(i).set(random(625, 725), random(60, 130));
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
    }

    @Override
    public void keyPressed() {
//        if(key == ESC){
//            key = 0;
//        }
        bStop = !bStop;
        if (bStop) {
            noLoop();
        } else {
            loop();
        }
    }
    
//    @Override
//    public void mousePressed() {
//        bStop = !bStop;
//        if (bStop) {
//            noLoop();
//        } else {
//            loop();
//        }
//    }

    public float getCenterStr() {
        return centerStr;
    }

    public void setCenterStr(float centerStr) {
        this.centerStr = centerStr;
    }

    public void lockParts() {
        for (Element p : negA) {
            p.lock();
        }
        for (Element p : negB) {
            p.lock();
        }
        for (Element p : negC) {
            p.lock();
        }
        for (Element p : posA) {
            p.lock();
        }
        for (Element p : posB) {
            p.lock();
        }
        for (Element p : posC) {
            p.lock();
        }
        for (Element p : solB) {
            p.lock();
        }
        for (Element p : solC) {
            p.lock();
        }
    }

    public void unlockParts() {
        for (Element p : negA) {
            p.unlock();
        }
        for (Element p : negB) {
            p.unlock();
        }
        for (Element p : negC) {
            p.unlock();
        }
        for (Element p : posA) {
            p.unlock();
        }
        for (Element p : posB) {
            p.unlock();
        }
        for (Element p : posC) {
            p.unlock();
        }
        for (Element p : solB) {
            p.unlock();
        }
        for (Element p : solC) {
            p.unlock();
        }
    }

    public float getFlit() {
        return flit;
    }

    public void setFlit(float flit) {
        this.flit = flit;
    }

    public void update1() {
        
        //display negative particles of negA and check for proximity to solutes
        for (Element p : negA) {
            negAtt.setAttractor(p);
            negAtt.setStrength(negStr);
            negAtt.apply(p);
            
            float c1;
            
            if(centerStr < 0){
                c1 = centerStr / 2;
            }else{
                c1 = centerStr * 2f;
            }
            cent1.setStrength(c1);
            
//        System.out.println("NegAtt Str:" + negAtt.getStrength());               // AEB how to set the attraction behavior strength for all particles
            
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));

            checkEdges(p, 37, 213);
            
            fill(255, 0, 0); // red

//            count++;
            
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }

        //display positive particles for posA
        for (Element p : posA) {     
            fill(0, 0, 255); // blue
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }
        physics1.update();
    }

    public void update2() {
        //display negative particles of negB and check for proximity to solutes
        for (Element p : negB) {
            negAtt.setAttractor(p);
            negAtt.setStrength(negStr);
            negAtt.apply(p);
            
            float c2;
            
            if(centerStr < 0){
                c2 = centerStr / 1.35f;
            }else{
                c2 = centerStr;
            }
            cent2.setStrength(c2);

            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
            
            checkEdges(p, 312, 488);

//            for (Element a : solB) {
//                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {
//                    mv = new Vec2D(0,0);
//                    move.setForce(mv);
//                    p.lock();
//                    p.removeBehavior(move);
//                    p.unlock();
//                }
//            }
            

            fill(255, 0, 0); // red
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }

        //display positive particles for posB
        for (Element p : posB) {
//            p.update();
            
            fill(0, 0, 255); // blue
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);

        }

        //display solute particles for solB and check for proximity to negB particles
        for (Element a : solB) {

            if (!a.behaviors.contains(grav)) {
                a.addBehavior(grav);
            }

            for (Element p : negB) {

                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {                              // AEB needs help
                    a.removeBehavior(grav);
                }
            }
            
//            a.update();
            
            fill(255, 255, 0); // yellow
            stroke(0);
            strokeWeight(2);
            ellipse(a.x, a.y, radius * 2, radius * 2);
        }   
        physics2.update();
    }

    public void update3() {
        //display negative particles of negC and check for proximity to solutes
        for (Element p : negC) {           
            negAtt.setAttractor(p);
            negAtt.setStrength(negStr);
            negAtt.apply(p);
            
            if(p == negC.get(negC.size() - 1)){
               negAtt.setStrength(-.1f);
               negAtt.apply(p);
            }
            
            float c3;
            
            if(centerStr < 0){
                c3 = centerStr / 1.35f;
            }else{
                c3 = centerStr * .85f;
            }
            cent3.setStrength(c3);
            
            p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));

            checkEdges(p, 587, 763);

//            for (Element a : solC) {
//                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {
//                    p.lock();
//                    p.removeBehavior(move);
//                    p.unlock();
//                }
//            }
            
            fill(255, 0, 0); // red
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }

        //display positive particles for posC
        for (Element p : posC) {
            fill(0, 0, 255); // blue
            noStroke();
            ellipse(p.x, p.y, radius * 2, radius * 2);
        }

        //display solute particles for solC and check for proximity to negC particles
        for (Element a : solC) {

            if (!a.behaviors.contains(grav)) {
                a.addBehavior(grav);
            }

            for (Element p : negC) {

                if ((sqrt((a.x - p.x) * (a.x - p.x))) < 10 && (sqrt((a.y - p.y) * (a.y - p.y))) < 10) {                              // AEB needs help
                    a.removeBehavior(grav);
                }
            }

            fill(255, 255, 0); // yellow
            stroke(0);
            strokeWeight(2);
            ellipse(a.x, a.y, radius * 2, radius * 2);
        }
        physics3.update();
    }
    
    public float getNegStr() {
        return negStr;
    }

    public void setNegStr(float negStr) {
        this.negStr = negStr;
    }
}