package commons;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Entity
public class Board {
    @Id
    @Getter
    @NonNull
    private String name;

    @Getter
    /* If null the board will not have a password. */
    private String password;

    private List<CardList> cards = new ArrayList<>();

    /**
     * Return whether or not the board is password protected.
     * 
     * @return a {@link Boolean}
     */
    public boolean isPasswordProtected() {
        return password != null;
    }

    /**
     * Adds an empty {@link CardList} to the board.
     * 
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     *         added
     */
    public boolean addCardList() {
        return addCardList(new CardList());
    }

    /**
     * Adds a {@link CardList} to the board.
     * 
     * @param cardList the {@link CardList} to add
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     *         added
     * 
     */
    public boolean addCardList(CardList cardList) {
        if (cardList == null)
            return false;
        if (this.cards.contains(cardList))
            return false;
        this.cards.add(cardList);
        return true;
    }

    /**
     * Removes a {@link CardList} from the board.
     * 
     * @param cardList the {@link CardList} to remove
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     *         removed
     */
    public boolean removeCardList(CardList cardList) {
        return this.cards.remove(cardList);
    }

    /**
     * Removes a {@link CardList} from the board.
     * 
     * @param id the id of the {@link CardList} to remove
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     *         removed
     */
    public boolean removeCardList(long id) {
        CardList cardList = this.cards.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (cardList == null)
            return false;
        return removeCardList(cardList);
    }
}
