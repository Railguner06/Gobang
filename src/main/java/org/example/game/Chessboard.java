package org.example.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Chessboard extends JPanel{

    public static final int CHESSBOARD_SIZE = 15; // 棋盘大小15X15
    private ArrayList<Location> locationList = new ArrayList<>(); // 棋盘上所有已落子的位置坐标
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
        repaint();
    }

    // 覆盖paint方法
    public void paint(Graphics g) {
        super.paint(g);
        drawChessboard(g);
        drawChessman(g);
    }

    // 画棋盘
    public void drawChessboard(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // 画线
        g.setColor(lineColor);
        int cellSize = (this.getWidth() - 2 * margin) / (CHESSBOARD_SIZE - 1); // 每个格子的边长
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            g.drawLine(margin, margin + i * cellSize, this.getWidth() - margin, margin + i * cellSize); // 画横线
            g.drawLine(margin + i * cellSize, margin, margin + i * cellSize, this.getHeight() - margin); // 画纵线
        }
    }

    // 画棋子
    public void drawChessman(Graphics g) {
        for (Location loc : locationList) {
            g.setColor(loc.getOwner() == Chess.FIRST ? Color.BLACK : Color.WHITE);
            int cellSize = (this.getWidth() - 2 * margin) / (CHESSBOARD_SIZE - 1); // 每个格子的边长
            g.fillOval(margin + cellSize * loc.getX() - cellSize / 2, margin + cellSize * loc.getY() - cellSize / 2, cellSize, cellSize);
        }
    }

    // 落子
    public void addChessman(int x, int y, int owner) {
        if (boardState[x][y] == 0) { // 检查当前位置是否为空
            locationList.add(new Location(x, y, owner));
            boardState[x][y] = owner; // 更新棋盘状态
            repaint();
        }
    }

    // 重载的落子方法
    public void addChessman(Location loc) {
        addChessman(loc.getX(), loc.getY(), loc.getOwner());
    }

    // 计算棋盘每个小格子的大小
    public int getCellSize() {
        return (this.getWidth() - 2 * margin) / (CHESSBOARD_SIZE - 1);
    }

    // 判断棋盘是否还没有棋子
    public boolean isEmpty() {
        return locationList.isEmpty();
    }

    // 悔棋：移除最后两个棋子
    public void undoMove() {
        if (!locationList.isEmpty()) {
            Location lastMove = locationList.remove(locationList.size() - 1); // 移除最后一个棋子
            boardState[lastMove.getX()][lastMove.getY()] = 0; // 更新棋盘状态为空
        }
        if (!locationList.isEmpty()) {
            Location secondLastMove = locationList.remove(locationList.size() - 1); // 再次移除上一个棋子
            boardState[secondLastMove.getX()][secondLastMove.getY()] = 0; // 更新棋盘状态为空
        }
        repaint(); // 重新绘制棋盘
    }
}