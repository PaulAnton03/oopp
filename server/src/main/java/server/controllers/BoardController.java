package server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Board;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;
    private final CardRepository cardRepository;

    public BoardController(BoardRepository boardRepository, CardListRepository cardListRepository, CardRepository cardRepository) {
        this.boardRepository = boardRepository;
        this.cardListRepository = cardListRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @GetMapping("/name/{name}")
    public Board getByName(@PathVariable("name") String name) {
        return boardRepository.findByName(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        final Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        if (!board.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }

        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Board> delete(@PathVariable("id") long id) {
        final Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        board.ifPresent(b -> {boardRepository.deleteDownProp(b, cardListRepository, cardRepository);});
        return ResponseEntity.ok(board.get());
    }
}
