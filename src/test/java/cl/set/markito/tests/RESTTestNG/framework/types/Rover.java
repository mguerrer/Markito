package cl.set.markito.tests.RESTTestNG.framework.types;

public class Rover {
    int id;
    String name;
    String landing_date;
    String launch_date;
    String status;
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
    public String getLanding_date() {
        return landing_date;
    }
    public void setLanding_date(String landing_date) {
        this.landing_date = landing_date;
    }
    public String getLaunch_date() {
        return launch_date;
    }
    public void setLaunch_date(String launch_date) {
        this.launch_date = launch_date;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}