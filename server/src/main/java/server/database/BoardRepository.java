package server.database;

import commons.Board;
import commons.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByName(String name);

    /**
     * Delete a {@link Board board}, propagating changes down to {@link CardList CardLists}
     * @param board
     * @param cardListRepository
     * @param cardRepository
     */
    default void deleteDownProp(Board board, CardListRepository cardListRepository, CardRepository cardRepository) {
        if (board.getCardLists() != null) {
            for (CardList cardList: board.getCardLists()) {
                cardListRepository.deleteDownProp(cardList, cardRepository);
            }
        }
        this.deleteById(board.getId());
    }
}
