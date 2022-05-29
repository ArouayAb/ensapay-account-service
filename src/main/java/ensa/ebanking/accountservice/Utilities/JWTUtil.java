package ensa.ebanking.accountservice.Utilities;

public class JWTUtil {
    public static final String SECRET = "secret";
    public static final String AUTH_HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final long EXPIRATION_ACCESS_TOKEN = 60 * 60 * 1000;
    public static final long EXPIRATION_REFRESH_TOKEN = 120 * 60 * 1000;

    // ACCESS TOKEN AND REFRESH TOKEN MUST BE LESS THANK 1 MINUTE FOR THE ADMIN SIDE :
    public static final long EXPIRATION_ADMIN_ACCESS_TOKEN = 5 * 60 * 1000;
    public static final long EXPIRATION_ADMIN_REFRESH_TOKEN = 15 * 60 * 1000;

}
