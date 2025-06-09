# Java 算法刷题记录

这个项目用于记录每日算法刷题代码，按照不同的算法类型进行分类整理。

## 项目结构

```
src/main/java/com/wangguangwu/algorithm/
├── array       // 数组相关题目
├── string      // 字符串相关题目
├── linkedlist  // 链表相关题目
├── tree        // 树相关题目
├── graph       // 图相关题目
├── dp          // 动态规划相关题目
├── greedy      // 贪心算法相关题目
├── search      // 搜索算法相关题目
├── sort        // 排序算法相关题目
├── math        // 数学相关题目
├── design      // 设计类题目
└── daily       // 每日一题
```

## 使用方法

1. 在相应的分类目录下创建Java类文件，命名可以使用题目编号+题目名称的方式
2. 在类中实现算法解法，并添加必要的注释说明题目来源、难度、思路等
3. 可以使用JUnit编写测试用例验证算法正确性

## 示例

```java
package com.wangguangwu.algorithm.array;

/**
 * LeetCode 1. 两数之和
 * 难度：简单
 * 
 * 题目描述：
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。
 */
public class TwoSum {
    
    public int[] twoSum(int[] nums, int target) {
        // 算法实现
        // ...
        return new int[]{0, 1}; // 示例返回值
    }
    
    // 可以添加多个解法
    public int[] twoSumOptimized(int[] nums, int target) {
        // 优化后的算法实现
        // ...
        return new int[]{0, 1}; // 示例返回值
    }
}
```
