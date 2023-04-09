package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


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
    @EqualsAndHashCode.Exclude
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH,
                    CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(name = "tags_cards_tags",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "card_tag_id"))
    private Set<CardTag> cardTags = new HashSet<>();


    @ManyToOne
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
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @JsonIgnore
    public boolean isNetworkValid() {
        System.out.println("Bagli olan board" + this.board.getName());
        System.out.println("Texti" + this.getText());
        System.out.println("Color" + this.getColor());
        return this.board != null
                && !isNullOrEmpty(this.getText())
                && !isNullOrEmpty(this.getColor());
    }
}
