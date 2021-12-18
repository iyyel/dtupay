package infrastructure.token.interfaces;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public interface ITokenService {
    String getToken();
    boolean validateToken();
}
