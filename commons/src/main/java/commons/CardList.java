package commons;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    private List<Card> cardList = new ArrayList<>();
    @NonNull
    private String title = "New Card List";


    public void removeCard(Card card){
        this.cardList.remove(card);
    }

    public void addCard(Card card){
        this.cardList.add(card);
    }
}
