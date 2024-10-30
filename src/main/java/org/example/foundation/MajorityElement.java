package org.example.foundation;

import java.util.Scanner;

public class MajorityElement {

    // 分治法找到众数
    public static int majorityElement(int[] nums, int left, int right) {
        // 基本情况：如果区间内只有一个元素，则它本身是众数
        if (left == right) {
            return nums[left];
        }

        // 分割数组为两半
        int mid = (left + right) / 2;
        int leftMajority = majorityElement(nums, left, mid);
        int rightMajority = majorityElement(nums, mid + 1, right);

        // 如果两半的众数相同，那么它就是整个区间的众数
        if (leftMajority == rightMajority) {
            return leftMajority;
        }

        // 否则，计算两边的众数的出现次数，返回出现次数较多的那个
        int leftCount = countInRange(nums, leftMajority, left, right);
        int rightCount = countInRange(nums, rightMajority, left, right);

        return leftCount > rightCount ? leftMajority : rightMajority;
    }

    // 计算指定元素在区间内的出现次数
    private static int countInRange(int[] nums, int num, int left, int right) {
        int count = 0;
        for (int i = left; i <= right; i++) {
            if (nums[i] == num) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入多重集合的元素，以空格隔开：");
        String[] input = scanner.nextLine().split(" ");
        int n = input.length;
        int[] nums = new int[n];

        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(input[i]);
        }

        scanner.close();

        int majorityElement = majorityElement(nums, 0, nums.length - 1);
        int count = countInRange(nums, majorityElement, 0, nums.length - 1);

        System.out.println("众数为：" + majorityElement + "，重数为：" + count);
    }
}