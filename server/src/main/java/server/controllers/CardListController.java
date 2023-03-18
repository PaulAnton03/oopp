package server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.CardList;
import server.database.CardListRepository;

@RestController
@RequestMapping("/list")
public class CardListController {
    private final CardListRepository cardListRepository;

    public CardListController(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<CardList> getAllLists() {
        return cardListRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (!cardListRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardListRepository.findById(id).get());
    }

    @PostMapping("/create")
    public ResponseEntity<CardList> create(@RequestBody CardList cardList) {
        if (cardList.getTitle() == null || cardList.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CardList card = cardListRepository.save(cardList);
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!cardListRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        cardListRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
