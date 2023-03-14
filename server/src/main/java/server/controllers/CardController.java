package server.controllers;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardRepository cardRepository;

    public CardController(CardRepository repo) {
        this.cardRepository = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (!cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardRepository.findById(id).get());
    }

    @PostMapping("/create")
    public ResponseEntity<Card> create(@RequestBody Card card) {
        if (!card.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        Card saved = cardRepository.save(card);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!cardRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        cardRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
