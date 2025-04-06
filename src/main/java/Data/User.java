package Data;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String CNP;
    private String phoneNo;
    private Specialty specialty;
    private Role role;

    public User(int id,String name, String email, String username, String password, String CNP, String phoneNo, Specialty  specialty, Role role){
        this.id=id;
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
        this.CNP=CNP;
        this.phoneNo=phoneNo;
        this.specialty=specialty;
        this.role=role;
    }

    public User(String name, String email, String username, String password, String CNP, String phoneNo, Specialty  specialty, Role role){
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
        this.CNP=CNP;
        this.phoneNo=phoneNo;
        this.specialty=specialty;
        this.role=role;
    }

    public User(String name, String email, String username, String password, String CNP, String phoneNo, Role role){
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
        this.CNP=CNP;
        this.phoneNo=phoneNo;
        this.role=role;
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

    public String getPhoneNo() { return phoneNo; }

    public Role getRole() {
        return role;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setId(int id) { this.id=id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
