package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.*;

import javax.persistence.*;
import java.awt.*;


@Data
@Entity
@RequiredArgsConstructor
public class Tag {

    @Id
    @SequenceGenerator(name = "tags_seq", sequenceName = "TAGS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false)
    @OrderColumn(name = "tag_index")
    @JsonIncludeProperties("id")
    @EqualsAndHashCode.Exclude
    private Card card;

    //I am making this not a non-null
    private String text;

    private Color color;




}
