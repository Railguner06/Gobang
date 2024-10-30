package org.example;

import org.example.game.Chessboard;
import org.example.game.UI;

/**
 * 测试类
 */
public class GobangTest {
    public static void main(String[] args){
        Chessboard chessboard = new Chessboard();
        new UI().init(chessboard);
    }
}
