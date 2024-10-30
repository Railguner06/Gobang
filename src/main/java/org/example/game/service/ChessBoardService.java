package org.example.game.service;

import org.example.game.entity.LocationEntity;

import javax.swing.*;

public interface ChessBoardService {

    /**
     * 初始化
     */
    void init();

    /**
     * 重载的落子方法
     * @param location
     */
    void addChessman(LocationEntity location);

    /**
     * 计算棋盘每个小格子的大小
     * @return
     */
    int getCellSize();

    /**
     * 判断棋盘是否还没有棋子
     * @return
     */
    boolean isEmpty();

    /**
     * 悔棋：移除最后两个棋子
     */
    void undoMove();

    /**
     * 返回自身实例 使用 ChessBoardService 获取棋盘组件
     * @return
     */
    JComponent getChessboardComponent();

}
