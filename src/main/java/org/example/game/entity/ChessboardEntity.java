package org.example.game.entity;

import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

@Data
public class ChessboardEntity {

    public static final int CHESSBOARD_SIZE = 15; // 棋盘大小15X15
    private ArrayList<LocationEntity> locationList = new ArrayList<>(); // 棋盘上所有已落子的位置坐标
    private Color backgroundColor = new Color(255, 245, 186); // 棋盘背景色
    private Color lineColor = new Color(66, 66, 66); // 棋盘线条颜色
    private int margin = 20; // 棋盘边缘长度
    private int[][] boardState = new int[CHESSBOARD_SIZE][CHESSBOARD_SIZE]; // 记录棋盘状态，0 表示空，1 表示玩家，-1 表示 AI

    // 初始化棋盘
    public void init() {
        locationList.clear();
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            for (int j = 0; j < CHESSBOARD_SIZE; j++) {
                boardState[i][j] = 0; // 清空棋盘状态
            }
        }
    }


    public ArrayList<LocationEntity> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LocationEntity> locationList) {
        this.locationList = locationList;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int[][] getBoardState() {
        return boardState;
    }

    public void setBoardState(int[][] boardState) {
        this.boardState = boardState;
    }
}
