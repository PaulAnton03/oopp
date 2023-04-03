package commons;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "card_lists")
@JsonIdentityInfo(scope = CardList.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CardList implements DBEntity {

    @Id
    @SequenceGenerator(name="card_lists_seq", sequenceName="CARD_LISTS_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="card_lists_seq")
    protected long id;

    @OneToMany(mappedBy = "cardList", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderColumn(name = "card_index")
    private List<Card> cards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    @OrderColumn(name = "card_list_index")
    @JsonIncludeProperties("id")
    @EqualsAndHashCode.Exclude
    private Board board;

    @NonNull
    private String title;

    @NonNull
    private String color;

    public CardList() {
        this.title = "New Card List";
    }

    public CardList(String title) {
        this.title = title;
        this.color = "#b2b2ebff";
    }

    public boolean removeCard(Card card) {
        return this.cards.remove(card);
    }

    public boolean removeCard(long id) {
        Card card = this.getCards().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (card == null)
            return false;
        return removeCard(card);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void addCardAtPosition(Card card, int position) {
        this.cards.add(position, card);
    }

    @Override
    public String toString() {
        return "CardList [id=" + id + ", title=" + title + ", color=" + color + ", cards=" + cards + "]";
    }

    /**
     * @return Is {@link CardList} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return this.getCards() != null
                && !isNullOrEmpty(this.getTitle())
                && !isNullOrEmpty(this.getColor());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
