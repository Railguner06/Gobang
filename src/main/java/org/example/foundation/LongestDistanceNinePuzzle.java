package org.example.foundation;

import java.util.*;

public class LongestDistanceNinePuzzle {
    static class Node {
        int[][] state;       // 当前状态
        String moves;        // 移动序列
        int dist;            // 当前移动距离
        int zeroRow, zeroCol;// 空格位置

        Node(int[][] state, String moves, int dist, int zeroRow, int zeroCol) {
            this.state = state;
            this.moves = moves;
            this.dist = dist;
            this.zeroRow = zeroRow;
            this.zeroCol = zeroCol;
        }
    }

    // 定义空格四个移动方向（上、下、左、右）
    private static final int[][] DIRECTIONS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private static final String[] DIR_MOVES = { "U", "D", "L", "R" };
    private static final int[][] TARGET = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };

    // 检查边界合法性
    private static boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    // 序列化矩阵状态为字符串
    private static String serializeState(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) {
            for (int num : row) {
                sb.append(num);
            }
        }
        return sb.toString();
    }

    // 使用BFS找到最长路径
    public static void bfsLongestPath(int[][] initialState) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int longestDistance = 0;
        List<Node> longestStates = new ArrayList<>();

        // 找到初始空格位置
        int zeroRow = 0, zeroCol = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (initialState[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }

        // 初始化队列和访问集合
        Node start = new Node(initialState, "", 0, zeroRow, zeroCol);
        queue.add(start);
        visited.add(serializeState(initialState));

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.dist > longestDistance) {
                longestDistance = node.dist;
                longestStates.clear();
                longestStates.add(node);
            } else if (node.dist == longestDistance) {
                longestStates.add(node);
            }

            // 扩展4个方向
            for (int i = 0; i < 4; i++) {
                int newRow = node.zeroRow + DIRECTIONS[i][0];
                int newCol = node.zeroCol + DIRECTIONS[i][1];

                if (isWithinBounds(newRow, newCol)) {
                    int[][] newState = new int[3][3];
                    for (int r = 0; r < 3; r++) {
                        newState[r] = node.state[r].clone();
                    }
                    // 移动空格
                    newState[node.zeroRow][node.zeroCol] = newState[newRow][newCol];
                    newState[newRow][newCol] = 0;

                    String serialized = serializeState(newState);
                    if (!visited.contains(serialized)) {
                        visited.add(serialized);
                        String newMoves = node.moves + DIR_MOVES[i];
                        queue.add(new Node(newState, newMoves, node.dist + 1, newRow, newCol));
                    }
                }
            }
        }

        // 输出结果
        System.out.println(longestDistance + " " + longestStates.size());
        for (Node result : longestStates) {
            for (int[] row : result.state) {
                for (int num : row) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }
            System.out.println(result.moves);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] initialState = new int[3][3];

        System.out.println("请输入九宫格的初始状态（3行，每行3个数字，空格用0表示）：");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                initialState[i][j] = scanner.nextInt();
            }
        }

        bfsLongestPath(initialState);
    }
}
