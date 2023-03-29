package commons;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.awt.*;
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

    @ManyToMany(mappedBy = "tagList", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    private String text;

    private Color color;


}
