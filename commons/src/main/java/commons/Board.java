package commons;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter
    @NonNull
    private String name;

    @Getter
    @Setter
    /* If null the board will not have a password. */
    private String password;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardList> cards = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Board [id=" + id + ", name=" + name + ", password=" + password + ", cards=" + cards + "]";
    }

    public boolean isValid() {
        return this.cards != null
            && !isNullOrEmpty(this.getName());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
