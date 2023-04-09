package server.controllers;

import commons.Card;
import commons.CardList;
import commons.CardTag;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    private final TagRepository tagRepository;

    private final SubTaskRepository subTaskRepository;

    private final CardTagRepository cardTagRepository;

    public CardController(SimpMessagingTemplate messagingTemplate, CardRepository cardRepository,
                          CardListRepository cardListRepository, SubTaskRepository subTaskRepository
            , TagRepository tagRepository, CardTagRepository cardTagRepository) {
        this.messagingTemplate = messagingTemplate;
        this.cardRepository = cardRepository;
        this.cardListRepository = cardListRepository;
        this.subTaskRepository = subTaskRepository;
        this.tagRepository = tagRepository;
        this.cardTagRepository = cardTagRepository;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Card>> getAll() {
        return ResponseEntity.ok(cardRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        final Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card with id " + id + " not found");
        }
        return ResponseEntity.ok(card.get());
    }

    /**
     * Creates a new card and assigns it to a specified card list
     *
     * @param card       the card data
     * @param cardListId the id of the list for this card
     * @param position   optional position of this card within the list
     * @return the saved card
     */
    @PostMapping("/create")
    public ResponseEntity<Card> create(@RequestBody Card card, @RequestParam long cardListId, @RequestParam Optional<Integer> position) {
        if (!card.isNetworkValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card data is not valid");
        }

        Optional<CardList> cardList = cardListRepository.findById(cardListId);
        if (cardList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card list with id " + cardListId + " not found");
        }

        if (position.isPresent() && (position.get() < 0 || position.get() > cardList.get().getCards().size())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position " + position + " is invalid");
        }

        card.setCardList(cardList.get());
        position.ifPresentOrElse(
                pos -> cardList.get().getCards().add(pos, card),
                () -> cardList.get().getCards().add(card));
        cardRepository.save(card);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/board/" + card.getCardList().getBoard().getId() + "/cards", card);
        return ResponseEntity.ok(card);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> delete(@PathVariable("id") long id) {
        final Optional<Card> optCard = cardRepository.findById(id);
        if (optCard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card with id " + id + " not found");
        }
        Card card = optCard.get();
        if (card.getCardList() != null) {
            card.getCardList().removeCard(card.getId());
        }
        cardRepository.deleteById(card.getId());
        messagingTemplate.convertAndSend("/topic/board/" + card.getCardList().getBoard().getId() + "/cards", card);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Card> update(@RequestBody Card card, @PathVariable("id") long id) {
        if (!card.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }
        final Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Card updated = cardRepository.save(card);
        CardList cardList = cardListRepository.getById(card.getCardList().getId());
        messagingTemplate.convertAndSend("/topic/board/" + cardList.getBoard().getId() + "/cards", updated);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/addTag/{id}")
    public ResponseEntity<Card> addTag(@RequestBody Card card, @PathVariable("id") long id) {
        final Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isEmpty()) {
            //  return ResponseEntity.notFound().build();
        }
        Optional<Tag> optionalTag = tagRepository.findById(id);
        CardTag cardTag = new CardTag(card, optionalTag.get());
        card.getCardTags().add(cardTag);
        Card updated = cardRepository.save(card);
        return ResponseEntity.ok(updated);
    }

}
