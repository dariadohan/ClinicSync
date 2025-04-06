package Data;

public class MedicalService {
    int id;
    private String name;
    private float price;
    private int duration;

    public MedicalService(int id, String name, float price, int duration){
        this.id=id;
        this.name=name;
        this.price=price;
        this.duration=duration;
    }

    public MedicalService(String name, float price, int duration){
        this.name=name;
        this.price=price;
        this.duration=duration;
    }

    public MedicalService() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
