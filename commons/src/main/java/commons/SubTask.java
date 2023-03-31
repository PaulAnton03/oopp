package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "subtasks")
@JsonIdentityInfo(scope = SubTask.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SubTask implements DBEntity {

    @Id
    @SequenceGenerator(name = "subtasks_seq", sequenceName = "SUBTASKS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subtasks_seq")
    protected long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    @JsonIgnore
    private Card card;

    @NonNull
    private String title;

    @NonNull
    private Boolean finished;

    @Override
    public String toString() {
        return "Subtask [id=" + id + ", title=" + title + ", finished=" + finished + "]";
    }

    /**
     * @return Is {@link SubTask} valid for network transfer
     */
    @JsonIgnore
    public boolean isNetworkValid() {
        return !isNullOrEmpty(this.getTitle());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}