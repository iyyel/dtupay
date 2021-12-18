package exceptions;

/**
 * @author Troels (s161791)
 */

public class ConfigException extends Exception{

        public ConfigException () {
            super();
        }

        public ConfigException (String errorMessage) {
            super(errorMessage);
        }
}
