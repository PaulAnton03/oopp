package commons;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Board board;

    private String text;

    private String color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return getId() == tag.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
