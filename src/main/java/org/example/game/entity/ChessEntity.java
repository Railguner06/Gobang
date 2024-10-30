package org.example.game.entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

@Data
public class ChessEntity {

    public static final int CHESSBOARD_SIZE = 15;
    //先手，-1表示机器，1表示人类，与LocationEntity中的对应
    public static int FIRST = 1;
    //与界面棋盘对应，0代表空，-1代表机器，1代表人类
    private int[][] chessboard = new int[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    //每个位置得分
    private int[][] score = new int[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    // 用于记录每一步的历史
    private Stack<LocationEntity> history = new Stack<>();

    private Set<LocationEntity> disabledLocations = new HashSet<>();


    /**
     * 初始化
     */
    public void init(){
        FIRST = 1;//默认人类先手
        for(int i = 0; i  < CHESSBOARD_SIZE; i++){
            for(int j = 0; j < CHESSBOARD_SIZE; j++){
                chessboard[i][j] = 0;
                score[i][j] = 0;
            }
        }
        history.clear();
    }

    /**
     * 获取棋盘
     * @return
     */
    public int[][] getChessboard(){
        return chessboard;
    }

    /**
     * push到栈
     * @param location
     */
    public void push(LocationEntity location) {
        history.push(location);
    }

    /**
     * 获取 history 栈
     * @return
     */
    public Stack<LocationEntity> getHistory() {
        return history;
    }

    /**
     * 获取评分数组
     * @return
     */
    public int[][] getScore(){
        initScore();
        return score;
    }

    /**
     * 初始化评分数组
     */
    private void initScore(){
        for(int i = 0; i  < CHESSBOARD_SIZE; i++){
            for(int j = 0; j < CHESSBOARD_SIZE; j++){
                score[i][j] = 0;
            }
        }
    }

}
