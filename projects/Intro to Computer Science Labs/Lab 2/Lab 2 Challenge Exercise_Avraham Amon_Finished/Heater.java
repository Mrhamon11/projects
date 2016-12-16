
/**
 * Write a description of class Heater here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Heater
{
    // instance variables - replace the example below with your own
    private double temperature;
    private double min;
    private double max;
    private double increment;

    /**
     * Constructor for objects of class Heater
     */
    public Heater(double heaterMin, double heaterMax)
    {
        // initialise instance variables
        temperature = 15.0;
        min = heaterMin;
        max = heaterMax;
        increment = 5.0;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void warmer()
    {
        if (temperature <= max - increment) {
            temperature += increment;
        }
    }
    public void colder ()
    {
        if (temperature >= min + increment) {
            temperature -= increment;
        }
    }
    public double getTemperature()
    {
        return temperature;
    }
    public void setIncrement(double inc)
    { 
        if (inc > 0) {
            increment = inc;
        }
    }
}
