/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package dto;

import java.io.Serializable;

public class Token implements Serializable {

    public Token() {
    }

    private String id;

    public Token(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id='" + id + '\'' +
                '}';
    }

}