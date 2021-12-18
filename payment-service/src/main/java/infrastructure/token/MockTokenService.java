package infrastructure.token;

import infrastructure.token.interfaces.ITokenService;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public class MockTokenService implements ITokenService {

    public String getToken(){
        return "Dette er en token";
    }

    public boolean validateToken(){
        return true;
    }

}
