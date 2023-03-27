package server.controllers;
import commons.Board;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<CardList>> getAllLists() {
        var list = cardListRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable long id) {
        final Optional<CardList> cardList = cardListRepository.findById(id);
        if (cardList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card list with id " + id + " not found");
        }
        return ResponseEntity.ok(cardList.get());
    }

    @PostMapping("/create")
    public ResponseEntity<CardList> create(@RequestBody CardList cardList, @RequestParam long boardId) {
        if (!cardList.isNetworkValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card list data is invalid");
        }

        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board with id " + boardId + " not found");
        }

        cardList.setBoard(board.get());
        board.get().getCardLists().add(cardList);
        cardListRepository.save(cardList);
        return ResponseEntity.ok(cardList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CardList> delete(@PathVariable("id") long id) {
        final Optional<CardList> optCardList = cardListRepository.findById(id);
        if (optCardList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card list with id " + id + " not found");
        }
        final CardList cardList = optCardList.get();
        if (cardList.getBoard() != null) {
            cardList.getBoard().removeCardList(cardList.getId());
        }
        cardListRepository.deleteById(cardList.getId());
        return ResponseEntity.ok(cardList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CardList> update(@RequestBody CardList cardList) {
        if(!cardList.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }
        final Optional<CardList> optionalCardList = cardListRepository.findById(cardList.getId());
        if(optionalCardList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CardList updated = cardListRepository.save(cardList);
        return ResponseEntity.ok(updated);
    }

    @MessageMapping("/lists")
    @SendTo("/topic/lists")
    public CardList addMessage(CardList list) {
        return list;
    }

}
