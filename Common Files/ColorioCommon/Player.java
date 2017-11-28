package ColorioCommon;

import java.io.Serializable;

import static ColorioCommon.Constants.*;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Player implements Serializable{
    private Centroid top;
    private Centroid bottom;
    private Centroid left;
    private Centroid right;

    public Player(Centroid top, Centroid bottom, Centroid left, Centroid right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public Centroid getTop() {
        return top;
    }

    public Centroid getBottom() {
        return bottom;
    }

    public Centroid getLeft() {
        return left;
    }

    public Centroid getRight() {
        return right;
    }

    /**
     * Moves all centroids in the given direction(s) for the given time interval
     * @param horizontalDir positive, negative or no (0) movement on the x-Axis
     * @param verticalDir positive, negative or no (0) movement on the y-Axis
     * @param timeInterval how long the centroids moved (for linear movement)
     * @implNote s=v*t movement, at the edge of the map moves slower (v*sqrt(2)) if diagonal movement would be expected
     */
    public void movePlayer(int horizontalDir, int verticalDir, long timeInterval){

        //Forcing -1/0/1
        int hDir = 0;
        int vDir = 0;
        if(horizontalDir > 0){
            hDir = 1;
        }else if(horizontalDir < 0){
            hDir = -1;
        }
        if(verticalDir > 0){
            vDir = 1;
        }else if(verticalDir < 0){
            vDir = -1;
        }

        //Calculate movement vectors (same for all Centroids)
        double velocity = baseSpeed;
        double movementVectorX = hDir * (velocity * timeInterval)/((abs(hDir) + abs(vDir)) == 2 ? sqrt(2.0) : 1.0);
        double movementVectorY = vDir * (velocity * timeInterval)/((abs(hDir) + abs(vDir)) == 2 ? sqrt(2.0) : 1.0);

        double newTopX = getTop().getX() + movementVectorX;
        double newTopY = getTop().getY() + movementVectorY;
        double newBotX = getBottom().getX() + movementVectorX;
        double newBotY = getBottom().getY() + movementVectorY;
        double newLeftX = getLeft().getX() + movementVectorX;
        double newLeftY = getLeft().getY() + movementVectorY;
        double newRightX = getRight().getX() + movementVectorX;
        double newRightY = getRight().getY() + movementVectorY;

        //Check if stays inside the Map (should work if Centroids keep to their names)
        boolean vOk = false;
        boolean hOk = false;
        if(newLeftX > 0 && newRightX < mapMaxX){
            hOk = true;
        }
        if(newTopY > 0 && newBotY < mapMaxY){
            vOk = true;
        }

        //Set values (if OK)
        top.setLocation(hOk ? newTopX : getTop().getX(), vOk ? newTopY : getTop().getY());
        bottom.setLocation(hOk ? newBotX : getBottom().getX(), vOk ? newBotY : getBottom().getY());
        left.setLocation(hOk ? newLeftX : getLeft().getX(), vOk ? newLeftY : getLeft().getY());
        right.setLocation(hOk ? newRightX : getRight().getX(), vOk ? newRightY : getRight().getY());


    }

    /**
     * Makes the centroids move towards their ideal distances from each other
     * @param timeInterval the last time
     */
    public void manageDistances(long timeInterval){
        //System.out.println(timeInterval + " " + top.weight);
        double velocity = baseSpeed;

        if((right.getX() - left.getX())/2 < (top.getWeight() / 10)){
            double movementVector = velocity * timeInterval / (4*timeInterval);

            double newTopX = top.getX();
            double newTopY = top.getY() - movementVector;
            double newBotX = bottom.getX();
            double newBotY = bottom.getY() + movementVector;
            double newLeftX = left.getX() - movementVector;
            double newLeftY = left.getY();
            double newRightX = right.getX() + movementVector;
            double newRightY = right.getY();

            if(newTopY > 0 && newBotY < mapMaxY && newLeftX > 0 && newRightX < mapMaxX){
                top.setLocation(newTopX, newTopY);
                bottom.setLocation(newBotX, newBotY);
                right.setLocation(newRightX, newRightY);
                left.setLocation(newLeftX, newLeftY);

            }
        }
    }

    /**
     * Grows all Centroids by equally distributing the given weight among them
     * @param weight The weight to grow the player with
     */
    public void growPlayer(double weight){
        top.weight += weight / 4;
        bottom.weight += weight / 4;
        left.weight += weight / 4;
        right.weight += weight / 4;
    }
}
