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

import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    @Setter
    @Getter
    private static String serverPath = "http://localhost:8080/";

    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
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

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/boards/create") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public CardList addCardList(CardList cardList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    public List<CardList> getCardLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverPath).path("/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
}