package server.controllers;

import java.util.List;
import java.util.Optional;
import commons.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import commons.CardList;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/lists")
public class CardListController {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CardListController(CardListRepository cardListRepository, BoardRepository boardRepository, CardRepository cardRepository) {
        this.cardListRepository = cardListRepository;
        this.boardRepository = boardRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<CardList>> getAllLists() {
        var list = cardListRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable long id) {
        final Optional<CardList> cardList = cardListRepository.findById(id);
        if (cardList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cardList.get());
    }

    @PostMapping("/create")
    public ResponseEntity<CardList> create(@RequestBody CardList cardList, @RequestParam long boardId) {
        if (!cardList.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        cardList.setBoard(board.get());
        cardListRepository.save(cardList);
        return ResponseEntity.ok(cardList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CardList> delete(@PathVariable("id") long id) {
        final Optional<CardList> cardList = cardListRepository.findById(id);
        if (cardList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cardList.ifPresent(cL -> {
            if (cL.getBoard() != null) {
                cL.getBoard().removeCardList(cL.getId());
            }
            cardListRepository.deleteDownProp(cL, cardRepository);});
        return ResponseEntity.ok(cardList.get());
    }


}
