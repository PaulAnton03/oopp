package commons;

public class Tag {
    String text;
    int id;
    String color;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
