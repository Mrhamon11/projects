package util;

/**
 * Created by aviam on 7/30/2017.
 */
public class Utilities {
    public static String convertIntToFolderString(int num){
        return num < 10 ? "0" + num : "" + num;
    }

    public static boolean isInt(String str){
        try{
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException nfe){
            return false;
        }
    }
}
