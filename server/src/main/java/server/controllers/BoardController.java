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

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping(path = { "", "/" })
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

        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!boardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        boardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
