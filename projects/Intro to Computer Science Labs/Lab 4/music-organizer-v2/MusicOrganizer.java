import java.util.ArrayList;

/**
 * A class to hold details of audio files.
 * This version can play the files.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class MusicOrganizer
{
    // An ArrayList for storing the file names of music files.
    private ArrayList<String> files;
    // A player for the music files.
    private MusicPlayer player;

    /**
     * Create a MusicOrganizer
     */
    public MusicOrganizer()
    {
        files = new ArrayList<String>();
        player = new MusicPlayer();
    }

    /**
     * Add a file to the collection.
     * @param filename The file to be added.
     */
    public void addFile(String filename)
    {
        files.add(filename);
    }

    /**
     * Return the number of files in the collection.
     * @return The number of files in the collection.
     */
    public int getNumberOfFiles()
    {
        return files.size();
    }

    /**
     * List a file from the collection.
     * @param index The index of the file to be listed.
     */
    public void listFile(int index)
    {
        if(validIndex(index) == true) {
            String filename = files.get(index);
            System.out.println(filename);
        }        
    }

    /**
     * Remove a file from the collection.
     * @param index The index of the file to be removed.
     */
    public void removeFile(int index)
    {
        if(validIndex(index) == true) {
            files.remove(index);
        }   
    }

    /**
     * Start playing a file in the collection.
     * Use stopPlaying() to stop it playing.
     * @param index The index of the file to be played.
     */
    public void startPlaying(int index)
    {
        String filename = files.get(index);
        player.startPlaying(filename);
    }

    /**
     * Stop the player.
     */
    public void stopPlaying()
    {
        player.stop();
    }

    private boolean validIndex(int index)
    {
        if(index >= 0 && index <= (files.size()-1)) {
            return true;
        }
        else {
            System.out.println("Error!");
            return false;
        }
    }

    public void listAllFiles()
    {
        int index = 0; 
        System.out.println(files.size() + " tracks in the Organizer");
        System.out.println();
        for(String filename : files) {
            System.out.println(index + ": " + filename);
            index++;
        }
    }

    public void listAllFiles2()
    {
        int index = 0;
        System.out.println(files.size() + " tracks in the Organizer");
        System.out.println();
        while(index < files.size()) {
            String filename = files.get(index);
            System.out.println(index + ": " + filename);
            index++;
        }
    }

    public int findFirst(String searchString)
    {
        int index = 0;
        for(String filename : files) {
            if(filename.contains(searchString)){
                break;
            }
            else {
                if(index < files.size()-1) {
                    index++;
                }
            }
        }
        String filename = files.get(index);
        if(filename.contains(searchString)) {
            return index;
        }
        else {
            return -1;
        }
    }
}
