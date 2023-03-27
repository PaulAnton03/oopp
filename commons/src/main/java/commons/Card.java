package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
@JsonIdentityInfo(scope = Card.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Card {

    @Id
    @SequenceGenerator(name="cards_seq", sequenceName="CARDS_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cards_seq")
    protected long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_list_id", nullable = false)
    @OrderColumn(name = "card_index")
    @JsonIncludeProperties("id")
    @EqualsAndHashCode.Exclude
    private CardList cardList;

    @NonNull
    private String title;
    @NonNull
    private String description;

    @Override
    public String toString() {
        return "Card [id=" + id + ", title=" + title + ", description=" + description + "]";
    }

    /**
     * @return Is {@link Card} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return !isNullOrEmpty(this.getTitle());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
