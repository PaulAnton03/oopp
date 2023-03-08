package commons;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    private List<Card> cardList;
    private String title;

    /**
     * Constructor for a card list without a title
     */
    public CardList() {
        this.cardList = new ArrayList<>();
        this.title = "New Card List";
    }
    /**
     * Constructor for a card list with a given title
     * @param title title of the card list
     */
    public CardList(String title) {
        this.title = title;
        this.cardList = new ArrayList<>();
    }

    public void removeCard(Card card){
        this.cardList.remove(card);
    }

    public void addCard(Card card){
        this.cardList.add(card);
    }
}
