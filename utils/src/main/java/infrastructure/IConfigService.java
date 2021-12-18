package infrastructure;

import exceptions.ConfigException;

/**
 * @author Troels (s161791)
 */

public interface IConfigService {
    String getProp(String s) throws ConfigException;
}
