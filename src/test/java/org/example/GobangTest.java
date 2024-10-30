package org.example;

import org.example.game.Chessboard;
import org.example.game.UI;
import org.example.game.entity.ChessEntity;

public class GobangTest {
    public static void main(String[] args){
        Chessboard chessboard = new Chessboard();
        ChessEntity chess = new ChessEntity();
        new UI().init(chessboard,chess);
    }
}
