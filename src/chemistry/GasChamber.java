/**
 * Programmers: Ed Broxson Date: 04/01/2013 Purpose: Create and draw multiple
 * particles for use in Chemistry Freeze Point Depression example.
 */
package chemistry;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import processing.core.PApplet;
import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;

public class GasChamber extends PApplet {

    ArrayList<Element> negA, negB, negC; // negative
    ArrayList<Element> posA, posB, posC; // positive
    ArrayList<Element> solB, solC;
    private boolean bStop, isFrozen1, isFrozen2, isFrozen3, isTiming;
    private int numParts = 20;
    private int numSolutes1 = 3, numSolutes2 = 9;
    private GravityBehavior grav;
    private ConstantForceBehavior move;
    private AttractionBehavior soluteAtt;
    private AttractionBehavior negAtt;
    private AttractionBehavior posAtt;
    private AttractionBehavior cent1, cent2, cent3;
    private AttractionBehavior ba1, ba2, ba3, ba4, ba5, ba6, ba7, ba8, ba9, ba10, ba11, ba12, ba13, ba14, ba15, ba16;
    private AttractionBehavior ca1, ca2, ca3, ca4, ca5, ca6, ca7, ca8, ca9, ca10, ca11, ca12, ca13, ca14, ca15, ca16;
    private float radius, flit = .5f;
    private float soluteStr = .00f;
    private float negStr = .2f; 
    private float posStr = .00f; 
    private float centerStr = .008f; // attracts everything to middle
    private float edge = 35; // range of pusher particles for beakers 2 and 3
    private Vec2D normGrav, mv;
    private Timer time1, time2, time3;
    private int fp = 3500;
    private JOptionPane jp1, jp2, jp3;
    private JDialog jd1, jd2, jd3;
    private FreezePointDepression fpd = new FreezePointDepression();
    
    
// ***** look into setting beaker area to circle
    
   
    private VerletParticle2D center1, center2, center3;
    private VerletParticle2D b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16;
    private VerletParticle2D c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16;
    
    private VerletPhysics2D physics1;
    private VerletPhysics2D physics2;
    private VerletPhysics2D physics3;

    @Override
    public void setup() {
        size(800, 200);
        smooth();
        physics1 = new VerletPhysics2D();
        physics2 = new VerletPhysics2D();
        physics3 = new VerletPhysics2D();
        physics1.setDrag(.4f); 
        physics2.setDrag(.4f);
        physics3.setDrag(.4f);                                                 
        physics1.setWorldBounds(new Rect(30, 30, 190, 155));
        physics2.setWorldBounds(new Rect(305, 30, 190, 155));
        physics3.setWorldBounds(new Rect(580, 30, 190, 155));

        // center attraction points to move everything toward center as it freezes
        center1 = new Element(new Vec2D(125, 102));
        center1.lock();
        cent1 = new AttractionBehavior(center1, width, centerStr * 1.2f);
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
        
        setupBeaker2EdgeRepellers();
        setupBeaker3EdgeRepellers();
        

        negA = new ArrayList<>();
        negB = new ArrayList<>();
        negC = new ArrayList<>();
        posA = new ArrayList<>();
        posB = new ArrayList<>();
        posC = new ArrayList<>();
        solB = new ArrayList<>();
        solC = new ArrayList<>();

        radius = 4;


        //timer for the first beaker to freeze the particles when the temperature slider is below .2
        time1 = new Timer(fp, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        
               if(flit < .2 && !isFrozen1){
                time2.start();
                isFrozen1 = true;
                 
                jp1 = new JOptionPane("Beaker 1 Frozen",JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, "Some Text");
                jd1 = jp1.createDialog(null, "Beaker 1");
                jd1.setSize(175, 100);
                jd1.setLocation(445, 475);
                jd1.setVisible(true);                 
                
               }
            }
        });
        
        //timer for the second beaker to freeze the particles when the temperature slider is below .2
        time2 = new Timer(fp - 1500 + (numSolutes1 * 200), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                        
                if(flit < .2 && !isFrozen2){
                time3.start();
                isFrozen2 = true;
  
                jp2 = new JOptionPane("Beaker 2 Frozen",JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, "Some Text");
                jd2 = jp2.createDialog(null, "Beaker 2");
                jd2.setSize(175, 100);
                jd2.setLocation(720, 475);
                jd2.setVisible(true);
               }
            }
        });

        //timer for the third beaker to freeze the particles when the temperature slider is below .2
        time3 = new Timer(fp - 2000 + (numSolutes2 * 100), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(flit < .2 && !isFrozen3){
                isFrozen3 = true;

                noLoop();
                fpd.setTableEditable();
                fpd.setValuesVisible();
                
                jp3 = new JOptionPane("Beaker 3 Frozen",JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, "Some Text");
                jd3 = jp3.createDialog(null, "Beaker 3");
                jd3.setSize(175, 100);
                jd3.setLocation(995, 475);
                jd3.setVisible(true);
               }
            }
        });


//        mv = new Vec2D(random(-flit, flit), random(-flit, flit));
        normGrav = new Vec2D(0.0f, 0.0000f);
        grav = new GravityBehavior(normGrav);
//        move = new ConstantForceBehavior(mv);  // sets the force to be only one thing, never changing unless I reinitialize it below

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
        for (int i = 0; i < numSolutes1; i++) {
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
        for (int i = 0; i < numSolutes2; i++) {
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
                physics2.addSpring(new VerletMinDistanceSpring2D(ai, aj, 16f, 1f));
            }
        }

        // set springs for solC to keep solutes away from each other
        for (int i = 0; i < solC.size() - 1; i++) {
            VerletParticle2D ai = solC.get(i);
            for (int j = i + 1; j < solC.size(); j++) {
                VerletParticle2D aj = solC.get(j);
                physics3.addSpring(new VerletMinDistanceSpring2D(ai, aj, 16f, 1f));
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
        cent1.setStrength(centerStr * 1.2f);
        cent2.setStrength(centerStr * .7f);
        cent3.setStrength(centerStr * .6f);
        
        ba1.setStrength(-centerStr * 5);
        ba2.setStrength(-centerStr * 4);
        ba3.setStrength(-centerStr * 4);
        ba4.setStrength(-centerStr * 4);
        ba5.setStrength(-centerStr * 5);
        ba6.setStrength(-centerStr * 4);
        ba7.setStrength(-centerStr * 4);
        ba8.setStrength(-centerStr * 4);
        ba9.setStrength(-centerStr * 5);
        ba10.setStrength(-centerStr * 4);
        ba11.setStrength(-centerStr * 4);
        ba12.setStrength(-centerStr * 4);
        ba13.setStrength(-centerStr * 5);
        ba14.setStrength(-centerStr * 4);
        ba15.setStrength(-centerStr * 4);
        ba16.setStrength(-centerStr * 4);
        
        ca1.setStrength(-centerStr * 5);
        ca2.setStrength(-centerStr * 4);
        ca3.setStrength(-centerStr * 4);
        ca4.setStrength(-centerStr * 4);
        ca5.setStrength(-centerStr * 5);
        ca6.setStrength(-centerStr * 4);
        ca7.setStrength(-centerStr * 4);
        ca8.setStrength(-centerStr * 4);
        ca9.setStrength(-centerStr * 5);
        ca10.setStrength(-centerStr * 4);
        ca11.setStrength(-centerStr * 4);
        ca12.setStrength(-centerStr * 4);
        ca13.setStrength(-centerStr * 5);
        ca14.setStrength(-centerStr * 4);
        ca15.setStrength(-centerStr * 4);
        ca16.setStrength(-centerStr * 4);

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

//        fill(0);
//        noStroke();
//        ellipse(center1.x, center1.y, radius * 2, radius * 2);
//        ellipse(center2.x, center2.y, radius * 2, radius * 2);
//        ellipse(center3.x, center3.y, radius * 2, radius * 2);

        update1();      
        update2();
        update3();
        
        if(flit < .2){
            time1.start();
            isTiming = true;

        }else if(flit > .19 && isTiming){
            time1.stop();
            time2.stop();
            time3.stop();
//            unlockBeaker1();
//            unlockBeaker2();
//            unlockBeaker3();
            isFrozen1 = false;
            isFrozen2 = false;
            isFrozen3 = false;
            isTiming = false;
            fpd.setTableNotEditable();
            resetParts();
        }
        
    }

    @Override
    public void keyPressed() {
        if(key == ESC){
            key = 0;
        }
        bStop = !bStop;
        if (bStop) {
            noLoop();
        } else {
            loop();
        }
    }
    
    @Override
    public void mousePressed() {
        bStop = !bStop;
        if (bStop) {
            noLoop();
        } else {
            loop();
        }
    }

    public void resetParts(){
        for (int i = 0; i < negA.size(); i++) {
                negA.get(i).lock();
                negA.get(i).set(random(35, 220), random(35, 185));
//                negA.get(i).addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));
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
    
    public float getCenterStr() {
        return centerStr;
    }

    public void setCenterStr(float centerStr) {
        this.centerStr = centerStr;
    }

    public void lockBeaker1(){
        for (Element p : negA) {
            p.lock();
        }
        for (Element p : posA) {
            p.lock();
        }
    }
    
    public void lockBeaker2(){
        for (Element p : negB) {
            p.lock();
        }
        for (Element p : posB) {
            p.lock();
        }
        for (Element p : solB) {
            p.lock();
        }
    }
    
    public void lockBeaker3() {
        for (Element p : negC) {
            p.lock();
        }
        for (Element p : posC) {
            p.lock();
        }  
        for (Element p : solC) {
            p.lock();
        }
    }
    
    public void unlockBeaker1(){
        for (Element p : negA) {
            p.unlock();
        }
        for (Element p : posA) {
            p.unlock();
        }
    }
    
    public void unlockBeaker2(){
        for (Element p : negB) {
            p.unlock();
        }
        for (Element p : posB) {
            p.unlock();
        }
        for (Element p : solB) {
            p.unlock();
        }
    }

    public void unlockBeaker3(){
        for (Element p : negC) {
            p.unlock();
        }
        for (Element p : posC) {
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
            if(isTiming){
                negAtt.setAttractor(p);
                negAtt.setStrength(0);
                negAtt.apply(p);
            }
            
            if(!isFrozen1){
                negAtt.setAttractor(p);
                negAtt.setStrength(negStr);
                negAtt.apply(p);

                float cn1;

                if(centerStr < 0){
                    cn1 = centerStr / 2;
                }else{
                    cn1 = centerStr * 2f;
                }
                cent1.setStrength(cn1);

    //        System.out.println("NegAtt Str:" + negAtt.getStrength());               // AEB how to set the attraction behavior strength for all particles

                p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));

                checkEdges(p, 37, 213);
            }
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
            if(isTiming){
                negAtt.setAttractor(p);
                negAtt.setStrength(0);
                negAtt.apply(p);
            }
            
            if(!isFrozen2){
                negAtt.setAttractor(p);
                negAtt.setStrength(negStr);
                negAtt.apply(p);

                float cn2;

                if(centerStr < 0){
                    cn2 = centerStr / 1.35f;
                }else{
                    cn2 = centerStr;
                }
                cent2.setStrength(cn2);

                p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));

                checkEdges(p, 312, 488);
            }
            

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

        for(int i = 0; i < solB.size(); i++){
            if(i < numSolutes1 && numSolutes1 < solB.size()){
                solB.remove(i);
            }else if(solB.size() < numSolutes1){
                solB.add(new Element(new Vec2D(random(350, 450), random(60, 130))));
                Element s2 = solB.get(solB.size() - 1);

                soluteAtt = new AttractionBehavior(s2, radius * 4, soluteStr);

                physics2.addParticle(s2);
                physics2.addBehavior(soluteAtt);
                s2.addBehavior(grav);
                
                
                
                for (int j = 0; j < solB.size(); j++) {
                    VerletParticle2D aj = solB.get(j);
                    if(aj != s2){
                    physics2.addSpring(new VerletMinDistanceSpring2D(s2, aj, 16f, 1f));
                    }
                }

                VerletConstrainedSpring2D s2s = new VerletConstrainedSpring2D(s2, center2, 50f, .00008f);
                physics2.addSpring(s2s);

                for (int j = 0; j < negB.size(); j++) {
                    Element a = negB.get(j);
                    Element b = posB.get(j);
                    physics2.addSpring(new VerletMinDistanceSpring2D(s2, a, 8f, 1f));
                    physics2.addSpring(new VerletMinDistanceSpring2D(s2, b, 13f, 1f));
                }
            }
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
            
//            a.update();
            
            fill(255, 255, 0); // yellow
            stroke(0);
            strokeWeight(2);
            ellipse(a.x, a.y, radius * 2, radius * 2);
            }
        }   
        physics2.update();
    }

    public void update3() {
        //display negative particles of negC and check for proximity to solutes
        for (Element p : negC) { 
            if(isTiming){
                negAtt.setAttractor(p);
                negAtt.setStrength(0);
                negAtt.apply(p);
                centerStr += .00001f;
            }            
            
            if(!isFrozen3){
                negAtt.setAttractor(p);
                negAtt.setStrength(negStr);
                negAtt.apply(p);

                if(p == negC.get(negC.size() - 1)){
                   negAtt.setStrength(-.1f);
                   negAtt.apply(p);
                }

                float cn3;

                if(centerStr < 0){
                    cn3 = centerStr / 1.35f;
                }else{
                    cn3 = centerStr * .85f;
                }
                cent3.setStrength(cn3);

                p.addSelf(new Vec2D(random(-flit, flit), random(-flit, flit)));

                checkEdges(p, 587, 763);
            }
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

        
        for(int i = 0; i < solC.size(); i++){
            if(i < numSolutes2 && numSolutes2 < solC.size()){
                solC.remove(i);
            }else if(solC.size() < numSolutes2){
                solC.add(new Element(new Vec2D(random(625, 725), random(60, 130))));
                Element s3 = solC.get(solC.size() - 1);

                soluteAtt = new AttractionBehavior(s3, radius * 4, soluteStr);

                physics3.addParticle(s3);
                physics3.addBehavior(soluteAtt);
                s3.addBehavior(grav);
                
                
                
                for (int j = 0; j < solC.size(); j++) {
                    VerletParticle2D aj = solC.get(j);
                    if(aj != s3){
                    physics3.addSpring(new VerletMinDistanceSpring2D(s3, aj, 16f, 1f));
                    }
                }

                VerletConstrainedSpring2D s3s = new VerletConstrainedSpring2D(s3, center3, 50f, .00008f);
                physics3.addSpring(s3s);

                for (int j = 0; j < negC.size(); j++) {
                    Element a = negC.get(j);
                    Element b = posC.get(j);
                    physics3.addSpring(new VerletMinDistanceSpring2D(s3, a, 8f, 1f));
                    physics3.addSpring(new VerletMinDistanceSpring2D(s3, b, 13f, 1f));
                }
            }
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

    public int getNumSolutes1() {
        return numSolutes1;
    }

    public void setNumSolutes1(int numSolutes1) {
        this.numSolutes1 = numSolutes1;
    }

    public int getNumSolutes2() {
        return numSolutes2;
    }

    public void setNumSolutes2(int numSolutes2) {
        this.numSolutes2 = numSolutes2;
    }

    public boolean isIsFrozen1() {
        return isFrozen1;
    }

    public void setIsFrozen1(boolean isFrozen1) {
        this.isFrozen1 = isFrozen1;
    }

    public boolean isIsFrozen2() {
        return isFrozen2;
    }

    public void setIsFrozen2(boolean isFrozen2) {
        this.isFrozen2 = isFrozen2;
    }

    public boolean isIsFrozen3() {
        return isFrozen3;
    }

    public void setIsFrozen3(boolean isFrozen3) {
        this.isFrozen3 = isFrozen3;
    }
    
    public void setupBeaker2EdgeRepellers(){
        //setting up pusher particles for the edge of beaker 2
        b1 = new Element(new Vec2D(301, 16));
        b1.lock();
        ba1 = new AttractionBehavior(b1, edge + 10, -centerStr * 2);
        physics2.addParticle(b1);
        physics2.addBehavior(ba1);
        
        b2 = new Element(new Vec2D(351, 16));
        b2.lock();
        ba2 = new AttractionBehavior(b2, edge, -centerStr * 2);
        physics2.addParticle(b2);
        physics2.addBehavior(ba2);
        
        b3 = new Element(new Vec2D(401, 16));
        b3.lock();
        ba3 = new AttractionBehavior(b3, edge, -centerStr * 2);
        physics2.addParticle(b3);
        physics2.addBehavior(ba3);
        
        b4 = new Element(new Vec2D(451, 16));
        b4.lock();
        ba4 = new AttractionBehavior(b4, edge, -centerStr * 2);
        physics2.addParticle(b4);
        physics2.addBehavior(ba4);
        
        b5 = new Element(new Vec2D(499, 16));
        b5.lock();
        ba5 = new AttractionBehavior(b5, edge + 10, -centerStr * 2);
        physics2.addParticle(b5);
        physics2.addBehavior(ba5);      
        
        b6 = new Element(new Vec2D(499, 60));
        b6.lock();
        ba6 = new AttractionBehavior(b6, edge, -centerStr * 2);
        physics2.addParticle(b6);
        physics2.addBehavior(ba6);
        
        b7 = new Element(new Vec2D(499, 103));
        b7.lock();
        ba7 = new AttractionBehavior(b7, edge, -centerStr * 2);
        physics2.addParticle(b7);
        physics2.addBehavior(ba7);
        
        b8 = new Element(new Vec2D(499, 146));
        b8.lock();
        ba8 = new AttractionBehavior(b8, edge, -centerStr * 2);
        physics2.addParticle(b8);
        physics2.addBehavior(ba8);
        
        b9 = new Element(new Vec2D(499, 189));
        b9.lock();
        ba9 = new AttractionBehavior(b9, edge + 10, -centerStr * 2);
        physics2.addParticle(b9);
        physics2.addBehavior(ba9);
             
        b10 = new Element(new Vec2D(451, 189));
        b10.lock();
        ba10 = new AttractionBehavior(b10, edge, -centerStr * 2);
        physics2.addParticle(b10);
        physics2.addBehavior(ba10);
        
        b11 = new Element(new Vec2D(401, 189));
        b11.lock();
        ba11 = new AttractionBehavior(b11, edge, -centerStr * 2);
        physics2.addParticle(b11);
        physics2.addBehavior(ba11);
        
        b12 = new Element(new Vec2D(351, 189));
        b12.lock();
        ba12 = new AttractionBehavior(b12, edge, -centerStr * 2);
        physics2.addParticle(b12);
        physics2.addBehavior(ba12);
        
        b13 = new Element(new Vec2D(301, 189));
        b13.lock();
        ba13 = new AttractionBehavior(b13, edge + 10, -centerStr * 2);
        physics2.addParticle(b13);
        physics2.addBehavior(ba13);
        
        b14 = new Element(new Vec2D(301, 146));
        b14.lock();
        ba14 = new AttractionBehavior(b14, edge, -centerStr * 2);
        physics2.addParticle(b14);
        physics2.addBehavior(ba14);
        
        b15 = new Element(new Vec2D(301, 103));
        b15.lock();
        ba15 = new AttractionBehavior(b15, edge, -centerStr * 2);
        physics2.addParticle(b15);
        physics2.addBehavior(ba15);
        
        b16 = new Element(new Vec2D(301, 60));
        b16.lock();
        ba16 = new AttractionBehavior(b16, edge, -centerStr * 2);
        physics2.addParticle(b16);
        physics2.addBehavior(ba16);
    }
    
    public void setupBeaker3EdgeRepellers(){
        //setting up pusher particles for the edge of beaker 3
        c1 = new Element(new Vec2D(576, 16));
        c1.lock();
        ca1 = new AttractionBehavior(c1, edge + 10, -centerStr * 2);
        physics3.addParticle(c1);
        physics3.addBehavior(ca1);
        
        c2 = new Element(new Vec2D(626, 16));
        c2.lock();
        ca2 = new AttractionBehavior(c2, edge, -centerStr * 2);
        physics3.addParticle(c2);
        physics3.addBehavior(ca2);
        
        c3 = new Element(new Vec2D(676, 16));
        c3.lock();
        ca3 = new AttractionBehavior(c3, edge, -centerStr * 2);
        physics3.addParticle(c3);
        physics3.addBehavior(ca3);
        
        c4 = new Element(new Vec2D(726, 16));
        c4.lock();
        ca4 = new AttractionBehavior(c4, edge, -centerStr * 2);
        physics3.addParticle(c4);
        physics3.addBehavior(ca4);
        
        c5 = new Element(new Vec2D(774, 16));
        c5.lock();
        ca5 = new AttractionBehavior(c5, edge + 10, -centerStr * 2);
        physics3.addParticle(c5);
        physics3.addBehavior(ca5);
         
        c6 = new Element(new Vec2D(774, 60));
        c6.lock();
        ca6 = new AttractionBehavior(c6, edge, -centerStr * 2);
        physics3.addParticle(c6);
        physics3.addBehavior(ca6);
        
        c7 = new Element(new Vec2D(774, 103));
        c7.lock();
        ca7 = new AttractionBehavior(c7, edge, -centerStr * 2);
        physics3.addParticle(c7);
        physics3.addBehavior(ca7);
        
        c8 = new Element(new Vec2D(774, 146));
        c8.lock();
        ca8 = new AttractionBehavior(c8, edge, -centerStr * 2);
        physics3.addParticle(c8);
        physics3.addBehavior(ca8);
        
        c9 = new Element(new Vec2D(774, 189));
        c9.lock();
        ca9 = new AttractionBehavior(c9, edge + 10, -centerStr * 2);
        physics3.addParticle(c9);
        physics3.addBehavior(ca9);
            
        c10 = new Element(new Vec2D(726, 189));
        c10.lock();
        ca10 = new AttractionBehavior(c10, edge, -centerStr * 2);
        physics3.addParticle(c10);
        physics3.addBehavior(ca10);
        
        c11 = new Element(new Vec2D(676, 189));
        c11.lock();
        ca11 = new AttractionBehavior(c11, edge, -centerStr * 2);
        physics3.addParticle(c11);
        physics3.addBehavior(ca11);
        
        c12 = new Element(new Vec2D(626, 189));
        c12.lock();
        ca12 = new AttractionBehavior(c12, edge, -centerStr * 2);
        physics3.addParticle(c12);
        physics3.addBehavior(ca12);
        
        c13 = new Element(new Vec2D(576, 189));
        c13.lock();
        ca13 = new AttractionBehavior(c13, edge + 10, -centerStr * 2);
        physics3.addParticle(c13);
        physics3.addBehavior(ca13);
        
        c14 = new Element(new Vec2D(576, 146));
        c14.lock();
        ca14 = new AttractionBehavior(c14, edge, -centerStr * 2);
        physics3.addParticle(c14);
        physics3.addBehavior(ca14);
        
        c15 = new Element(new Vec2D(576, 103));
        c15.lock();
        ca15 = new AttractionBehavior(c15, edge, -centerStr * 2);
        physics3.addParticle(c15);
        physics3.addBehavior(ca15);
        
        c16 = new Element(new Vec2D(576, 60));
        c16.lock();
        ca16 = new AttractionBehavior(c16, edge, -centerStr * 2);
        physics3.addParticle(c16);
        physics3.addBehavior(ca16);
    }
    
}