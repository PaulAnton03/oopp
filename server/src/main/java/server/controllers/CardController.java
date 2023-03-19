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
    public List<Card> getAll() {
        return cardRepository.findAll();
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
        if (!card.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }

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
        card.ifPresent(c -> {
            if (c.getCardList() != null) {
                c.getCardList().removeCard(c.getId());
            }
            cardRepository.deleteDownProp(c);});
        return ResponseEntity.ok(card.get());
    }
}
