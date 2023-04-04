package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Entity
@NoArgsConstructor(force = true)
@Table(name = "boards")
@JsonIdentityInfo(scope = Board.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Board implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @NonNull
    private String name;

    /* If null the board will not have a password. */
    private String password;

    @JsonIgnore
    private boolean editable = true;

    @JsonIgnore
    private final String defaultColor = "#ffffffff";

    @NonNull
    private String color;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Tag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderColumn(name = "card_list_index")
    private List<CardList> cardLists = new ArrayList<>();


    public Board(String name) {
        this.name = name;
        this.color = "#ffffffff";
    }

    /**
     * Adds an empty {@link CardList} to the board.
     *
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     * added
     */
    public boolean addCardList() {
        return addCardList(new CardList());
    }

    /**
     * Adds a {@link CardList} to the board.
     *
     * @param cardList the {@link CardList} to add
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     * added
     */
    public boolean addCardList(CardList cardList) {
        if (cardList == null)
            return false;
        if (this.cardLists.contains(cardList))
            return false;
        this.cardLists.add(cardList);
        return true;
    }

    /**
     * Removes a {@link CardList} from the board.
     *
     * @param cardList the {@link CardList} to remove
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     * removed
     */
    public boolean removeCardList(CardList cardList) {
        return this.cardLists.remove(cardList);
    }

    /**
     * Removes a {@link CardList} from the board.
     *
     * @param id the id of the {@link CardList} to remove
     * @return a {@link Boolean} indicating whether or not the {@link CardList} was
     * removed
     */
    public boolean removeCardList(long id) {
        CardList cardList = this.cardLists.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (cardList == null)
            return false;
        return removeCardList(cardList);
    }

    @Override
    public String toString() {
        return "Board [id=" + id + ", name=" + name + ", password=" + password + ", color=" + color + ", cardLists=" + cardLists + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board = (Board) o;
        return getId() == board.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @JsonIgnore
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * @return Is {@link Board} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return this.cardLists != null
                && !isNullOrEmpty(this.getName())
                && !isNullOrEmpty(this.getColor());
    }

    @JsonIgnore
    public boolean isEditable() {
        return editable;
    }

    @JsonIgnore
    public String getDefaultColor() {
        return defaultColor;
    }
}
