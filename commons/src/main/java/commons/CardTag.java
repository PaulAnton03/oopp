package commons;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "cards_tags")
public class CardTag {

    @Id
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


    @Data
    @Embeddable
    public static class CardTagId implements Serializable {
        private Long cardId;
        private Long tagId;
    }
}