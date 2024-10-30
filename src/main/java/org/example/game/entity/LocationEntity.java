package org.example.game.entity;

import lombok.Data;

@Data
public class LocationEntity {

    //某个棋盘位置横坐标，0-14
    private int x;
    //某个棋盘位置纵坐标，0-14
    private int y;
    //占据该位置的棋手方，1是人类，-1是机器，0是空
    private int owner;

    public LocationEntity(int x, int y, int owner){
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    /**
     * 用于鼠标落子
     * @param x
     * @param y
     */
    public LocationEntity(int x, int y){
        this(x,y,1);
    }

    public LocationEntity(LocationEntity location){
        this.x = location.x;
        this.y = location.y;
        this.owner = location.owner;
    }

}
