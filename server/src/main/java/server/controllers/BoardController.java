package server.controllers;

import commons.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;
import java.util.Optional;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with name " + name + " not found");
        }
        return ResponseEntity.ok(board.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        final Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with id " + id + " not found");
        }
        return ResponseEntity.ok(board.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        if (!board.isNetworkValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board data is invalid");
        }

        if (boardRepository.findByName(board.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board with name " + board.getName() + " already exists");
        }
        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Board> delete(@PathVariable("id") long id) {
        final Optional<Board> optBoard = boardRepository.findById(id);
        if (optBoard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board with id " + id + " not found");
        }
        final Board board = optBoard.get();
        boardRepository.deleteById(board.getId());
        return ResponseEntity.ok(board);
    }
}
