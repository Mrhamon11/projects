package testing;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import database.Course;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avi on 9/28/2017.
 */
public class GSONTest {
//    public static void main(String[] args) throws FileNotFoundException {
//        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//        List<Course> allCourses = new ArrayList<Course>();
//
//        InputStream inputStream = new FileInputStream(new File("src/main/resources/courses.json"));
//        Reader reader = new InputStreamReader(inputStream);
//        JsonObject section = parser.parse(reader).getAsJsonObject();
//        JsonArray sections = section.getAsJsonArray("section");
//        for(JsonElement je : sections){
//            JsonObject jo = je.getAsJsonObject();
//            String dept = jo.keySet().toArray()[0].toString();
//            JsonArray courses = jo.getAsJsonArray(dept);
//            for(JsonElement element : courses){
//                allCourses.add(gson.fromJson(element, Course.class));
//            }
//        }
////        System.out.println();
////        String[] a = {"12345"};
////        Course course = new Course("Avi's Course", a, "AVI");
////        allCourses.add(course);
////        System.out.println();
//
//        String json = gson.toJson(allCourses);
//
//        System.out.println(json);
//
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/main/resources/testjson.json")));
//            bw.write(json);
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Type type = new TypeToken<List<Course>>(){}.getType();
//        List<Course> secondCourseList = gson.fromJson(json, type);
//        System.out.println("");
//    }
}
