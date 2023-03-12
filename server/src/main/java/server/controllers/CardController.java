package server.api;

import commons.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final Random random;
    private final CardRepository repo;

    public CardController(Random random, CardRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Card> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Card> add(@RequestBody Card card) {

        if (card.getCardList() == null || isNullOrEmpty(card.getTitle()) || isNullOrEmpty(card.getDescription())) {
            return ResponseEntity.badRequest().build();
        }

        Card saved = repo.save(card);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("rnd")
    public ResponseEntity<Card> getRandom() {
        var cards = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(cards.get(idx));
    }
}
