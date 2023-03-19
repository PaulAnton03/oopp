package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * Delete a {@link Card} without propagating changes up to {@link CardList}
     * @param card
     */
    default void deleteDownProp(Card card) {
        this.deleteById(card.getId());
    }
}
