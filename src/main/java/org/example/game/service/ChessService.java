package org.example.game.service;

import org.example.game.entity.ChessEntity;
import org.example.game.entity.LocationEntity;

public interface ChessService {

    /**
     * 获取实体
     * @return
     */
    ChessEntity getChess();

    /**
     * 初始化
     */
    void init();

    /**
     * 落子
     * @param location
     */
    void addChessman(LocationEntity location);


    /**
     * 判断落子位置是否合法
     * @param location
     * @return
     */
    boolean isLegal(LocationEntity location);

    /**
     * 悔棋：回退两步
     * @return
     */
    boolean undoMove();

    /**
     * 判断哪方赢了
     * （必定有刚落的子引发，因此只需判断刚落子的周围），owner为-1代表机器，owner为1代表人类
     * @param location
     * @return
     */
    boolean isWin(LocationEntity location);

    /**
     * 搜查位置
     * @return
     */
    LocationEntity searchLocation();

}
