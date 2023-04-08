package server.controllers;

import commons.CardTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardTagRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cardtags")
public class CardTagController {

    @Autowired
    private CardTagRepository cardTagRepository;

    @GetMapping("/{id}")
    public ResponseEntity<CardTag> getCardTag(@PathVariable(value = "id") Long cardTagId) {
        CardTag cardTag = cardTagRepository.findById(cardTagId)
                .orElseThrow(() -> new RuntimeException("CardTag not found with id: " + cardTagId));
        return ResponseEntity.ok().body(cardTag);
    }

    @PostMapping("/create")
    public CardTag createCardTag(@RequestBody CardTag cardTag) {
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
    public Map<String, Boolean> deleteCardTag(@PathVariable(value = "id") Long cardTagId) {
        CardTag cardTag = cardTagRepository.findById(cardTagId)
                .orElseThrow(() -> new RuntimeException("CardTag not found with id: " + cardTagId));
        cardTagRepository.delete(cardTag);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
