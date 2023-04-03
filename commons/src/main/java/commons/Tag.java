package commons;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
public class Tag implements DBEntity{
    private String text;
    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "tags_seq", sequenceName = "TAGS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
    protected int id;
    private String color;

    public Tag(String text, int id, String color) {
        this.text = text;
        this.id = id;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public long getId(){
        return (long) id;
    }
}
