/**
 * Programmers: Ed Broxson & Chase McCowan
 * Date: 02/22/2013 
 * Purpose: Build particles and handle collisions and movement.
 */
package chemistry;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import chemistry.FreezePointDepression;
import processing.core.PVector;

public class Element {

    //variables
    private float diameter, mag, x, y;
    private float height = 200;
    private float width = 775;
    private float startBoxWidth = 200;
    private int id;
    private static boolean gateOpen = false;
    private static boolean isWinner = false;
    private static boolean isSecondWinner = false;
    private static int winnerId;
    //objects
    private Element[] others;
    private PVector location, velocity;
    private FreezePointDepression gd = new FreezePointDepression();              

    
    /**
     * No Arg Constructor to create an
     * Element
     */
    public Element() {
        location = new PVector(90, 100);
        x = location.x;
        y = location.y;
        velocity = new PVector(1, 1);
        diameter = 10;
        id = 0;
        mag = 1;
    }

    /**
     * Constructor to create an Element and associate it with all other Elements
     * via an array
     */
    public Element(float w, float h, float vin, float din, float min, int idin, Element[] oin) {
        location = new PVector(w, h);
        x = location.x;
        y = location.y;
        velocity = new PVector(vin, vin);
        diameter = din;
        id = idin;
        others = oin;
        mag = min;
    }

    /**
     * check if Element is at the edge and reverse it's direction also trigger
     * Winning conditions based on first particles of each Element to reach
     * finish line
     */
    public void checkEdges() {

        x += velocity.x;
        y += velocity.y;

        if (!gateOpen && x + (diameter * 2) / 2 > startBoxWidth) {
            x = startBoxWidth - (diameter * 2) / 2;
            velocity.x *= -1;
        } else if (x + (diameter * 2) / 2 > width) {
            if (!isWinner) {
                JOptionPane.showMessageDialog(null, "WINNER!");
                gd.setTableTime1(id % 2);                       
                isWinner = true;
                winnerId = id % 2;
            } else if (isWinner && !isSecondWinner && (winnerId != (id % 2))) {
                JOptionPane.showMessageDialog(null, "The first particle of the "
                        + "second Gas has crossed the finish line."
                        + "\nEnter the rate and molecular weight for each"
                        + " particle in the table below.");
                gd.setTableTime1(id % 2);                        
                isSecondWinner = true;
            }
        } else if (x - (diameter * 2) / 2 < 0) {
            x = (diameter * 2) / 2;
            velocity.x *= -1;
        }
        if (y + diameter / 2 > height) {
            y = height - diameter / 2;
            velocity.y *= -1;
        } else if (y - diameter / 2 < 0) {
            y = diameter / 2;
            velocity.y *= -1;
        }
    }

    /**
     * Get diameter of current Element
     */
    public float getDiameter() {
        return diameter;
    }

    /**
     * Get value of gateOpen
     */
    public static boolean isGateOpen() {
        return gateOpen;
    }

    /**
     * Get height boundary - used to check edges
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get value of isSecondWinner
     */
    public static boolean getIsSecondWinner() {
        return isSecondWinner;
    }

    /**
     * Get value of IsWinner
     */
    public static boolean getIsWinner() {
        return isWinner;
    }

    /**
     * Get location vector
     */
    public PVector getLocation() {
        return location;
    }

    /**
     * Get magnitude of Element - controls speed
     */
    public float getMag() {
        return mag;
    }

    /**
     * Get width boundary of starting box - used to check edges
     */
    public float getStartBoxWidth() {
        return startBoxWidth;
    }

    /**
     * Get velocity vector
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * Get width boundary - used to check edges
     */
    public float getWidth() {
        return width;
    }

    /**
     * Set diameter of Element
     */
    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    /**
     * Set value of gateOpen
     */
    public static void setGateOpen(boolean gateOpen) {
        Element.gateOpen = gateOpen;
    }

    /**
     * Set height boundary - used to check edges
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Set value of isSecondWinner
     */
    public static void setIsSecondWinner(boolean isSecondWinner) {
        Element.isSecondWinner = isSecondWinner;
    }

    /**
     * Set value of isWinner
     */
    public static void setIsWinner(boolean isWinner) {
        Element.isWinner = isWinner;
    }

    /** 
     * Set location vector
     */
    public void setLocation(PVector location) {
        this.location = location;
    }
    
    /**
     * Set magnitude of Element
     */
    public void setMag(float mag) {
        this.mag = mag;
    }

    /**
     * Set width boundary of starting box - used to check edges
     */
    public void setStartBoxWidth(float startBoxWidth) {
        this.startBoxWidth = startBoxWidth;
    }

    /**
     * Set velocity vector
     */
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Set width boundary - used to check edges
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * method to update the element information based on collisions
     */
    public void update() {

        //add velocity to location of Element to move it
        location.add(velocity);

        // This only handles the particles with id's greater than current id
        for (int i = id + 1; i < others.length; i++) {
            float dx = others[i].x - x;
            float dy = others[i].y - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float minDist = others[i].diameter / 2 + diameter / 2;
            if (distance < minDist) {
                float angle = (float) Math.atan2(dy, dx);
                float targetX = (float) (x + Math.cos(angle) * minDist);
                float targetY = (float) (y + Math.sin(angle) * minDist);
                float ax = (targetX - others[i].x);
                float ay = (targetY - others[i].y);
                velocity.x -= ax;
                velocity.y -= ay;
                others[i].velocity.x += ax;
                others[i].velocity.y += ay;
                others[i].velocity.normalize();
                // resets speed back to original speed
                if (i % 2 != 0) {
                    others[i].velocity.setMag(others[i].mag);
                } else {
                    others[i].velocity.setMag(others[i].mag);
                }
            }
        }

        // this handles particles with id's less than current id
        for (int i = id - 1; i >= 0; i--) {
            float dx = others[i].x - x;
            float dy = others[i].y - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float minDist = others[i].diameter / 2 + diameter / 2;
            if (distance < minDist) {
                float angle = (float) Math.atan2(dy, dx);
                float targetX = (float) (x + Math.cos(angle) * minDist);
                float targetY = (float) (y + Math.sin(angle) * minDist);
                float ax = (targetX - others[i].x);
                float ay = (targetY - others[i].y);
                velocity.x -= ax;
                velocity.y -= ay;
                others[i].velocity.x += ax;
                others[i].velocity.y += ay;
                others[i].velocity.normalize();
                // resets speed back to original speed
                if (i % 2 != 0) {
//                    others[i].velocity.setMag(others[i].mag);
                    others[i].velocity.x *= -1;
                } else {
                    others[i].velocity.setMag(others[i].mag);
                }
            }
        }
        //limit the Element speed to value of mag
        velocity.setMag(mag);
    }
}
