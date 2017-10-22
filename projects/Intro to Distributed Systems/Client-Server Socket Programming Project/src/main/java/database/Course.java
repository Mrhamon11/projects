package database;

import java.io.Serializable;
import java.util.List;

/**
 * Course class to be used in the JavaSocketClientServer project. Representation of a course
 * that is stored in the server database. Courses can have more than one CRN representing the
 * unique ID for that section.
 * Created by Avi on 9/29/2017.
 */
public class Course implements Serializable{
    private String courseTitle;
    private List<String> crn;
    private String dept;

    /**
     * Constructs a course from the supplied course title, list of CRNs, and department.
     * @param courseTitle the course title
     * @param crn the list of CRNs
     * @param dept the department
     */
    public Course(String courseTitle, List<String> crn, String dept){
        this.courseTitle = courseTitle;
        this.crn = crn;
        this.dept = dept;
    }

    /**
     * Returns the course title.
     * @return the course title
     */
    public String getCourseTitle() {
        return this.courseTitle;
    }

    /**
     * Sets the course title to the supplied value.
     * @param courseTitle the new title
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * Returns the list of CRN codes.
     * @return the list of CRN codes
     */
    public List<String> getCrn() {
        return this.crn;
    }

    /**
     * Sets the list of CRN codes to the supplied list.
     * @param crn the new list of CRN codes
     */
    public void setCrn(List<String> crn) {
        this.crn = crn;
    }

    /**
     * Returns the department the course is in.
     * @return the department the course is in
     */
    public String getDept() {
        return this.dept;
    }

    /**
     * Sets the department to the supplied value.
     * @param dept the new department
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * Adds a new CRN code to the course list.
     * @param crn the new CRN code to be added
     */
    public void addCRN(String crn){
        this.crn.add(crn);
    }

    /**
     * Removes the supplied CRN code from the list.
     * @param crn the CRN to be removed.
     */
    public void removeCRN(String crn){
        this.crn.remove(crn);
    }

    /**
     * The string representation of the course. Courses with multiple CRN codes will be
     * put on their own lines.
     * @return
     */
    @Override
    public String toString() {
        if(this.crn.size() == 1){
            return "CRN: " + this.crn.get(0) + "---" + "Course Name: " + this.courseTitle;
        }
        else{
            StringBuilder sb = new StringBuilder();
            for(String crn : this.crn){
                sb.append("CRN: " + crn + "---" + "Course Name: " + this.courseTitle + "\n");
            }
            return sb.toString().trim();
        }
    }
}
