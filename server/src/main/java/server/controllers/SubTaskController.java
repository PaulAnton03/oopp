package server.controllers;

import commons.Card;
import commons.SubTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.CardRepository;
import server.database.SubTaskRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subtasks")
public class SubTaskController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SubTaskRepository subTaskRepository;
    private final CardRepository cardRepository;

    public SubTaskController(SimpMessagingTemplate messagingTemplate, SubTaskRepository subTaskRepository,
                             CardRepository cardRepository) {
        this.messagingTemplate = messagingTemplate;
        this.subTaskRepository = subTaskRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<SubTask>> getAll() {
        return ResponseEntity.ok(subTaskRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTask> getById(@PathVariable("id") long id) {
        final Optional<SubTask> subTask = subTaskRepository.findById(id);
        if (subTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask with id " + id + " not found");
        }
        return ResponseEntity.ok(subTask.get());
    }

    /**
     * Creates a new subtask and assigns it to a specified card
     *
     * @param subTask  the subtask data
     * @param cardId   the id of the card for this subtask
     * @param position optional position of this subtask within the card
     * @return the saved subtask
     */
    @PostMapping("/create")
    public ResponseEntity<SubTask> create(@RequestBody SubTask subTask, @RequestParam long cardId, @RequestParam Optional<Integer> position) {
        if (!subTask.isNetworkValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subtask data is not valid");
        }

        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card with id " + cardId + " not found");
        }

        if (position.isPresent() && (position.get() < 0 || position.get() > card.get().getSubtasks().size())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position " + position + " is invalid");
        }

        subTask.setCard(card.get());
        position.ifPresentOrElse(
                pos -> card.get().getSubtasks().add(pos, subTask),
                () -> card.get().getSubtasks().add(subTask));
        subTask.setFinished(false);
        subTaskRepository.save(subTask);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/board/" + subTask.getCard().getCardList().getBoard().getId() + "/card/" + subTask.getCard().getId() + "/subtasks/create", subTask);
        return ResponseEntity.ok(subTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SubTask> delete(@PathVariable("id") long id) {
        final Optional<SubTask> optSubTask = subTaskRepository.findById(id);
        if (optSubTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask with id " + id + " not found");
        }
        SubTask subTask = optSubTask.get();
        if (subTask.getCard() != null) {
            subTask.getCard().removeSubTask(subTask.getId());
        }
        subTaskRepository.deleteById(subTask.getId());
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/board/" + subTask.getCard().getCardList().getBoard().getId() + "/card/" + subTask.getCard().getId() + "/subtasks/delete", subTask);
        return ResponseEntity.ok(subTask);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubTask> update(@RequestBody SubTask subTask) {
        if (!subTask.isNetworkValid()) {
            return ResponseEntity.badRequest().build();
        }
        final Optional<SubTask> optionalSubTask = subTaskRepository.findById(subTask.getId());
        if (optionalSubTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        SubTask updated = subTaskRepository.save(subTask);
        return ResponseEntity.ok(updated);
    }
}
