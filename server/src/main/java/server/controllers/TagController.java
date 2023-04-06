package server.controllers;

import commons.Board;
import commons.Tag;
import commons.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    private final BoardRepository boardRepository;

    public TagController(TagRepository tagRepository, CardRepository cardRepository, BoardRepository boardRepository){
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
        this.boardRepository = boardRepository;
    }

    @GetMapping(path = { "", "/"})
    public ResponseEntity<List<Tag>> getAll(){
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        final Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "tag with id: "+ id
            + " not found");
        }
        return ResponseEntity.ok(tag.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Tag> create(@RequestBody Tag tag, @RequestParam long boardId){
        Board board = boardRepository.getById(boardId);
        if(!board.getTagList().contains(tag)){
            board.addTag(tag);
        } else{
            System.out.println("This tag already exists in board!");
        }
       // tagRepository.save(tag); //Cascaded so no need
        tag.setBoard(board);

        tagRepository.save(tag); //hmm but maybe we still need it
        System.out.println("Created tag! : " + tag.getId());
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tag> delete(@PathVariable("id") long id){
        final Optional<Tag> optTag = tagRepository.findById(id);
        if(optTag.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Tag not found");
        }
        Tag repoTag = optTag.get();
//        for(Card  c : repoTag.getCards()){
//            c.getTags().remove(repoTag);
//        }
//        Board board = boardRepository.getById(repoTag.getBoard().getId());
//        if(board.getTagList().contains(repoTag)){
//            board.getTagList().remove(repoTag);
//        }
        if(repoTag.getBoard() != null){
            repoTag.getBoard().removeTag(repoTag.getId());
        }
        tagRepository.deleteById(repoTag.getId());
        System.out.println("Deleted tag!"+ id);
        //todo messagingTemplate?
        return ResponseEntity.ok(repoTag);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> update(@RequestBody Tag tag, @PathVariable("id") long id, @RequestParam long boardId) {
        if(!tag.isNetworkValid()){
            System.out.println("tag is not network valid!");
            return ResponseEntity.badRequest().build();
        }
        final Optional<Tag> optTag = tagRepository.findById(tag.getId());
        if(optTag.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Board board = boardRepository.findById(boardId).get();
        tag.setBoard(board);
        tagRepository.save(tag);
        //boardRepository.save(tag.getBoard()); //by saving board
        //we save  things from topdown
        return ResponseEntity.ok(optTag.get());
    }
}


