package server.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @GetMapping("/name/{name}")
    public Board getBoardByName(@PathVariable("name") String name) {
        return boardRepository.findByName(name);
    }

    @GetMapping("/find/{id}")
    public Board getBoardByName(@PathVariable("id") long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @GetMapping("/create/{name}")
    public Board createBoard(@PathVariable("name") String name) {
        Board board = new Board(name);
        boardRepository.save(board);
        return board;
    }
}
