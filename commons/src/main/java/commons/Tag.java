package commons;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import javax.persistence.*;
import java.util.*;


@Data
@Entity
@RequiredArgsConstructor
@JsonIdentityInfo(scope = Tag.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "tags")
public class Tag implements DBEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Getter
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ManyToMany(targetEntity = commons.Card.class, mappedBy = "tags",
            cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ElementCollection
    private Set<Card> cards = new HashSet<>();


    @ManyToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Board board;

    private String text;

    private String color = "ffffffff";

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

    @JsonIgnore
    public static boolean isNullOrEmpty(String s){
        return s == null || s.isEmpty();
    }

    @JsonIgnore
    public boolean isNetworkValid() {
        return this.board != null
                && !isNullOrEmpty(this.getText())
                && !isNullOrEmpty(this.getColor());
    }
}
