package server.controllers;

import commons.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.opentest4j.TestAbortedException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
    @Mock
    BoardRepository boardRepoMock;
    @Mock
    SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    BoardController controller;

    @Test
    public void getAllBoardsTest() {
        List<Board> boardList = new ArrayList<>();
        boardList.add(new Board("board 1"));
        boardList.add(new Board("board 2"));

        when(boardRepoMock.findAll()).thenReturn(boardList);

        assertEquals(boardList, controller.getAll().getBody());
    }

    @Test
    public void getBoardTest() {
        final Board board = new Board("name");

        when(boardRepoMock.findById(10L)).thenReturn(Optional.of(board));
        when(boardRepoMock.findById(not(eq(10L)))).thenReturn(Optional.empty());

        assertEquals(board, controller.getById(10L).getBody());
        assertThrows(ResponseStatusException.class, () -> controller.getById(10000L));
    }

    @Test
    public void getBoardByNameTest() {
        final Board board = new Board("name");

        when(boardRepoMock.findByName("name")).thenReturn(Optional.of(board));
        when(boardRepoMock.findByName(not(eq("name")))).thenReturn(Optional.empty());

        assertEquals(board, controller.getByName("name").getBody());
        assertThrows(ResponseStatusException.class, () -> controller.getByName("invalid"));
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void createBoardTest() {
        final Board suppliedBoard = new Board("supplied");
        suppliedBoard.setId(15L);
        final Board savedBoard = new Board("saved");
        savedBoard.setId(15L);

        when(boardRepoMock.save(suppliedBoard)).thenReturn(savedBoard);
        doNothing().when(messagingTemplate).convertAndSend(any(String.class), any(Board.class));

        Board ret = controller.create(suppliedBoard).getBody();

        assertEquals(savedBoard, ret);

        verify(messagingTemplate, times(1)).convertAndSend("/topic/board/15/create", suppliedBoard);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void createBoardInvalidTest() {
        final Board invalidBoard = new Board();

        when(boardRepoMock.save(any())).thenThrow(new TestAbortedException("Method shouldn't save"));

        assertThrows(ResponseStatusException.class, () -> controller.create(invalidBoard));
    }

}
