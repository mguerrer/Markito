package cl.set.markito.tests.MarkitoREST.framework.types;

public class Camera {
    int id;
    String name;
    int rover_id;
    String full_name;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRover_id() {
        return rover_id;
    }
    public void setRover_id(int rover_id) {
        this.rover_id = rover_id;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}