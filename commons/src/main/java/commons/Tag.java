package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@RequiredArgsConstructor
public class Tag implements DBEntity {


    @Id
    @SequenceGenerator(name = "tags_seq", sequenceName = "TAGS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
    protected long id;

    @Getter
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private Set<Card> cards = new HashSet<>();

    private String text;

    @Getter
    @Setter
    private int red;
    @Getter
    @Setter
    private int blue;
    @Getter
    @Setter
    private int green;

}
