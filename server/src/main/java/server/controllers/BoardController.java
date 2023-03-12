package server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Board;
import server.database.BoardRepository;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
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
        if (id < 0 || !boardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardRepository.findById(id).get());
    }

    @GetMapping("/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        if (board.getName() == null || board.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }
}
