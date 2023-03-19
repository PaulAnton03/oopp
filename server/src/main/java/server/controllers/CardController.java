package server.controllers;

import commons.Card;
import commons.CardList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    public CardController(CardRepository cardRepository, CardListRepository cardListRepository) {
        this.cardRepository = cardRepository;
        this.cardListRepository = cardListRepository;
    }

    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Card>> getAll() {
        return ResponseEntity.ok(cardRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        final Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Card> create(@RequestBody Card card, @RequestParam long cardListId) {
        System.out.println(card);
        if (!card.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Card data valid");

        Optional<CardList> cardList = cardListRepository.findById(cardListId);
        if (cardList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        card.setCardList(cardList.get());
        cardRepository.save(card);
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> delete(@PathVariable("id") long id) {
        final Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cardRepository.deleteById(id);
        return ResponseEntity.ok(card.get());
    }
}
