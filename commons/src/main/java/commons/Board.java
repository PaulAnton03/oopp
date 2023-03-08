package commons;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Entity
public class Board {
    @Id
    @NonNull
    private String name;

    /* If null the board will not have a password. */
    private String password;

    // TODO: Implement the list of list of cards.

    /**
     * Return whether or not the board is password protected.
     * 
     * @return a {@link Boolean}
     */
    public boolean isPasswordProtected() {
        return password != null;
    }
}
