/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.glassfish.jersey.client.ClientConfig;

import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    @Setter
    @Getter
    private static String serverPath = "localhost:8080";

    private StompSession session = null;

    public StompSession connect() {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect("ws://" + serverPath + "/websocket", new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    private WebTarget webTargetFromPath(String path) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverPath).path(path);
    }

    private Invocation.Builder webTargetAddDefault(WebTarget webTarget) {
        return webTarget.request(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
    }

    public List<Board> getBoards() {
        WebTarget webTarget = webTargetFromPath("/boards");
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Board addBoard(Board board) {
        WebTarget webTarget = webTargetFromPath("/boards/create");
        return webTargetAddDefault(webTarget).post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public Board getBoard(long id) {
        WebTarget webTarget = webTargetFromPath("/boards/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Board getBoard(String name) {
        WebTarget webTarget = webTargetFromPath("/board/name/{name}").resolveTemplate("name", name);
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Board deleteBoard(long id) {
        WebTarget webTarget = webTargetFromPath("/boards/delete/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).delete(new GenericType<>() {
        });
    }

    public List<Card> getCards() {
        WebTarget webTarget = webTargetFromPath("/cards");
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Card getCard(long id) {
        WebTarget webTarget = webTargetFromPath("/cards/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Card addCard(Card card) {
        WebTarget webTarget = webTargetFromPath("/cards/create?cardListId={cardListId}").resolveTemplate("cardListId", card.getCardListId());
        return webTargetAddDefault(webTarget).post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card deleteCard(long id) {
        WebTarget webTarget = webTargetFromPath("/cards/delete/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).delete(new GenericType<>() {
        });
    }

    public List<CardList> getCardLists() {
        WebTarget webTarget = webTargetFromPath("/lists");
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public CardList getCardList(long id) {
        WebTarget webTarget = webTargetFromPath("/lists/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public CardList addCardList(CardList cardList) {
        WebTarget webTarget = webTargetFromPath("/lists/create?boardId={boardId}").resolveTemplate("boardId", cardList.getBoardId());
        return webTargetAddDefault(webTarget).post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public CardList deleteCardList(long id) {
        WebTarget webTarget = webTargetFromPath("/lists/delete/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).delete(new GenericType<>() {
        });
    }

    // For TESTING purpose
    public Board getBoardTest(long boardId) {
        if (boardId == 1) {

            Board board = new Board("Testing board");
            CardList list1 = new CardList("List 1");
            CardList list2 = new CardList("List 2");
            list1.addCard(new Card("Do the dishes", "In kitchen"));
            list1.addCard(new Card("Do the homework", "Maths, Biology"));
            list2.addCard(new Card("Title", "Only card in list 2"));
            return board;

        } else {

            Board board = new Board("Default empty board");
            return board;
        }
    }
}
