package commons;

import lombok.Data;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CardList {

    private List<Card> cardList;
    private String title;

    /**
     * Constructor for a card list without a title
     */
    public CardList() {
        this.cardList = new ArrayList<>();
    }
    /**
     * Constructor for a card list with a given title
     * @param title title of the card list
     */
    public CardList(String title) {
        this.title = title;
        this.cardList = new ArrayList<>();
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void removeCard(int index){
        this.cardList.remove(index);
    }

    public void addCard(Card card){
        this.cardList.add(card);
    }
}
