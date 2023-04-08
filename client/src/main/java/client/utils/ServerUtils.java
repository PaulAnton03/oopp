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

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    @Setter
    @Getter
    private String serverPath = "localhost:8080";

    @Getter
    @Setter
    private boolean admin = false;

    private WebSocketStompClient stomp = null;

    @Getter
    private StompSession session = null;

    public void connect() {
        var client = new StandardWebSocketClient();
        if (session != null) {
            stomp.stop();
            stomp = null;
            session = null;
        }
        stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            session = stomp.connect("ws://" + serverPath + "/websocket", new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    public List<Board> longPollBoards() throws InterruptedException, ExecutionException {
        var target = ClientBuilder.newClient(new ClientConfig())
                .target("http://" + serverPath + "/boards");
        var invocation = target.request(APPLICATION_JSON).buildGet();
        var type = new GenericType<List<Board>>() {
        };
        return invocation.submit(type).get();
    }


    private WebTarget webTargetFromPath(String path) {
        return ClientBuilder.newClient(new ClientConfig())
                .target("http://" + serverPath).path(path);

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

    public Board updateBoard(Board board) {
        WebTarget webTarget = webTargetFromPath("/boards/update/{id}").resolveTemplate("id", board.getId());
        return webTargetAddDefault(webTarget).put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public Board getBoard(long id) {
        WebTarget webTarget = webTargetFromPath("/boards/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>() {
        });
    }

    public Board getBoard(String name) {
        WebTarget webTarget = webTargetFromPath("/boards/name/{name}").resolveTemplate("name", name);
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
        WebTarget webTarget = webTargetFromPath("/cards/create")
                .queryParam("cardListId", card.getCardList().getId());
        return webTargetAddDefault(webTarget).post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card updateCard(Card card) {
        WebTarget webTarget = webTargetFromPath("/cards/update/{id}").resolveTemplate("id", card.getId());
        return webTargetAddDefault(webTarget).put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public Card addCardAtPosition(Card card, int position) {
        WebTarget webTarget = webTargetFromPath("/cards/create")
                .queryParam("cardListId", card.getCardList().getId())
                .queryParam("position", position);
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
        WebTarget webTarget = webTargetFromPath("/lists/create")
                .queryParam("boardId", cardList.getBoard().getId());
        return webTargetAddDefault(webTarget).post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public CardList updateCardList(CardList cardList) {
        WebTarget webTarget = webTargetFromPath("/lists/update/{id}").resolveTemplate("id", cardList.getId());
        return webTargetAddDefault(webTarget).put(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public CardList deleteCardList(long id) {
        WebTarget webTarget = webTargetFromPath("/lists/delete/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).delete(new GenericType<>() {
        });
    }

    public List<Tag> getTagList() {
        WebTarget webTarget = webTargetFromPath("/tags");
        return webTargetAddDefault(webTarget).get(new GenericType<>(){});
    }
    public Tag getTag(long id) {
        WebTarget webTarget = webTargetFromPath("/tags/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>(){});
    }

    public Tag updateTag(Tag tag, long board_id){
        WebTarget webTarget = webTargetFromPath("/tags/update/{id}").resolveTemplate("id", tag.getId())
                .queryParam("boardId", board_id);
        return webTargetAddDefault(webTarget).put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    public Tag createTag(Tag tag, long board_id){
        WebTarget webTarget = webTargetFromPath("/tags/create")
                .queryParam("boardId", board_id);
        return webTargetAddDefault(webTarget).post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    public Tag deleteTag(long id){
        WebTarget webTarget = webTargetFromPath("/tags/delete/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).delete(new GenericType<>(){});
    }

    public CardTag getCardTag(long id){
        WebTarget webTarget = webTargetFromPath("/cardtags/{id}").resolveTemplate("id", id);
        return webTargetAddDefault(webTarget).get(new GenericType<>(){});
    }
    public CardTag createCardTag(CardTag cardTag) {
        WebTarget webTarget = webTargetFromPath("/cardtags/create").resolveTemplate("id", cardTag.getId());
        return webTargetAddDefault(webTarget).post(Entity.entity(cardTag, APPLICATION_JSON), CardTag.class);
    }

    public void deleteCardTag()
        public void updateCardTag()

    // For TESTING purpose
    public Board getBoardTest() {
        Board board = new Board("Testing board");
        CardList list1 = new CardList("List 1");
        CardList list2 = new CardList("List 2");
        list1.addCard(new Card("Do the dishes", "In kitchen"));
        list1.addCard(new Card("Do the homework", "Maths, Biology"));
        list2.addCard(new Card("Title", "Only card in list 2"));
        return board;
    }

    public void stop() {
        if (session != null) {
            stomp.stop();
            stomp = null;
            session = null;
        }
    }


}
