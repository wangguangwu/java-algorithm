package com.wangguangwu.algorithm.bfs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 752. 打开转盘锁
 *
 * <p>你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'。
 * 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9'。每次旋转只能旋转一个拨轮的一位数字。</p>
 *
 * <p>锁的初始数字为 '0000'，一个代表四个拨轮的数字的字符串。</p>
 *
 * <p>列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。</p>
 *
 * <p>字符串 target 代表可以解锁的数字，请给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1。</p>
 *
 * <p><a href="https://leetcode.cn/problems/open-the-lock/description/">752. 打开转盘锁</a></p>
 *
 * @author wangguangwu
 */
public class OpenTheLock {

    public int openLock(String[] deadends, String target) {
        // 记录需要跳过的死亡密码
        Set<String> deadSet = Arrays.stream(deadends).collect(Collectors.toSet());
        // 记录已经穷举过的密码，防止走回头路
        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        // 从起点开始启动 BFS
        int step = 0;
        queue.offer("0000");
        visited.add("0000");

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String cur = queue.poll();

                // 判断是否到达终点
                // 如果当前状态在 deadSet 中，会跳过当前状态，也不会将其相邻状态添加到队列中
                if (deadSet.contains(cur)) {
                    continue;
                }
                if (cur.equals(target)) {
                    return step;
                }
                // 将一个节点的未遍历相邻节点加入队列
                for (int j = 0; j < 4; j++) {
                    String up = plusOne(cur, j);
                    if (!visited.contains(up)) {
                        queue.offer(up);
                        visited.add(up);
                    }
                    String down = minusOne(cur, j);
                    if (!visited.contains(down)) {
                        queue.offer(down);
                        visited.add(down);
                    }
                }
            }
            step++;
        }
        return -1;
    }

    // 将 s[j] 向上拨动一次
    String plusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '9') {
            ch[j] = '0';
        } else {
            ch[j] += 1;
        }
        return new String(ch);
    }

    // 将 s[i] 向下拨动一次
    String minusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '0') {
            ch[j] = '9';
        } else {
            ch[j] -= 1;
        }
        return new String(ch);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        OpenTheLock solution = new OpenTheLock();

        // 测试用例1：正常情况
        String[] deadends1 = {"8888"};
        String target1 = "0009";
        System.out.println("测试用例1结果: " + solution.openLock(deadends1, target1));
        // 预期输出: 1

        // 测试用例2：起始状态是死亡数字
        String[] deadends2 = {"0000"};
        String target2 = "8888";
        System.out.println("测试用例2结果: " + solution.openLock(deadends2, target2));
        // 预期输出: -1

        // 测试用例3：LeetCode示例
        String[] deadends3 = {"0201", "0101", "0102", "1212", "2002"};
        String target3 = "0202";
        System.out.println("测试用例3结果: " + solution.openLock(deadends3, target3));
        // 预期输出: 6

        // 测试用例4：无法到达目标
        String[] deadends4 = {"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"};
        String target4 = "8888";
        System.out.println("测试用例4结果: " + solution.openLock(deadends4, target4));
        // 预期输出: -1
    }
}
