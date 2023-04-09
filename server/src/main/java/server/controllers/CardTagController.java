package server.controllers;

import commons.Card;
import commons.CardTag;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.CardTagRepository;
import server.database.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/cardtags")
public class CardTagController {

    @Autowired
    private CardTagRepository cardTagRepository;

    public CardTagController(CardTagRepository cardTagRepository,
                             TagRepository tagRepository, CardRepository cardRepository) {
        this.cardTagRepository = cardTagRepository;
        this.tagRepository = tagRepository;
        this.cardRepository = cardRepository;
    }

    private final TagRepository tagRepository;
    private final CardRepository cardRepository;

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<CardTag>> getAll() {
        return ResponseEntity.ok(cardTagRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardTag> getCardTag(@PathVariable(value = "id") Long cardTagId) {
        CardTag cardTag = cardTagRepository.findById(cardTagId)
                .orElseThrow(() -> new RuntimeException("CardTag not found with id: " + cardTagId));
        return ResponseEntity.ok().body(cardTag);
    }

    @PostMapping("/create")
    public CardTag createCardTag(@RequestBody CardTag cardTag) {
        Card card = cardTag.getCard();
        System.out.println("to be combined" + card.getTitle());
        Tag tag = cardTag.getTag();
        System.out.println("to be combined" + tag.getText());
        return cardTagRepository.save(cardTag);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CardTag> updateCardTag(@PathVariable(value = "id") Long cardTagId,
                                                 @RequestBody CardTag cardTagDetails) {
        CardTag cardTag = cardTagRepository.findById(cardTagId)
                .orElseThrow(() -> new
                        RuntimeException("CardTag not found with id: " + cardTagId));
        cardTag.setCard(cardTagDetails.getCard());
        cardTag.setTag(cardTagDetails.getTag());
        final CardTag updatedCardTag = cardTagRepository.save(cardTag);
        return ResponseEntity.ok(updatedCardTag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CardTag> deleteCardTag(@PathVariable(value = "id") Long cardTagId) {
        CardTag cardTag = cardTagRepository.findById(cardTagId)
                .orElseThrow(() -> new RuntimeException("CardTag not found with id: " + cardTagId));

        cardTagRepository.delete(cardTag);
        return ResponseEntity.ok(cardTag);
    }

}
