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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "card_lists")
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @OneToMany(mappedBy = "cardList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Card> cardList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @NonNull
    private String title;

    public CardList() {
        this.title = "New Card List";
    }

    public void removeCard(Card card) {
        this.cardList.remove(card);
    }

    public void addCard(Card card) {
        this.cardList.add(card);
    }

    /**
     * board is not serialized for network transfer, so this method must be used to get board's id by client
     * @return boardId
     */
    public long getBoardId() {
        return board.getId();
    }

    /**
     * @return Is {@link CardList} valid for network transfer
     */
    public boolean isNetworkValid() {
        return this.getCardList() != null
            && !isNullOrEmpty(this.getTitle());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
