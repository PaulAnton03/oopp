package server.controllers;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

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

    @PostMapping("/create")
    public ResponseEntity<Board> create(@RequestBody Board board) {
        System.out.println(board);
        if (board.getName() == null || board.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Board boardSaved = boardRepository.save(board);
        return ResponseEntity.ok(boardSaved);
    }

    @GetMapping("/delete")
    public ResponseEntity<Board> delete() {
        boardRepository.deleteAll();
        return ResponseEntity.ok().build();
    }
}
