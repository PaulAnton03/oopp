package server.controllers;

import commons.Tag;
import commons.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    public TagController(TagRepository tagRepository, CardRepository cardRepository){
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
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
    public ResponseEntity<Tag> create(@RequestBody Tag tag){
//        Optional<Card> card = cardRepository.findById(cardId);
//        if(card.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card with given Id not found");
//        }
//        System.out.println("I reached here!");
//        tag.getCards().add(card.get());
//        card.get().getTags().add(tag);
        tagRepository.save(tag);
        System.out.println("Yo before return");
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tag> delete(@PathVariable("id") long id){
        final Optional<Tag> optTag = tagRepository.findById(id);
        if(optTag.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        Tag tag = optTag.get();
        for(Card  c : tag.getCards()){
            c.getTags().remove(tag);
        }
        tagRepository.deleteById(tag.getId());
        return ResponseEntity.ok(tag);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        final Optional<Tag> optTag = tagRepository.findById(tag.getId());
        if(optTag.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Tag updated = tagRepository.save(tag);
        return ResponseEntity.ok(updated);
    }
}


