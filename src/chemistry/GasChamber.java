/**
 * Programmers: Ed Broxson & Chase McCowan 
 * Date: 02/20/2013 
 * Purpose: Create and draw multiple particles for use in Chemistry Diffusion example.
 */
package chemistry;

import chemistry.Element;
import processing.core.PApplet;
import processing.core.PFont;

public class GasChamber extends PApplet {

    //variables
    private boolean bStop, gateOpen;
    private float diameter, vel1, vel2, x, y;
    private int  fRate = 85, mw1, mw2, numParts = 40;
    private int[] color1 = new int[3];
    private int[] color2 = new int[3];
    private static double time1, time2;
    private String gas1, gas2;
    //objects
    private Element[] particles = new Element[numParts];
    private PFont  delinFont, mainFont;

    /**
     * method to draw particles and text in box, runs continously
     */
    @Override
    public void draw() {
        // set frame rate, background and stroke colors
        frameRate(fRate);
        background(170);
        stroke(0);

        // paint gate box and text
        if (!gateOpen) {
            line(200, 0, 200, height);
            line(200, 25, 225, 25);
            line(225, 25, 225, 175);
            line(200, 175, 225, 175);

            textFont(mainFont);
            textAlign(CENTER, BOTTOM);
            pushMatrix();
            fill(0, 0, 255);
            translate(200, 100);
            rotate(HALF_PI);
            text("Gate", 0, 0);
            popMatrix();
        }

        // paint finsh line text
        textFont(mainFont);
        textAlign(CENTER, BOTTOM);
        pushMatrix();
        fill(0, 0, 255);
        translate(750, 100);
        rotate(HALF_PI);
        text("Finish Line", 0, 0);
        popMatrix();

        // paint deliniation line
        textFont(delinFont);
        textAlign(CENTER, TOP);
        pushMatrix();
        fill(0, 0, 255);
        translate(221, 0);
        text("-------------------------------------------------- 50 Meters ---------------------------------------------------", 265, 0);
        popMatrix();

        // update all particles when  colliding or reaching edges
        for (int i = 0; i < numParts; i++) {
            particles[i].update();
            particles[i].checkEdges();
        }

        // remove strokes for particle fill
        noStroke();

        // paint particles
        for (int j = 0; j < particles.length; j++) {
            x = particles[j].getLocation().x;
            y = particles[j].getLocation().y;
            diameter = particles[j].getDiameter();

            if (j % 2 == 0) {
                fill(color1[0], color1[1], color1[2]);
                ellipse(x, y, diameter, diameter);
                fill(100, 100, 100);
                ellipse(x+diameter, y, diameter, diameter);
            } else {
                fill(color2[0], color2[1], color2[2]);
                ellipse(x, y, diameter, diameter);
            }
        }
    }

    /**
     * Get color of first particle type as an array of 3 integers 0-255 each RGB
     * format
     */
    public int[] getColor1() {
        return color1;
    }

    /**
     * Get color of second particle type as an array of 3 integers 0-255 each
     * RGB format
     */
    public int[] getColor2() {
        return color2;
    }

    /**
     * Get the frame rate - speed the simulation moves
     */
    public int getFRate() {
        return fRate;
    }

    /**
     * Get Molecular Weight of first chosen Element
     */
    public int getMw1() {
        return mw1;
    }

    /**
     * Get Molecular Weight of second chosen Element
     */
    public int getMw2() {
        return mw2;
    }

    /**
     * Get Number of Particles in simulation
     */
    public int getNumParts() {
        return numParts;
    }

    /**
     * Get correct of Time it takes for first chosen Element to travel 50 Meters
     */
    public static double getTime1() {
        return time1;
    }

    /**
     * Get correct of Time it takes for second chosen Element to travel 50
     * Meters
     */
    public static double getTime2() {
        return time2;
    }

    /**
     * Get velocity of first chosen Element
     */
    public float getVel1() {
        return vel1;
    }

    /**
     * Get velocity of second chosen Element
     */
    public float getVel2() {
        return vel2;
    }

    /**
     * Get value of bStop - determines Pause and Play of simulation
     */
    public boolean isbStop() {
        return bStop;
    }

    /**
     * Get value of gateOpen
     */
    public boolean isGateOpen() {
        return gateOpen;
    }

    /**
     * method to pause and restart the drawing of this PApplet simulation
     */
    @Override
    public void keyPressed() {
        bStop = !bStop;
        if (bStop) {
            noLoop();
        } else {
            loop();
        }
    }

    /**
     * method to fill the start box with 20 of each element to be used in
     * simulation
     */
    public void particleFill(int part1, int part2) {

        float diam1 = 0;
        float diam2 = 0;
        float rate = 1.0f;
        double time = 1.0;

        switch (part1) {
            case 0:
                vel1 = rate;
                diam1 = 2;
                mw1 = 4;
                time1 = time;
                gas1 = "Helium";
                color1[0] = 0;  // black
                color1[1] = 0;
                color1[2] = 0;
                break;
            case 1:
                vel1 = rate / sqrt(5);
                diam1 = 5;
                mw1 = 20;
                time1 = time * sqrt(5);
                gas1 = "Neon";
                color1[0] = 0;  // green
                color1[1] = 255;
                color1[2] = 0;
                break;
            case 2:
                vel1 = rate / sqrt(10);
                diam1 = 6;
                mw1 = 40;
                time1 = time * sqrt(10);
                gas1 = "Argon";
                color1[0] = 255;  // red
                color1[1] = 0;
                color1[2] = 0;
        }

        switch (part2) {
            case 0:
                vel2 = rate;
                diam2 = 2;
                mw2 = 4;
                time2 = time;
                gas2 = "Helium";
                color2[0] = 255;  // white
                color2[1] = 255;
                color2[2] = 255;
                break;
            case 1:
                vel2 = rate / sqrt(5);
                diam2 = 5;
                mw2 = 20;
                time2 = time * sqrt(5);
                gas2 = "Neon";
                color2[0] = 0;  // dk green
                color2[1] = 100;
                color2[2] = 0;
                break;
            case 2:
                vel2 = rate / sqrt(10);
                diam2 = 6;
                mw2 = 40;
                time2 = time * sqrt(10);
                gas2 = "Argon";
                color2[0] = 100;  // blue-gray
                color2[1] = 100;
                color2[2] = 100;
                break;
            case 3:
                vel2 = rate / sqrt(20);
                diam2 = 9;
                mw2 = 80;
                time2 = time * sqrt(20);
                gas2 = "Unknown1";
                color2[0] = 255;  // purple
                color2[1] = 0;
                color2[2] = 255;
                break;
            case 4:
                vel2 = rate / sqrt(16);
                diam2 = 7;
                mw2 = 64;
                time2 = time * sqrt(16);
                gas2 = "Unknown2";
                color2[0] = 255;  // yellow
                color2[1] = 255;
                color2[2] = 0;
                break;
            case 5:
                vel2 = rate / sqrt(4);
                diam2 = 4;
                mw2 = 16;
                time2 = time * sqrt(4);
                gas2 = "Unknown3";
                color2[0] = 0;  // blue
                color2[1] = 0;
                color2[2] = 255;
        }

        for (int i = 0; i < numParts; i++) {
            if (i % 2 == 0) {
                particles[i] = new Element(random(0, 180), random(0, height), random(-vel1, vel1), diam1, vel1, i, particles);
            } else {
                particles[i] = new Element(random(0, 180), random(0, height), random(-vel2, vel2), diam2, vel2, i, particles);
            }
        }
    }

    /**
     * Set value of bStop - determines Pause and Play of simulation
     */
    public void setbStop(boolean bStop) {
        this.bStop = bStop;
    }

    /**
     * Set color of first particle type as an array of 3 integers 0-255 each RGB
     * format
     */
    public void setColor1(int[] color1) {
        this.color1 = color1;
    }

    /**
     * Set color of second particle type as an array of 3 integers 0-255 each
     * RGB format
     */
    public void setColor2(int[] color2) {
        this.color2 = color2;
    }

    /**
     * Set frame rate - speed the simulation moves
     */
    public void setFRate(int fRate) {
        this.fRate = fRate;
    }

    /**
     * Set value of gateOpen in GasChamber and Element
     */
    public void setGateOpen(boolean gateOpen) {
        this.gateOpen = gateOpen;
        Element.setGateOpen(gateOpen);
    }

    /**
     * Set Molecular Weight of first chosen Element
     */
    public void setMw1(int mw1) {
        this.mw1 = mw1;
    }

    /**
     * Set Molecular Weight of second chosen Element
     */
    public void setMw2(int mw2) {
        this.mw2 = mw2;
    }

    /**
     * Set number of particles used in simulation
     */
    public void setNumParts(int numParts) {
        this.numParts = numParts;
    }

    /**
     * Set correct of Time it takes for first chosen Element to travel 50 Meters
     */
    public static void setTime1(double time1) {
        GasChamber.time1 = time1;
    }

    /**
     * Set correct of Time it takes for second chosen Element to travel 50
     * Meters
     */
    public static void setTime2(double time2) {
        GasChamber.time2 = time2;
    }

    /**
     * setup size of sketch and other items that are needed before drawing
     */
    @Override
    public void setup() {
        frameRate(fRate);
        size(775, 200);
        mainFont = createFont("sans-serif", 24);
        delinFont = createFont("sans-serif", 14);
    }
}
