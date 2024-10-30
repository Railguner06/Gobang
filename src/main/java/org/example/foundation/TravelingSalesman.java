package org.example.foundation;

import java.util.Scanner;

public class TravelingSalesman {
    private static int n;                // 城市数量
    private static int[][] distance;     // 城市间的距离
    private static boolean[] visited;    // 记录城市是否访问过
    private static int minPath = Integer.MAX_VALUE;  // 最短路径长度
    private static String minPathRoute = "";         // 最短路径

    // 深度优先搜索（DFS）+ 回溯法
    private static void dfs(int city, int count, int currentCost, String route) {
        if (count == n && distance[city][0] > 0) {
            int totalCost = currentCost + distance[city][0];
            if (totalCost < minPath) {
                minPath = totalCost;
                minPathRoute = route + "->1";
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i] && distance[city][i] > 0) {
                visited[i] = true;
                dfs(i, count + 1, currentCost + distance[city][i], route + "->" + (i + 1));
                visited[i] = false;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入地点数 n：");
        n = scanner.nextInt();
        distance = new int[n][n];
        visited = new boolean[n];

        System.out.println("请输入地点之间的连线数 e：");
        int e = scanner.nextInt();

        System.out.println("请在依次输入两个地点 u 和 v 之间的距离 w，格式：地点 u 地点 v 距离 w");
        for (int i = 0; i < e; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            int w = scanner.nextInt();
            distance[u][v] = w;
            distance[v][u] = w;
        }
        scanner.close();

        // 从第一个城市出发
        visited[0] = true;
        dfs(0, 1, 0, "1");

        System.out.println("最短路径：" + minPathRoute);
        System.out.println("最短路径长度：" + minPath);
    }
}
