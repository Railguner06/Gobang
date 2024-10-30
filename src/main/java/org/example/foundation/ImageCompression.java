package org.example.foundation;

import java.util.Arrays;
import java.util.Scanner;

public class ImageCompression {

    // 计算 [start, end] 区间的最小存储位数
    private static int calculateCost(int[] pixelValues, int start, int end) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        // 找出区间内的最小值和最大值
        for (int i = start; i <= end; i++) {
            min = Math.min(min, pixelValues[i]);
            max = Math.max(max, pixelValues[i]);
        }

        // 计算位数：需要的位数是 log2(max - min + 1)
        int range = max - min + 1;
        if (range <= 1) return 1 * (end - start + 1); // 如果区间范围小于等于1，则只需要1位
        int bits = (int) Math.ceil(Math.log(range) / Math.log(2)); // 位数
        return bits * (end - start + 1); // 段的总位数
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入像素点的灰度值，以空格隔开：");
        String[] input = scanner.nextLine().split(" ");
        int n = input.length;
        int[] pixelValues = new int[n];
        for (int i = 0; i < n; i++) {
            pixelValues[i] = Integer.parseInt(input[i]);
        }

        // 动态规划数组，dp[i] 表示前 i 个像素点的最小存储位数
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        // 分段数组，用于记录最优分段的位置
        int[] split = new int[n + 1];

        // 动态规划过程
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                int cost = calculateCost(pixelValues, j, i - 1);
                if (dp[j] != Integer.MAX_VALUE && dp[j] + cost < dp[i]) {
                    dp[i] = dp[j] + cost;
                    split[i] = j;
                }
            }
        }

        System.out.println("最少存储位数：" + dp[n] + "bit");

        // 输出最优解的分段情况
        System.out.print("最优解的分段情况：");
        int k = n;
        int segment = 1;
        while (k > 0) {
            int start = split[k];
            System.out.print("b[" + segment + "]=" + (k - start) + " ");
            segment++;
            k = start;
        }

        scanner.close();
    }
}
