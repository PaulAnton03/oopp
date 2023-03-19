package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Board;
import commons.CardList;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByName(String name);

    /**
     * Delete a {@link Board board}, propagating changes down to {@link CardLists}s
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
