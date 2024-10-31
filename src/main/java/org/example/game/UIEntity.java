package org.example.game.entity;

import lombok.Data;
import org.example.game.service.ChessBoardService;
import org.example.game.service.ChessService;
import org.example.game.service.impl.ChessBoardServiceImpl;
import org.example.game.service.impl.ChessServiceImpl;

import javax.swing.*;
@Data
public class UIEntity {

    private JFrame frame;//五子棋游戏窗口

    private ChessService chessService = new ChessServiceImpl();
    private ChessBoardService chessboardService = new ChessBoardServiceImpl();

    private JMenuBar menu;//菜单栏
    private JMenu option;//选项菜单
    private Action replayOption;//重玩一盘选项
    private Action AIFirstOption;//机器先手选项
    private Action HumanFirstOption;//人类先手选项

    private Action undoOption;//悔棋选项

}
