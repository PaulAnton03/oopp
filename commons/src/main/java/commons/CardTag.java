package commons;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "cards_tags")
public class CardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @org.springframework.lang.NonNull
    //   @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
//    @MapsId("tagId")
    @NonNull
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public CardTag(Card card, Tag tag) {
        this.card = card;
        this.tag = tag;
    }

    public CardTag() {
    }

    @Override
    public String toString() {
        return "CardTag{" +
                "id=" + id +
                ", card=" + card.getTitle() + " card-id: " + card.getId() +
                ", tag=" + tag.getText() + " tag_id: " + tag.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardTag)) return false;
        CardTag cardTag = (CardTag) o;
        return Objects.equals(getId(), cardTag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public Card getCard() {
        return card;
    }

    public void setCard(@NonNull Card card) {
        this.card = card;
    }

    @NonNull
    public Tag getTag() {
        return tag;
    }

    public void setTag(@NonNull Tag tag) {
        this.tag = tag;
    }
}