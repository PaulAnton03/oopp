package commons;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Data
@Entity
@RequiredArgsConstructor
public class Tag {

    @Id
    @SequenceGenerator(name = "tags_seq", sequenceName = "TAGS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
    protected long id;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false)
    @OrderColumn(name = "tag_index")
    @JsonIncludeProperties("id")
    @EqualsAndHashCode.Exclude
    private List<Card> cards = new ArrayList<>();

    private String text;

}
