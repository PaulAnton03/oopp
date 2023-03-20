package server.controllers;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {
    @Mock
    private CardRepository cardRepoMock;
    @Mock
    private CardListRepository cardListRepoMock;
    @InjectMocks
    private CardController controller;

    @Test
    public void getAllCardsTest() {
        List<Card> cardCollection = new ArrayList<>();
        cardCollection.add(new Card("1st task", "description"));
        cardCollection.add(new Card("2nd task", "description"));

        when(cardRepoMock.findAll()).thenReturn(cardCollection);

        assertEquals(cardCollection, controller.getAll().getBody());
    }

    @Test
    public void getCardTest() {
        final Card card = new Card("1st task", "description");

        when(cardRepoMock.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepoMock.findById(not(eq(1L)))).thenReturn(Optional.empty());

        assertEquals(card, controller.getById(1L).getBody());
        assertNull(controller.getById(0L).getBody());
    }

    @Test
    public void createCardTest() {
        final CardList list = new CardList("list");
        list.setId(10L);
        final Card suppliedCard = new Card("supply", "description");
        final Card savedCard = new Card("saved", "description");
        savedCard.setCardList(list);

        when(cardListRepoMock.findById(10L)).thenReturn(Optional.of(list));
        doAnswer(invocation -> {
            Card arg = (Card) invocation.getArguments()[0];

            assertEquals(list, arg.getCardList());
            arg.setTitle("saved");
            return null;
        }).when(cardRepoMock).save(any());

        var response = controller.create(suppliedCard, 10L);

        assertEquals(savedCard, response.getBody());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void createCardInvalidTest() {
        final long invalidCardListId = 10L;
        final long validCardListId = 20L;
        final Card invalidCard = new Card();
        final Card validCard = new Card("title", "description");

        when(cardListRepoMock.findById(invalidCardListId)).thenReturn(Optional.empty());
        when(cardListRepoMock.findById(validCardListId)).thenReturn(Optional.of(new CardList()));

        assertEquals(HttpStatus.BAD_REQUEST, controller.create(invalidCard, validCardListId).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, controller.create(validCard, invalidCardListId).getStatusCode());
    }
}
