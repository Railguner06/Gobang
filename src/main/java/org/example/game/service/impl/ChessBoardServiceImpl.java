package org.example.game.service.impl;

import org.example.game.entity.ChessEntity;
import org.example.game.entity.ChessboardEntity;
import org.example.game.entity.LocationEntity;
import org.example.game.service.ChessBoardService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.example.game.entity.ChessboardEntity.CHESSBOARD_SIZE;

public class ChessBoardServiceImpl extends JPanel implements ChessBoardService {

    private ChessboardEntity chessboard;

    /**
     * 初始化
     */
    @Override
    public void init() {
        chessboard = new ChessboardEntity();
        chessboard.init();
        repaint();
    }

    /**
     * 覆盖paint方法
     */
    public void paint(Graphics g) {
        super.paint(g);
        drawChessboard(g);
        drawChessman(g);
    }

    /**
     * 画棋盘
     * @param g
     */
    private void drawChessboard(Graphics g) {
        Color backgroundColor = chessboard.getBackgroundColor();
        Color lineColor = chessboard.getLineColor();
        int margin = chessboard.getMargin();

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

    /**
     * 画棋子
     * @param g
     */
    private void drawChessman(Graphics g) {

        ArrayList<LocationEntity> locationList = chessboard.getLocationList();
        int margin = chessboard.getMargin();

        for (LocationEntity loc : locationList) {
            g.setColor(loc.getOwner() == ChessEntity.FIRST ? Color.BLACK : Color.WHITE);
            int cellSize = (this.getWidth() - 2 * margin) / (CHESSBOARD_SIZE - 1); // 每个格子的边长
            g.fillOval(margin + cellSize * loc.getX() - cellSize / 2, margin + cellSize * loc.getY() - cellSize / 2, cellSize, cellSize);
        }
    }

    /**
     * 落子
     * @param location
     */
    @Override
    public void addChessman(LocationEntity location) {
        addChessman(location.getX(), location.getY(), location.getOwner());
    }

    private void addChessman(int x, int y, int owner) {
        int[][] boardState = chessboard.getBoardState();
        ArrayList<LocationEntity> locationList = chessboard.getLocationList();

        if (boardState[x][y] == 0) { // 检查当前位置是否为空
            locationList.add(new LocationEntity(x, y, owner));
            boardState[x][y] = owner; // 更新棋盘状态
            repaint();
        }
    }

    /**
     * 计算棋盘每个小格子的大小
     * @return
     */
    @Override
    public int getCellSize() {
        int margin = chessboard.getMargin();
        return (this.getWidth() - 2 * margin) / (CHESSBOARD_SIZE - 1);
    }

    /**
     * 判断棋盘是否还没有棋子
     * @return
     */
    @Override
    public boolean isEmpty() {
        ArrayList<LocationEntity> locationList = chessboard.getLocationList();
        return locationList.isEmpty();
    }

    /**
     * 悔棋：移除最后两个棋子
     */
    @Override
    public void undoMove() {
        ArrayList<LocationEntity> locationList = chessboard.getLocationList();
        int[][] boardState = chessboard.getBoardState();
        if (!locationList.isEmpty()) {
            LocationEntity lastMove = locationList.remove(locationList.size() - 1); // 移除最后一个棋子
            boardState[lastMove.getX()][lastMove.getY()] = 0; // 更新棋盘状态为空
        }
        if (!locationList.isEmpty()) {
            LocationEntity secondLastMove = locationList.remove(locationList.size() - 1); // 再次移除上一个棋子
            boardState[secondLastMove.getX()][secondLastMove.getY()] = 0; // 更新棋盘状态为空
        }
        repaint(); // 重新绘制棋盘
    }

    /**
     * 返回自身实例 使用 ChessBoardService 获取棋盘组件
     * @return
     */
    @Override
    public JComponent getChessboardComponent() {
        return this;
    }


}