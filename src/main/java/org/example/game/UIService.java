package org.example.game.service;

import java.awt.event.MouseEvent;

public interface UIService {
    /**
     * 初始化
     */
    void init();

    /**
     * 重玩一盘
     */
    void replayOptionInit();

    /**
     * 机器先手
     */
    void AIFirstOptionInit();

    /**
     * 人类先手
     */
    void HumanFirstOptionInit();

    /**
     * 鼠标点击事件
     * @param e
     */
    void play(MouseEvent e);

    /**
     * 悔棋
     */
    void undoOptionInit();
}
