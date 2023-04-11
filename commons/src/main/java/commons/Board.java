package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@NoArgsConstructor(force = true)
@Table(name = "boards")
@JsonIdentityInfo(scope = Board.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Board implements DBEntity {
    @Id
    @SequenceGenerator(name = "boards_seq", sequenceName = "BOARDS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boards_seq")
    protected long id;

    @NonNull
    private String name;

    /* If null the board will not have a password. */
    private String password;

    @JsonIgnore
    private boolean editable = true;

    @NonNull
    private String boardColor;
    @NonNull
    private String listColor;
    @NonNull
    private String cardColor;

    @NonNull
    private String fontColor;
    private String color;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Tag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderColumn(name = "card_list_index")
    private List<CardList> cardLists = new ArrayList<>();

    public Board(String name) {
        this.name = name;
        this.boardColor = "#ffffffff";
        this.cardColor = "#ffffffff";
        this.listColor = "#b2b2ebff";
        this.fontColor = "#000000ff";
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

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void addTag(Tag tag) {
        tagList.add(tag);
    }

    public boolean removeTag(long id){
        Tag tag = this.getTagList().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if(tag == null)
            return false;
        return removeTag(tag);
    }
    public boolean removeTag(Tag tag){
        return this.tagList.remove(tag);
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
        return "Board [id=" + id + ", name=" + name + ", password=" + password + ", cardLists="
                + cardLists +", tags="+ tagList + "]";
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
                && !StringUtil.isNullOrEmpty(this.getName());
    }

    @JsonIgnore
    public boolean isEditable() {
        return editable;
    }
}
