package cl.set.markito.tests.MarkitoREST.MarsPhotosAPIExample.framework.types;

public class Photo {
    int id;
    int sol;
    Camera camera;
    String img_src; 
    String earth_date;
    Rover rover;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSol() {
        return sol;
    }
    public void setSol(int sol) {
        this.sol = sol;
    }
    public Camera getCamera() {
        return camera;
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public String getImg_src() {
        return img_src;
    }
    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }
    public String getEarth_date() {
        return earth_date;
    }
    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }
    public Rover getRover() {
        return rover;
    }
    public void setRover(Rover rover) {
        this.rover = rover;
    }

}
