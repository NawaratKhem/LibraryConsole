package Controller;

import java.util.ArrayList;

import Models.Teacher;
import Provider.Instance;

public class TeacherController implements Instance {

    private ArrayList<Teacher> teachers = new ArrayList<>();

    public TeacherController(ArrayList<Teacher> teachers){
        this.teachers = teachers;
    }
    
    public ArrayList<Teacher> getTeachers(){
        return teachers;
    }
}
