package org.example.game;

import org.example.game.entity.ChessEntity;
import org.example.game.entity.LocationEntity;
import org.example.game.service.ChessService;
import org.example.game.service.impl.ChessServiceImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * todo 把chessboard解耦，有chessboard的都要改，标记的不全
 * UI界面
 */
public class UI{

    private JFrame frame;//五子棋游戏窗口

    //五子棋盘【关键】
    private Chessboard chessboard;
    //*****五子棋业务逻辑【关键】
    private ChessService chessService = new ChessServiceImpl();

    private JMenuBar menu;//菜单栏
    private JMenu option;//选项菜单
    private Action replayOption;//重玩一盘选项
    private Action AIFirstOption;//机器先手选项
    private Action HumanFirstOption;//人类先手选项

    private Action undoOption;//悔棋选项



    //完成五子棋游戏界面
    public void init(Chessboard board){
        chessboard = board;
        frame = new JFrame("人机对战五子棋");//创建游戏界面窗口
        menu = new JMenuBar();//创建菜单栏
        option = new JMenu("选项");//创建菜单栏中的“选项”菜单

        //把“选项”菜单加入到菜单栏
        menu.add(option);

        //把“重玩一盘”、“机器先手”、“人类先手”加入“选项”下拉项中
        replayOptionInit();
        option.add(replayOption);
        AIFirstOptionInit();
        option.add(AIFirstOption);
        HumanFirstOptionInit();
        option.add(HumanFirstOption);
        undoOptionInit();
        option.add(undoOption);

        frame.setJMenuBar(menu);//把menu设置为frame的菜单栏
        frame.add(chessboard);//把五子棋盘加入到frame

        //初始化棋盘
        //todo
        chessboard.init();
        chessService.init();

        //【【【最核心】】】绑定鼠标事件，要下棋了，为了避免写无用的抽象方法的实现，用适配器
        chessboard.addMouseListener(new MouseAdapter(){
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



    //“重玩一盘”选项绑定相应的处理事件
    public void replayOptionInit(){
        replayOption = new AbstractAction("重玩一盘", new ImageIcon("image/replay.png")){
            public void actionPerformed(ActionEvent e){
                //todo
                chessboard.init();//界面方面：初始化重来
                chessService.init();//逻辑业务方面：初始化重来
                chessService.init();//逻辑业务方面：初始化重来
            }
        };
    }

    //“机器先手”选项绑定相应的处理事件
    public void AIFirstOptionInit(){
        AIFirstOption = new AbstractAction("机器先手", new ImageIcon("image/robot.png")){
            public void actionPerformed(ActionEvent e){
                //棋盘还没有落子的时候可以选择“机器先手”，一旦有落子，选择“机器先手”失效
                if(chessboard.isEmpty()){
                    ChessEntity.FIRST = -1;
                    //机器先手，则先在中间位置下一个棋子
                    //todo
                    chessboard.addChessman(7, 7, -1);
                    chessService.addChessman(new LocationEntity(7,7,-1));
                }
            }
        };
    }

    //“人类先手”选项绑定相应的处理事件
    public void HumanFirstOptionInit(){
        HumanFirstOption = new AbstractAction("人类先手", new ImageIcon("image/human.png")){
            public void actionPerformed(ActionEvent e){
                //棋盘还没有落子的时候可以选择“人类先手”，一旦有落子，选择“人类先手”失效
                //todo
                if(chessboard.isEmpty()){
                    chessService.getChess().FIRST = 1;
                }
            }
        };
    }

    //【【【核心业务逻辑】】】处理鼠标落子事件
    public void play(MouseEvent e){
        int cellSize = chessboard.getCellSize();//每个格子的边长
        int x = (e.getX() - 5) / cellSize;//像素值转换成棋盘坐标
        int y = (e.getY() - 5) / cellSize;//像素值转换成棋盘坐标
        //判断落子是否合法
        boolean isLegal = chessService.isLegal(new LocationEntity(x,y));
        //如果落子合法
        if(isLegal){
            //todo
            chessboard.addChessman(x, y, 1);//界面方面加一个棋子
            chessService.addChessman(new LocationEntity(x,y,1));//逻辑业务方面加一个棋子

            //判断人类是否胜利
            if(chessService.isWin(new LocationEntity(x,y,1))){
                JOptionPane.showMessageDialog(frame, "人类获胜", "Congratulations，您赢了！", JOptionPane.PLAIN_MESSAGE);
                //todo
                chessboard.init();
                chessService.init();
                return;
            }

            //机器落子
            LocationEntity loc = chessService.searchLocation();
            //todo
            chessboard.addChessman(loc);
            chessService.addChessman(new LocationEntity(loc.getX(), loc.getY(), loc.getOwner()));


            //判断机器是否胜利
            if(chessService.isWin(new LocationEntity(loc.getX(), loc.getY(),  -1))){
                JOptionPane.showMessageDialog(frame, "机器获胜", "Congratulations，您输了！", JOptionPane.PLAIN_MESSAGE);
                //todo
                chessboard.init();
                chessService.init();
            }
        }
    }

    public void undoOptionInit(){
        undoOption = new AbstractAction("悔棋", new ImageIcon("image/undo.png")) {
            public void actionPerformed(ActionEvent e) {
                // 检查是否有可以悔棋的操作
                if (chessService.undoMove()) { // 若棋局逻辑能悔棋
                    //todo
                    chessboard.undoMove(); // 在界面上也撤销最新的一步棋
                } else {
                    JOptionPane.showMessageDialog(frame, "无可悔棋的操作", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
    }
}
