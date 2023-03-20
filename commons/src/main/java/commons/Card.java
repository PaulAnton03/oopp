package commons;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @SequenceGenerator(name="cards_seq", sequenceName="CARDS_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cards_seq")
    protected long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_list_id", nullable = false)
    private CardList cardList;

    @NonNull
    private String title;
    @NonNull
    private String description;

    /**
     * cardList is not serialized for network transfer, so this method must be used to get cardList's id by client
     * @return boardId
     */
    public long getCardListId() {
        return cardList.getId();
    }

    /**
     * @return Is {@link Card} valid for network transfer
     */
    public boolean isNetworkValid() {
        return !isNullOrEmpty(this.getTitle())
            && !isNullOrEmpty(this.getDescription());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
