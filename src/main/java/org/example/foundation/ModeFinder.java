package org.example.foundation;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ModeFinder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入多重集合的元素，以空格隔开：");
        String[] input = scanner.nextLine().split(" ");

        int[] arr = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            arr[i] = Integer.parseInt(input[i]);  // 将输入的字符串数组转换为整数数组
        }

        scanner.close();

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : arr) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int mode = arr[0];
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mode = entry.getKey();
            }
        }

        System.out.println("众数为：" + mode + "，重数为：" + maxFrequency);
    }
}
