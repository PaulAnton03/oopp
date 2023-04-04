package commons;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;


import java.util.HashSet;
import java.util.ArrayList;
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



    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ElementCollection
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(targetEntity = commons.Tag.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "cards_tags")
    public Set<Tag> getTags(){
        return tags;
    }


    //Board->Tag OneToMany, Tag -> Board ManyToOne,
    // taglerden cardlara manytomany, hem card dan tage, hem tagden hem carda.

    @NonNull
    private String title;
    @NonNull
    private String description;
//
//    @OneToOne(targetEntity = Tag.class)
    //private List<Triple<String, Integer, String>> tagList = new ArrayList<>();

    public boolean removeTag(long id) {
        Tag tag = this.getTags().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (tag == null) {
            return false;
        } else {
            this.getTags().remove(tag);
        }
        return true;
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
        String tags = this.getTags().toString();

        return "Card [id=" + id + ", title=" + title + ", description=" + description +
                 getCardList().toCardString() +
                ",subtasks= " + subtasks + "]"
                + " Tags: " + tags;
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
