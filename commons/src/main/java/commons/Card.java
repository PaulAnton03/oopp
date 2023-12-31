package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
@JsonIdentityInfo(scope = Card.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Card implements DBEntity {

    @Id
    @SequenceGenerator(name = "cards_seq", sequenceName = "CARDS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_seq")
    protected long id;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "subtask_index")
    @EqualsAndHashCode.Exclude
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
    private List<CardTag> cardTags = new ArrayList<>();


    @NonNull
    private String title;
    @NonNull
    private String description;

    public Card(String title) {
        this.title = title;
        this.description = "";
    }

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
        return "Card [id=" + id + ", title=" + title + ", description=" + description + ",subtasks=" + subtasks + "]";
    }

    /**
     * @return Is {@link Card} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return !StringUtil.isNullOrEmpty(this.getTitle());
    }

    public int findSubTaskById(long id) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == id)
                return i;
        }
        return -1;
    }

    public void swapSubTasks(int index1, int index2) {
        Collections.swap(subtasks, index1, index2);
    }
}
