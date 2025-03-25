package Data;

public class Pacient extends User{
    int phoneNo;
    Pacient(int id,String name, String email, String username, String password, String CNP, int phoneNo){
        super(id,name,email,username,password,CNP);
    }
}
