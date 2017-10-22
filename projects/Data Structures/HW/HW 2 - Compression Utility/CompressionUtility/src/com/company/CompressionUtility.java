package com.company;

import java.io.*;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class to compress any directory into either a .zip or .jar file. Users must enter 3 arguments from the command line: the absolute
 * directory of the directory they wish to compress, the output file type they want (either zip or jar), and the file type they wish
 * to include in a report. The report will include all files that were compressed that contain the file extension entered in.
 *
 * Created by Avi Amon 9/25/2016
 */
public class CompressionUtility {
    private String absoluteDir;
    private String outputFileType;
    private String reportFileType;
    private File outputFile;
    private File crcFile;
    private CRC32 crc;
    private File reportFile;

    public static void main(String[] args) {
        try {
            //CompressionUtility c = new CompressionUtility("C:\\Users\\Avi\\Desktop\\compression\\folder", "jar", "java");
            CompressionUtility c = new CompressionUtility(args[0], args[1], args[2]);
            c.compressFile(c.getOutputFileType(), c.getReportFileType());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Program accepts 3 arguments: the absolute directory, the output file (zip or jar), and the file type you wish " +
                    "to include in the report.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Constructor to create CompressionUtility object.
     *
     * @param absoluteDir    The full directory.
     * @param outputFileType The output file type (zip or jar)
     * @param reportFileType The file extension that users wish to include in a report.
     */
    public CompressionUtility(String absoluteDir, String outputFileType, String reportFileType) {
        this.absoluteDir = absoluteDir; //"C:\\Users\\Avi\\Desktop\\compression\\folder";
        this.outputFileType = outputFileType;
        this.outputFile = new File("out." + this.outputFileType);
        this.reportFileType = reportFileType;
        this.crcFile = new File("crc.txt");
        this.crc = new CRC32();
        this.reportFile = new File("report.txt");
    }

    /**
     * Main compression method. Given a full directory, output file type, and report file extension, this method will create either a .zip or .jar file
     * of all of the files that were compressed called out.zip/out.jar, it will create a text file called crc.txt that will contain the crc checksums of
     * all of the files that were compressed, and lastly, it will create a text file called report.txt that will list all of the compressed files that
     * had the desired file extension.
     *
     * @param outputFileType The output file type (zip or jar)
     * @param reportType     The file extension that users wish to include in a report.
     */
    public void compressFile(String outputFileType, String reportType) {

        try {
            //Create writers to writer to crc and report files.
            Writer crcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.crcFile)));
            Writer reportWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.reportFile)));

            //outputFileType is zip
            if (outputFileType.equals("zip")) {
                //Create out.zip in the same folder as the one whose contents will be compressed.
                ZipOutputStream zos = new
                        ZipOutputStream(new FileOutputStream("out.zip"));
                compress(this.absoluteDir, zos, crcWriter, reportWriter, reportType);
                zos.close();
            }

            //outputFileType is jar
            else if (outputFileType.equals("jar")) {
                //Create out.jar in the same folder as the one whose contents will be compressed.
                ZipOutputStream jos = new
                        JarOutputStream(new FileOutputStream("out.jar"));
                compress(this.absoluteDir, jos, crcWriter, reportWriter, reportType);
                jos.close();
            }

            //outputFileType was not zip or jar
            else {
                System.out.println("Output File Type must be either zip or jar");
            }

            //Close the writers.
            crcWriter.close();
            reportWriter.close();

            //Saves the three files in the directory we want to compress at the end.
            saveExternalFiles();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Compresses contents of a given directory into a zip or jar depending on user input
     * @param path The directory to be compressed.
     * @param zos Either a zip or jar output stream for files to be compressed into.
     * @param crcWriter A writer to write to the crc.txt file.
     * @param reportWriter A writer to write to the report.txt file.
     * @param reportType The file extension of the files that are to be included in the report.txt file.
     */
    private void compress(String path, ZipOutputStream zos, Writer crcWriter, Writer reportWriter, String reportType) {
        try {
            //Create a new file based on the entered directory path.
            File zipDir = new File(path);
            //Get an array of all of the child file/directories in the path.
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            //loop through dirList, and zip the files
            for (String file : dirList) {
                //File for the directory argument and each child file (both directories and files.
                File fullPath = new File(path, file);
                if (fullPath.isDirectory()) {
                    //The file is a directory, so call function recursively to
                    //Get to a file and zip it up.
                    String filePath = fullPath.getPath();
                    compress(filePath, zos, crcWriter, reportWriter, reportType);
                    continue;
                }

                //Used to make sure to not compress a file with a specific name. Calls isNotDesiredCompressed method to do the check.
                String pathEnd = fullPath.getPath();
                    //File was not a directory so continue here.
                    FileInputStream fis = new FileInputStream(fullPath);

                    //Get relative path of the file to be included in the zipping.
                    String relativePath = new File(this.absoluteDir).toURI().relativize(fullPath.toURI()).getPath();
                    //Create ZipEntry to add files to zip file.
                    ZipEntry toZip = new ZipEntry(relativePath);
                    //Put toZip in ZipOutputStream object
                    zos.putNextEntry(toZip);

                    //Write the content of the file to the ZipOutputStream
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    addCrc(fullPath.getPath(), crcWriter);

                    //Close the Stream
                    fis.close();

                    //If the relativePath file ends with the extension type we want, then write it to report.txt.
                    if (relativePath.contains("." + reportType)) {
                        reportWriter.write(relativePath + "\r\n");
                    }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a crc checksum to the crc.txt file for every inputted file.
     * @param fullPath The file path of the file.
     * @param crcWriter The writer to write to crc.txt.
     */
    private void addCrc(String fullPath, Writer crcWriter) {
        //I got part of this from the following website, and I changed some to make it fit with
        //what I thought worked. I got rid of the buffer. It seemed to work without it.
        //http://programming.mesexemples.com/java/io-nio/java-calculate-the-crc32-checksum-of-a-file/
        try {
            InputStream is = new FileInputStream(fullPath);
            for(int i = is.read(); i != -1; i = is.read()) {
                this.crc.update(i);
            }
            long newCrc = this.crc.getValue();
            crcWriter.write(newCrc + "\r\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Copies over data from temp files and stores them in the directory to be compressed.
     */
    private void saveExternalFiles(){
        //Some of this code came from https://www.mkyong.com/java/how-to-move-file-to-another-directory-in-java/
        try{
            File outSave = new File(this.absoluteDir + "\\out." + this.outputFileType);
            File crcSave = new File(this.absoluteDir + "\\" + this.crcFile.getPath());
            File reportSave = new File(this.absoluteDir + "\\" + this.reportFile.getPath());

            File[] inFiles = {this.outputFile, this.crcFile, this.reportFile};
            File[] outFiles = {outSave, crcSave, reportSave};

            for(int i = 0; i < outFiles.length; i++) {

                FileInputStream inStream = new FileInputStream(inFiles[i]);
                FileOutputStream outStream = new FileOutputStream(outFiles[i]);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }

                inStream.close();
                outStream.close();

                //delete the original file
                inFiles[i].delete();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Returns the output file type entered by the user.
     *
     * @return The output file type.
     */
    public String getOutputFileType() {
        return this.outputFileType;
    }

    /**
     * Returns the report file type entered by the user.
     *
     * @return The report file type.
     */
    public String getReportFileType() {
        return this.reportFileType;
    }
}