package org.example.game.service.impl;

import org.example.game.entity.ChessEntity;
import org.example.game.entity.LocationEntity;
import org.example.game.entity.UIEntity;
import org.example.game.service.ChessBoardService;
import org.example.game.service.ChessService;
import org.example.game.service.UIService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIServiceImpl implements UIService {
    UIEntity uiEntity ;
    @Override
    public void init() {
        uiEntity = new UIEntity();
        uiEntity.setFrame(new JFrame("人机对战五子棋")) ;//创建游戏界面窗口
        uiEntity.setMenu(new JMenuBar());//创建菜单栏
        uiEntity.setOption(new JMenu("选项"));//创建菜单栏中的“选项”菜单
        JFrame frame = uiEntity.getFrame();
        JMenuBar menu = uiEntity.getMenu();
        JMenu option = uiEntity.getOption();
        //把“选项”菜单加入到菜单栏
        menu.add(option);

        //把“重玩一盘”、“机器先手”、“人类先手” “悔棋”加入“选项”下拉项中
        replayOptionInit();
        option.add(uiEntity.getReplayOption());
        AIFirstOptionInit();
        option.add(uiEntity.getAIFirstOption());
        HumanFirstOptionInit();
        option.add(uiEntity.getHumanFirstOption());
        undoOptionInit();
        option.add(uiEntity.getUndoOption());

        frame.setJMenuBar(menu);//把menu设置为frame的菜单栏
        frame.add(uiEntity.getChessboardService().getChessboardComponent());//把五子棋盘加入到frame

        //初始化棋盘
        uiEntity.getChessboardService().init();
        uiEntity.getChessService().init();

        //绑定鼠标事件，要下棋了
        uiEntity.getChessboardService().getChessboardComponent().addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                //鼠标点击引发下棋事件，处理下棋事件比较繁琐，为此开一个方法
                play(e);
            }
        });

        //设置frame窗口左上角图标
        frame.setIconImage(frame.getToolkit().getImage("image/gobang.png"));
        frame.setSize(518, 565);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void replayOptionInit() {
        uiEntity.setReplayOption(new AbstractAction("重玩一盘", new ImageIcon("image/replay.png")){
            public void actionPerformed(ActionEvent e){
                uiEntity.getChessboardService().init();//界面方面：初始化重来
                uiEntity.getChessService().init();//逻辑业务方面：初始化重来
                uiEntity.getChessService().init();//逻辑业务方面：初始化重来
            }
        });
    }

    @Override
    public void AIFirstOptionInit() {
        uiEntity.setAIFirstOption(new AbstractAction("机器先手", new ImageIcon("image/robot.png")){
            public void actionPerformed(ActionEvent e){
                //棋盘还没有落子的时候可以选择“机器先手”，一旦有落子，选择“机器先手”失效
                if(uiEntity.getChessboardService().isEmpty()){
                    ChessEntity.FIRST = -1;
                    //机器先手，则先在中间位置下一个棋子
                    uiEntity.getChessboardService().addChessman(new LocationEntity(7,7,-1));
                    uiEntity.getChessService().addChessman(new LocationEntity(7,7,-1));
                }
            }
        });
    }

    @Override
    public void HumanFirstOptionInit() {
        uiEntity.setHumanFirstOption(new AbstractAction("人类先手", new ImageIcon("image/human.png")){
            public void actionPerformed(ActionEvent e){
                //棋盘还没有落子的时候可以选择“人类先手”，一旦有落子，选择“人类先手”失效
                if(uiEntity.getChessboardService().isEmpty()){
                    uiEntity.getChessService().getChess().FIRST = 1;
                }
            }
        });
    }

    @Override
    public void play(MouseEvent e) {
        ChessBoardService chessboardService = uiEntity.getChessboardService();
        ChessService chessService = uiEntity.getChessService();
        int cellSize = chessboardService.getCellSize();//每个格子的边长
        int x = (e.getX() - 5) / cellSize;//像素值转换成棋盘坐标
        int y = (e.getY() - 5) / cellSize;//像素值转换成棋盘坐标
        //判断落子是否合法
        boolean isLegal = chessService.isLegal(new LocationEntity(x,y));
        //如果落子合法
        if(isLegal){
            chessboardService.addChessman(new LocationEntity(x,y,1));//界面方面加一个棋子
            chessService.addChessman(new LocationEntity(x,y,1));//逻辑业务方面加一个棋子

            //判断人类是否胜利
            if(chessService.isWin(new LocationEntity(x,y,1))){
                JOptionPane.showMessageDialog(uiEntity.getFrame(), "人类获胜", "Congratulations，您赢了！", JOptionPane.PLAIN_MESSAGE);
                chessboardService.init();
                chessService.init();
                return;
            }

            //机器落子
            LocationEntity loc = chessService.searchLocation();
            chessboardService.addChessman(loc);
            chessService.addChessman(new LocationEntity(loc.getX(), loc.getY(), loc.getOwner()));


            //判断机器是否胜利
            if(chessService.isWin(new LocationEntity(loc.getX(), loc.getY(),  -1))){
                JOptionPane.showMessageDialog(uiEntity.getFrame(), "机器获胜", "Congratulations，您输了！", JOptionPane.PLAIN_MESSAGE);
                chessboardService.init();
                chessService.init();
            }
        }
    }

    @Override
    public void undoOptionInit() {
        uiEntity.setUndoOption(new AbstractAction("悔棋", new ImageIcon("image/undo.png")) {
            public void actionPerformed(ActionEvent e) {
                // 检查是否有可以悔棋的操作
                if (uiEntity.getChessService().undoMove()) { // 若棋局逻辑能悔棋
                    uiEntity.getChessboardService().undoMove(); // 在界面上也撤销最新的一步棋
                } else {
                    JOptionPane.showMessageDialog(uiEntity.getFrame(), "无可悔棋的操作", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
