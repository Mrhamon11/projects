/**
 * Simple class with methods that help the program.
 * Created by Avi on 4/23/2017.
 */
public class Utilities {
    /**
     * Determins if the string is an integer.
     * @param str The string to be parsed.
     * @return True if the string is an integer, false otherwise.
     */
    public static boolean isInt(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}