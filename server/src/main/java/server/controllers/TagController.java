package server.controllers;

import commons.Board;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.CardTagRepository;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final SimpMessagingTemplate messagingTemplate;
    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    private final CardTagRepository cardTagRepository;

    private final BoardRepository boardRepository;

    public TagController(TagRepository tagRepository, CardRepository cardRepository,
                         BoardRepository boardRepository, SimpMessagingTemplate messagingTemplate
        ,CardTagRepository cardTagRepository) {
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
        this.boardRepository = boardRepository;
        this.messagingTemplate = messagingTemplate;
        this.cardTagRepository = cardTagRepository;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Tag>> getAll() {
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        final Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tag with id: " + id
                    + " not found");
        }
        return ResponseEntity.ok(tag.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Tag> create(@RequestBody Tag tag, @RequestParam long boardId) {
        Board board = boardRepository.getById(boardId);
        if (!board.getTagList().contains(tag)) {
            board.addTag(tag);
        } else {
            System.out.println("This tag already exists in board!");
        }
        tag.setBoard(board);
        tagRepository.save(tag);
        System.out.println("Created tag! : " + tag.getId());
        if(messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/board/" +
                    tag.getBoard().getId() + "/tags", tag);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tag> delete(@PathVariable("id") long id) {
        final Optional<Tag> optTag = tagRepository.findById(id);
        if (optTag.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        Tag repoTag = optTag.get();
        messagingTemplate.convertAndSend("/topic/board/" + optTag.get().getBoard().getId()
                + "/tags/delete", repoTag);
        if (repoTag.getBoard() != null) {
            repoTag.getBoard().removeTag(repoTag.getId());
        }
        Board board = repoTag.getBoard();
        System.out.println("Deleted tag!" + id);

        tagRepository.deleteById(repoTag.getId());
        return ResponseEntity.ok(repoTag);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> update(@RequestBody Tag tag, @PathVariable("id") long id, @RequestParam long boardId) {
        Board board = boardRepository.findById(boardId).get();
        tag.setBoard(board);
        if (!tag.isNetworkValid()) {
            System.out.println("tag is not network valid!");
            return ResponseEntity.badRequest().build();
        }
        final Optional<Tag> optTag = tagRepository.findById(tag.getId());
        if (optTag.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        board = boardRepository.findById(boardId).get();
        tag.setBoard(board);
        tagRepository.save(tag);
        messagingTemplate.convertAndSend("/topic/board/" + optTag.get().getBoard().getId()
                + "/tags", tag);
        return ResponseEntity.ok(optTag.get());
    }
}


