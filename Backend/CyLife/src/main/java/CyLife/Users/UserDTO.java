package CyLife.Users;

public class UserDTO {
    private int userId;
    private String name;
    private String email;
    private String type;

    public UserDTO(int userId, String name, String email, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
