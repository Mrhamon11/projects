package logging;

import java.io.Serializable;

/**
 * Created by Avi on 12/31/2016.
 */
public class Count implements Serializable {
    private int numOfModifications;

    public Count(){
        this.numOfModifications = 0;
    }

    public int getNumOfModifications() {
        return this.numOfModifications;
    }

    public void addOne(){
        this.numOfModifications++;
    }

    public void resetCount(){
        this.numOfModifications = 0;
    }
}
