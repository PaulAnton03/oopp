package client.utils;

import client.components.BoardCtrl;
import commons.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ComponentFactoryTest {
    @Mock
    private ClientUtils client;
    @InjectMocks
    private ComponentFactory factory;

    @Test
    public void createComponentTest() {
        Board board = new Board("test");

        assertEquals(board, factory.create(BoardCtrl.class, board).getBoard());
        assertNotNull(factory.create(BoardCtrl.class, new Board()).getNode());
    }
}
