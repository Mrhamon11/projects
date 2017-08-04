package time_file_ds;

import util.Utilities;

import java.io.File;

/**
 * Created by aviam on 8/2/2017.
 */
public class TimeFile {
    private File file;
    private String name;

    public TimeFile(String path){
        this.file = new File(path);
        initName();
    }

    public TimeFile(File file){
        this.file = file;
        initName();
    }

    private void initName(){
        this.name = this.file.getName().split("\\.")[0]; //"file.type" -> "file"
        checkTimeName();
    }

    private void checkTimeName(){
        if(Utilities.isInt(this.name) && Utilities.isInt(getParent())){
            this.name = Integer.parseInt(getParent()) + ":" + getName();
        }
    }

    public String getName() {
        return this.name;
    }

    public String getParent(){
        return new File(this.file.getParent()).getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    public File getFile() {
        return this.file;
    }

    public static void main(String[] args) {
        TimeFile tf = new TimeFile("src/test/folder/file.type"); //Name -> file
        TimeFile tf2 = new TimeFile("src/test/12/03.type"); //Name -> 12:03
        TimeFile tf3 = new TimeFile("src/test/custom/01/15.type"); //Name -> 1:15
        System.out.println(tf);
        System.out.println(tf2);
        System.out.println(tf3);
    }
}
