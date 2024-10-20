package Provider;

import java.util.ArrayList;

import Models.Admin;
import Models.User;

import java.lang.reflect.InvocationTargetException;

public class InstanceProvider {

    private ArrayList<Instance> instances = new ArrayList<>(10);
    private User loggedinUser;

    public Object getInstance(Class<?> clazz) {
        for (Instance instance : instances) {
            if (clazz.equals(instance.getClass())) {
                return instance; // Return the existing instance if found
            }
        }
    
        try {
            // Create a new instance of the class using reflection
            Object newInstance = clazz.getDeclaredConstructor().newInstance();
            instances.add((Instance) newInstance); // Add the newly created instance to the list
            return newInstance; // Return the new instance
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace(); // Print the exception details to the console
            return null; // Return null if something goes wrong
        }
    }
    
    public void addNewObject(Instance instance){
        instances.add(instance);
    }

    public void removeObject(Instance instance){
        instances.remove(instance);
    }

    public void setLoggedinUser(User user){
        this.loggedinUser = user;
    }
    
    public void logUserOut(){
        this.loggedinUser = null;
    }

    public User getCurrentUser(){
        return loggedinUser;
    }

    public boolean IsAdmin(){
        if(loggedinUser != null && loggedinUser instanceof Admin){
            return true;
        }
        return false;
    }
}
