package org.example.game.service.impl;

import lombok.Data;
import org.example.game.entity.LocationEntity;
import org.example.game.entity.ChessEntity;
import org.example.game.service.ChessService;

import java.util.Stack;

import static org.example.game.entity.ChessEntity.CHESSBOARD_SIZE;
@Data
public class ChessServiceImpl implements ChessService {

    private ChessEntity chess;

    public ChessEntity getChess() {
        return chess;
    }

    /**
     * 初始化
     */
    @Override
    public void init(){
        chess = new ChessEntity();
        chess.init();
    }

    /**
     * 落子(包括机器下子）
     * @param location
     */
    @Override
    public void addChessman(LocationEntity location){
        int x = location.getX();
        int y = location.getY();
        int owner = location.getOwner();
        int[][] chessboard = chess.getChessboard();
        // 落子并更新棋盘状态
        if (chessboard[x][y] == 0) { // 只有在位置为空时才能落子
            chessboard[x][y] = owner;
            chess.push(location); // 记录该落子位置
        }
        chessboard[location.getX()][location.getY()] = location.getOwner();
        chess.setChessboard(chessboard);
    }

    /**
     * 判断落子位置是否合法
     * @param location
     * @return
     */
    @Override
    public boolean isLegal(LocationEntity location) {
        int x = location.getX();
        int y = location.getY();
        int[][] chessboard = chess.getChessboard();
        if(x >=0 && x < CHESSBOARD_SIZE && y >= 0 && y < CHESSBOARD_SIZE && chessboard[x][y] == 0){
            return true;
        }
        return false;
    }

    /**
     * 悔棋：回退两步
     * @return
     */
    @Override
    public boolean undoMove() {
        Stack<LocationEntity> history = chess.getHistory();
        int[][] chessboard = chess.getChessboard();
        if (history.size() >= 2) { // 检查是否有足够步数可以悔棋
            LocationEntity lastMove = history.pop(); // 取出最后一个落子位置
            chessboard[lastMove.getX()][lastMove.getY()] = 0; // 清空该位置
            LocationEntity secondLastMove = history.pop(); // 取出倒数第二个落子位置
            chessboard[secondLastMove.getX()][secondLastMove.getY()] = 0; // 清空该位置
            return true;
        }
        return false; // 不足两步无法悔棋
    }

    /**
     * 判断哪方赢了
     * （必定有刚落的子引发，因此只需判断刚落子的周围），owner为-1代表机器，owner为1代表人类
     * @param location
     * @return
     */
    @Override
    public boolean isWin(LocationEntity location){
        int x = location.getX();
        int y = location.getY();
        int owner = location.getOwner();
        int[][] chessboard = chess.getChessboard();

        int sum = 0;

        //判断横向左边
        for(int i = x - 1; i >= 0; i--){
            if(chessboard[i][y] == owner){sum++;}
            else{break;}
        }
        //判断横向右边
        for(int i = x + 1; i < CHESSBOARD_SIZE; i++){
            if(chessboard[i][y] == owner){sum++;}
            else{break;}
        }
        if(sum >= 4) {return true;}

        sum = 0;
        //判断纵向上边
        for(int i = y - 1; i >= 0; i--){
            if(chessboard[x][i] == owner){sum++;}
            else{break;}
        }
        //判断纵向下边
        for(int i = y + 1; i < CHESSBOARD_SIZE; i++){
            if(chessboard[x][i] == owner){sum++;}
            else{break;}
        }
        if(sum >= 4) {return true;}

        sum = 0;
        //判断左上角到右下角方向上侧
        for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j-- ){
            if(chessboard[i][j] == owner){sum++;}
            else{break;}
        }
        //判断左上角到右下角方向下侧
        for(int i = x + 1, j = y + 1; i < CHESSBOARD_SIZE && j < CHESSBOARD_SIZE; i++, j++ ){
            if(chessboard[i][j] == owner){sum++;}
            else{break;}
        }
        if(sum >= 4) {return true;}

        sum = 0;
        //判断右上角到左下角方向上侧
        for(int i = x + 1, j = y - 1; i < CHESSBOARD_SIZE && j >= 0; i++, j-- ){
            if(chessboard[i][j] == owner){sum++;}
            else{break;}
        }
        //判断右上角到左下角方向下侧
        for(int i = x - 1, j = y + 1; i >= 0 && j < CHESSBOARD_SIZE; i--, j++ ){
            if(chessboard[i][j] == owner){sum++;}
            else{break;}
        }
        if(sum >= 4) {return true;}

        return false;

    }

    /**
     * 五元组搜查位置
     * @return
     */
    @Override
    public LocationEntity searchLocation(){
        int[][] chessboard = chess.getChessboard();
        int[][] score = chess.getScore();

        //每次机器找寻落子位置，评分都重新算一遍（虽然算了很多多余的，因为上次落子时候算的大多都没变）
        //先定义一些变量
        int humanChessmanNum = 0;//五元组中的黑棋数量
        int machineChessmanNum = 0;//五元组中的白棋数量
        int tupleScoreTmp = 0;//五元组得分临时变量

        int goalX = -1;//目标位置x坐标
        int goalY = -1;//目标位置y坐标
        int maxScore = -1;//最大分数

        //1.扫描横向的15个行
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
                while(k < j + 5){

                    if(chessboard[i][k] == -1) machineChessmanNum++;
                    else if(chessboard[i][k] == 1)humanChessmanNum++;

                    k++;
                }
                tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                //为该五元组的每个位置添加分数
                for(k = j; k < j + 5; k++){
                    score[i][k] += tupleScoreTmp;
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量
            }
        }

        //2.扫描纵向15行
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
                while(k < j + 5){
                    if(chessboard[k][i] == -1) machineChessmanNum++;
                    else if(chessboard[k][i] == 1)humanChessmanNum++;

                    k++;
                }
                tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                //为该五元组的每个位置添加分数
                for(k = j; k < j + 5; k++){
                    score[k][i] += tupleScoreTmp;
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量
            }
        }

        //3.扫描右上角到左下角上侧部分
        for(int i = 14; i >= 4; i--){
            for(int k = i, j = 0; j < 15 && k >= 0; j++, k--){
                int m = k;
                int n = j;
                while(m > k - 5 && k - 5 >= -1){
                    if(chessboard[m][n] == -1) machineChessmanNum++;
                    else if(chessboard[m][n] == 1)humanChessmanNum++;

                    m--;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k-5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m > k - 5 ; m--, n++){
                        score[m][n] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //4.扫描右上角到左下角下侧部分
        for(int i = 1; i < 15; i++){
            for(int k = i, j = 14; j >= 0 && k < 15; j--, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(chessboard[n][m] == -1) machineChessmanNum++;
                    else if(chessboard[n][m] == 1)humanChessmanNum++;

                    m++;
                    n--;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k+5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n--){
                        score[n][m] += tupleScoreTmp;
                    }
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //5.扫描左上角到右下角上侧部分
        for(int i = 0; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(chessboard[m][n] == -1) machineChessmanNum++;
                    else if(chessboard[m][n] == 1)humanChessmanNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k + 5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
                        score[m][n] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //6.扫描左上角到右下角下侧部分
        for(int i = 1; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(chessboard[n][m] == -1) machineChessmanNum++;
                    else if(chessboard[n][m] == 1)humanChessmanNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k + 5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
                        score[n][m] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //从空位置中找到得分最大的位置
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(chessboard[i][j] == 0 && score[i][j] > maxScore){
                    goalX = i;
                    goalY = j;
                    maxScore = score[i][j];
                }
            }
        }

        if(goalX != -1 && goalY != -1){
            return new LocationEntity(goalX, goalY, -1);
        }

        //没找到坐标说明平局了，笔者不处理平局
        return new LocationEntity(-1, -1, -1);
    }

    /**
     * 各种五元组情况评分表
     * @param humanChessmanNum
     * @param machineChessmanNum
     * @return
     */
    private int tupleScore(int humanChessmanNum, int machineChessmanNum){
        //1.既有人类落子，又有机器落子，判分为0
        if(humanChessmanNum > 0 && machineChessmanNum > 0){
            return 0;
        }
        //2.全部为空，没有落子，判分为7
        if(humanChessmanNum == 0 && machineChessmanNum == 0){
            return 7;
        }
        //3.机器落1子，判分为35
        if(machineChessmanNum == 1){
            return 35;
        }
        //4.机器落2子，判分为800
        if(machineChessmanNum == 2){
            return 800;
        }
        //5.机器落3子，判分为15000
        if(machineChessmanNum == 3){
            return 15000;
        }
        //6.机器落4子，判分为800000
        if(machineChessmanNum == 4){
            return 800000;
        }
        //7.人类落1子，判分为15
        if(humanChessmanNum == 1){
            return 15;
        }
        //8.人类落2子，判分为400
        if(humanChessmanNum == 2){
            return 400;
        }
        //9.人类落3子，判分为1800
        if(humanChessmanNum == 3){
            return 1800;
        }
        //10.人类落4子，判分为100000
        if(humanChessmanNum == 4){
            return 100000;
        }
        return -1;//若是其他结果肯定出错了。这行代码根本不可能执行
    }
}
