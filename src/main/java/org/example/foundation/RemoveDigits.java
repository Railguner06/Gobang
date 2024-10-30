package org.example.foundation;

import java.util.Scanner;
import java.util.Stack;

public class RemoveDigits {
    public static String removeKDigits(String num, int k) {
        Stack<Character> stack = new Stack<>();

        for (char digit : num.toCharArray()) {
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }

        // 如果k大于0，则继续从栈中移除多余的数字
        while (k > 0) {
            stack.pop();
            k--;
        }

        // 构建最终的结果
        StringBuilder result = new StringBuilder();
        for (char digit : stack) {
            result.append(digit);
        }

        // 去除前导零
        while (result.length() > 1 && result.charAt(0) == '0') {
            result.deleteCharAt(0);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个正整数：");
        String num = scanner.nextLine();
        System.out.println("请输入要删除的数字个数：");
        int k = scanner.nextInt();
        scanner.close();

        String smallestNumber = removeKDigits(num, k);
        System.out.println("删除后的最小数为：" + smallestNumber);
    }
}
