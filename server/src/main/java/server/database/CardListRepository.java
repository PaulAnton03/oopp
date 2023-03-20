package server.database;

import commons.Card;
import commons.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Long>{
    /**
     * Delete a {@link CardList cardList}, only propagating changes down to {@link Card}s
     * @param cardList
     * @param cardRepository
     */
    default void deleteDownProp(CardList cardList, CardRepository cardRepository) {
        if (cardList.getCardList() != null) {
            for (Card card: cardList.getCardList()) {
                cardRepository.deleteDownProp(card);
            }
        }
        this.deleteById(cardList.getId());
    }
}
