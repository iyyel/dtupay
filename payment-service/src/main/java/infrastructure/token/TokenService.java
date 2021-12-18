package infrastructure.token;

import infrastructure.token.interfaces.ITokenService;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public class TokenService implements ITokenService {

    private String baseUrl;

    public TokenService() {
        this.baseUrl = System.getenv("TOKENSERVICEPATH");
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getToken() {
       return null;
    }

    @Override
    public boolean validateToken() {
        return true;
    }

}
