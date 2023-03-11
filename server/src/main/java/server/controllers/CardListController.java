package server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    private CardListRepository cardListRepository;

    public CardListController(CardListRepository cardListRepository) {
        this.cardListRepository = cardListRepository;
    }

    @RequestMapping(path = { "", "/" })
    public List<CardList> getAllLists() {
        return cardListRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardListRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardListRepository.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<CardList> add(@RequestBody CardList cardList) {

        if (cardList.getTitle() == null || cardList.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CardList card = cardListRepository.save(cardList);
        return ResponseEntity.ok(card);
    }
}
