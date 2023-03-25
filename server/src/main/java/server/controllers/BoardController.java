package server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(boardRepository.findAll());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Board> getByName(@PathVariable("name") String name) {
        final Optional<Board> board = boardRepository.findByName(name);
        if (board.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board.get());
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

        if (boardRepository.findByName(board.getName()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Board> delete(@PathVariable("id") long id) {
        final Optional<Board> optBoard = boardRepository.findById(id);
        if (optBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final Board board = optBoard.get();
        boardRepository.deleteDownProp(board, cardListRepository, cardRepository);
        return ResponseEntity.ok(board);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Board> update(@RequestBody Board board) {
        final Optional<Board> optionalBoard = boardRepository.findById(board.getId());
        if(optionalBoard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Board updated = boardRepository.save(board);
        return ResponseEntity.ok(updated);
    }
}
