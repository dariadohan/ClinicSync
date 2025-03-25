package Data;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String CNP;

    public User(int id,String name, String email, String username, String password, String CNP){
        this.id=id;
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
        this.CNP=CNP;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCNP() {
        return CNP;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
