package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database class to be used in the JavaSocketClientServer project. Stores all of the Course objects for
 * the the Server to use when parsing requests.
 * Created by Avi on 9/29/2017.
 */
public class Database {
    private Map<String, List<Course>> deptToCourses;
    private Map<String, Integer> crnToListIndices;
    private Map<String, String> crnTodDept;

    /**
     * Creates a new database.
     */
    public Database(){
        this.deptToCourses = new HashMap<String, List<Course>>();
        this.crnToListIndices = new HashMap<String, Integer>();
        this.crnTodDept = new HashMap<String, String>();
    }

    /**
     * Gets all of the courses in a department.
     * @param dept the department
     * @return the list of courses in that department
     */
    public List<Course> getCoursesByDept(String dept){
        return this.deptToCourses.get(dept.toUpperCase());
    }

    /**
     * Adds a course to the database.
     * @param courseTitle the course title.
     * @param crn the course CRN code
     * @param dept the department
     */
    public void addCourse(String courseTitle, String crn, String dept){
        List<String> crns = new ArrayList<String>();
        crns.add(crn);
        Course course = new Course(courseTitle, crns, dept);
        addCourse(course);
    }

    /**
     * Adds a course to the database.
     * @param course the course to be added
     */
    public void addCourse(Course course){
        String dept = course.getDept().toUpperCase();
        List<String> crns = course.getCrn();

        //Adds new course into deptToCourses mapping
        List<Course> coursesInDept = this.deptToCourses.get(dept);
        if(coursesInDept == null){
            coursesInDept = new ArrayList<Course>();
        }
        coursesInDept.add(course);
        this.deptToCourses.put(dept, coursesInDept);

        /*Adds new course into crnToListIndices mapping to keep a reference of
        where in the list it is stored.*/
        int index = coursesInDept.size() - 1; //The end, since that's where we just added the course to.
        for(String crn : crns){
            this.crnToListIndices.put(crn, index);
            this.crnTodDept.put(crn, dept);
        }
    }

    /**
     * Returns all courses in all departments.
     * @return a list of all of the courses in every department
     */
    public List<Course> getAllCourses(){
        List<Course> allCourses = new ArrayList<Course>();
        for(String dept : this.deptToCourses.keySet()){
            allCourses.addAll(this.deptToCourses.get(dept));
        }
        return allCourses;
    }

    /**
     * Returns true if the supplied major code is in the database, false otherwise.
     * @param majorCode the major code we are searching for
     * @return true if the major code is in the database, false otherwise
     */
    public boolean containsMajorCode(String majorCode){
        return this.deptToCourses.keySet().contains(majorCode);
    }

    /**
     * Returns true if the supplied course exists in the supplied department, false otherwise.
     * @param courseTitle the course
     * @param dept the department
     * @return true if the supplied course exists in the supplied department, false otherwise
     */
    public boolean courseInDeptExists(String courseTitle, String dept){
        List<Course> coursesInDept = this.deptToCourses.get(dept);
        for(Course course : coursesInDept){
            if(course.getCourseTitle().equals(courseTitle)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the supplied CRN code to the supplied course in the supplied department.
     * @param courseTitle the course
     * @param crn the CRN code to be added
     * @param dept the department the course is in
     */
    public void addCRNToExistingCourseInDept(String courseTitle, String crn, String dept){
        List<Course> coursesInDept = this.deptToCourses.get(dept);
        for(int i = 0; i < coursesInDept.size(); i++){
            if(coursesInDept.get(i).getCourseTitle().equals(courseTitle)){
                Course course = coursesInDept.get(i);
                course.addCRN(crn);
                this.crnToListIndices.put(crn, i);
                this.crnTodDept.put(crn, dept);
                break;
            }
        }
    }

    /**
     * Returns true if the CRN code exists in the database, false otherwise.
     * @param crn the CRN code
     * @return true if the CRN code exists in the database, false otherwise
     */
    public boolean crnExists(String crn){
        return this.crnToListIndices.keySet().contains(crn);
    }

    /**
     * Deletes the CRN from the its course in the database. If the course only had one crn, the
     * entire course is deleted.
     * @param crn the CRN code to be deleted
     */
    public void deleteCourse(String crn){
        int index = this.crnToListIndices.get(crn);
        this.crnToListIndices.remove(crn);
        List<Course> coursesInDept = this.deptToCourses.get(this.crnTodDept.get(crn));
        Course course = coursesInDept.get(index);
        course.removeCRN(crn);
        if(course.getCrn().isEmpty()){
            coursesInDept.remove(course);
        }
    }
}
