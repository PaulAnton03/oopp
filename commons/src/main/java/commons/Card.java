package commons;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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
    private CardList cardList;

    @NonNull
    private String title;
    @NonNull
    private String description;
//
//    @OneToOne(targetEntity = Tag.class)
    //private List<Triple<String, Integer, String>> tagList = new ArrayList<>();
    private String[] cardTagText = new String[10];
    private int[] cardTagId = new int[10];
    private String[] cardTagColor = new String[10];

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
        return "Card [id=" + id + ", title=" + title + ", description=" + description + ",subtasks= " + subtasks + "]";
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
