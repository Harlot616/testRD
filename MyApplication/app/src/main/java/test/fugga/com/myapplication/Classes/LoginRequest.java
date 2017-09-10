package test.fugga.com.myapplication.Classes;

public class LoginRequest {
    public String grant_type;
    public String username;
    public String password;
    public String client_id;
    public String client_secret;
    public String scope;

    public LoginRequest(String grant_type, String username, String password, String client_id,
                        String client_secret, String scope) {
        this.grant_type = grant_type;
        this.username = username;
        this.password = password;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.scope = scope;
    }
}
