package server.controllers;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import server.database.BoardRepository;
import server.database.CardListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardListControllerTest {
    @Mock
    private CardListRepository cardListRepoMock;
    @Mock
    private BoardRepository boardRepoMock;
    @InjectMocks
    private CardListController controller;

    @Test
    public void getAllListsTest() {
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(new CardList("list 0"));
        cardLists.add(new CardList("list 1"));
        when(cardListRepoMock.findAll()).thenReturn(cardLists);

        assertEquals(cardLists, controller.getAllLists().getBody());
    }

    @Test
    public void getListTest() {
        final long Id = 12;
        final CardList list = new CardList("title");
        when(cardListRepoMock.findById(Id)).thenReturn(Optional.of(list));

        assertEquals(list, controller.getById(Id).getBody());
    }

    @Test
    public void getListNotFoundTest() {
        final long Id = 12;
        when(cardListRepoMock.findById(Id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> controller.getById(Id));
    }

    @Test
    public void createListTest() {
        final Board board = new Board("board");
        board.setId(12);
        final CardList supplyList = new CardList("supply");
        final CardList savedList = new CardList("saved");
        savedList.setBoard(board);

        when(boardRepoMock.findById(board.getId())).thenReturn(Optional.of(board));
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((CardList) args[0]).setTitle("saved");
            return null;
        }).when(cardListRepoMock).save(any());

        var response = controller.create(supplyList, board.getId());

        verify(boardRepoMock).findById(board.getId());
        assertEquals(savedList, response.getBody());
    }

    @Test
    public void createListInvalidDataTest() {
        final Board board = new Board("board");
        board.setId(12);
        final CardList supplyList = new CardList();

        assertThrows(ResponseStatusException.class, () -> controller.create(supplyList, board.getId()));
    }

    @Test
    public void createListInvalidBoardIdTest() {
        final CardList supplyList = new CardList("title");

        when(boardRepoMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> controller.create(supplyList, 10L));
    }
}
