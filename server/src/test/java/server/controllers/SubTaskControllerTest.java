package server.controllers;

import commons.SubTask;
import commons.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.server.ResponseStatusException;
import server.database.SubTaskRepository;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubTaskControllerTest {
    @Mock
    private CardRepository cardRepoMock;
    @Mock
    private SubTaskRepository subTaskRepoMock;
    @InjectMocks
    private SubTaskController controller;

    @Test
    public void getAllSubTasksTest() {
        List<SubTask> subTaskCollection = new ArrayList<>();
        subTaskCollection.add(new SubTask("1st subtask", true));
        subTaskCollection.add(new SubTask("2nd subtask", false));

        when(subTaskRepoMock.findAll()).thenReturn(subTaskCollection);

        assertEquals(subTaskCollection, controller.getAll().getBody());
    }

    @Test
    public void getSubTaskTest() {
        final SubTask subTask = new SubTask("1st subtask", true);

        when(subTaskRepoMock.findById(1L)).thenReturn(Optional.of(subTask));
        when(subTaskRepoMock.findById(not(eq(1L)))).thenReturn(Optional.empty());

        assertEquals(subTask, controller.getById(1L).getBody());
        assertThrows(ResponseStatusException.class, () -> controller.getById(0L));
    }

    @Test
    public void createSubTaskTest() {
        final Card card = new Card("card", "");
        card.setId(10L);
        final SubTask suppliedSubTask = new SubTask("supply", false);
        final SubTask savedSubTask = new SubTask("saved", false);
        savedSubTask.setCard(card);

        when(cardRepoMock.findById(10L)).thenReturn(Optional.of(card));
        doAnswer(invocation -> {
            SubTask arg = (SubTask) invocation.getArguments()[0];

            assertEquals(card, arg.getCard());
            arg.setTitle("saved");
            return null;
        }).when(subTaskRepoMock).save(any());

        var response = controller.create(suppliedSubTask, 10L, Optional.empty());

        assertEquals(savedSubTask, response.getBody());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void createSubTaskInvalidTest() {
        final long invalidCardId = 10L;
        final long validCardId = 20L;
        final SubTask invalidSubTask = new SubTask();
        final SubTask validSubTask = new SubTask("title", false);

        when(cardRepoMock.findById(invalidCardId)).thenReturn(Optional.empty());
        when(cardRepoMock.findById(validCardId)).thenReturn(Optional.of(new Card()));

        assertThrows(ResponseStatusException.class, () -> controller.create(invalidSubTask, validCardId, Optional.empty()));
        assertThrows(ResponseStatusException.class, () -> controller.create(validSubTask, invalidCardId, Optional.empty()));
        assertThrows(ResponseStatusException.class, () -> controller.create(validSubTask, validCardId, Optional.of(-1)));
    }
}
