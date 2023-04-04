package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "board_themes")
public class BoardTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Board board;

    @Column(name = "board_color")
    private String boardColor = "#ffffffff";
    @Column(name = "list_color")
    private String listColor = "#b2b2ebff";
    @Column(name = "card_color")
    private String cardColor = "#ffffffff";

    @Column(name = "board_font")
    @Getter(value = AccessLevel.NONE)
    private String boardFont;
    @Column(name = "list_font")
    @Getter(value = AccessLevel.NONE)
    private String listFont;
    @Column(name = "card_font")
    @Getter(value = AccessLevel.NONE)
    private String cardFont;

    @JsonIgnore
    public String toString() {
        return "BoardTheme{" +
                "boardColor='" + boardColor + '\'' +
                ", listColor='" + listColor + '\'' +
                ", cardColor='" + cardColor + '\'' +
                ", boardFont='" + boardFont + '\'' +
                ", listFont='" + listFont + '\'' +
                ", cardFont='" + cardFont + '\'' +
                '}';
    }
}
