package commons;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "cards_tags")
public class CardTag {

    @EmbeddedId
    private CardTagId id;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // constructors, getters and setters

    @Data
    @Embeddable
    public static class CardTagId implements Serializable {
        private Long cardId;
        private Long tagId;
    }
}