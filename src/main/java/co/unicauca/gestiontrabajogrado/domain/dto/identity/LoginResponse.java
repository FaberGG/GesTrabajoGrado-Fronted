package co.unicauca.gestiontrabajogrado.domain.dto.identity;
public class LoginResponse {
    private UserProfile user;
    private String token;

    // Constructor, getters, setters
    public LoginResponse() {}

    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}