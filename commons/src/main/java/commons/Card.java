package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
@JsonIdentityInfo(scope = Card.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Card implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "subtask_index")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<SubTask> subtasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_list_id", nullable = false)
    @OrderColumn(name = "card_index")
    @JsonIncludeProperties("id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CardList cardList;



    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "cards_cards_tags",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "card_tag_id"))
    private Set<CardTag> cardTags = new HashSet<>();


    @NonNull
    private String title;
    @NonNull
    private String description;


    public boolean removeSubTask(SubTask subTask) {
        return this.subtasks.remove(subTask);
    }


    public boolean removeSubTask(long id) {
        SubTask subTask = this.getSubtasks().stream().filter(s -> s.getId() == id).findFirst().orElse(null);
        if (subTask == null)
            return false;
        return removeSubTask(subTask);
    }

    public void addSubTask(SubTask subTask) {
        this.subtasks.add(subTask);

    }

    @Override
    public String toString() {


        return "Card [id=" + id + ", title=" + title + ", description=" + description +
                getCardList().toCardString() +
                ",subtasks= " + subtasks + "]"
                ;
    }

    /**
     * @return Is {@link Card} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return !isNullOrEmpty(this.getTitle());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


}
