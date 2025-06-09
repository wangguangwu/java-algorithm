package com.wangguangwu.datastructure.arraylist;

import java.util.Arrays;

/**
 * 实现动态数组
 * <p>
 * 该实现模拟 ArrayList 的核心功能，包括动态扩容和缩容
 * 线程不安全
 *
 * @param <E> 元素类型
 * @author wangguangwu
 */
public class MyArrayList<E> {

    /**
     * 存储数据的底层数组
     */
    private E[] data;

    /**
     * 当前元素个数
     */
    private int size;

    /**
     * 默认初始容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 默认构造函数，使用默认容量初始化数组
     */
    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 指定初始容量的构造函数
     *
     * @param capacity 初始容量
     */
    @SuppressWarnings("unchecked")
    public MyArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.data = (E[]) new Object[capacity];
        this.size = 0;
    }

    /**
     * 在数组末尾添加元素
     *
     * @param e 要添加的元素
     */
    public void addLast(E e) {
        add(size, e);
    }

    /**
     * 在指定位置插入元素
     *
     * @param index 插入位置
     * @param e     要插入的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public void add(int index, E e) {
        // 判断index是否合法
        checkPositionIndex(index);
        // 判断是否需要扩容
        ensureCapacity(size + 1);
        // 将元素后移
        System.arraycopy(data, index, data, index + 1, size - index);
        // 插入元素
        data[index] = e;
        // 更新元素个数
        size++;
    }

    /**
     * 在数组开头添加元素
     *
     * @param e 要添加的元素
     */
    public void addFirst(E e) {
        add(0, e);
    }

    /**
     * 删除并返回数组末尾的元素
     *
     * @return 被删除的元素
     * @throws IllegalArgumentException 如果数组为空
     */
    public E removeLast() {
        if (size == 0) {
            throw new IllegalArgumentException("List is empty");
        }
        return remove(size - 1);
    }

    /**
     * 删除并返回指定位置的元素
     *
     * @param index 要删除的元素的索引
     * @return 被删除的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E remove(int index) {
        // 判断index是否合法
        checkElementIndex(index);
        E deletedVal = data[index];

        // 将元素前移
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        // 清空最后一个元素的引用，帮助GC
        data[size - 1] = null;
        size--;

        // 判断是否需要缩容
        if (size > 0 && size == data.length / 4) {
            resize(data.length / 2);
        }

        return deletedVal;
    }

    /**
     * 删除并返回数组开头的元素
     *
     * @return 被删除的元素
     * @throws IllegalArgumentException 如果数组为空
     */
    public E removeFirst() {
        if (size == 0) {
            throw new IllegalArgumentException("List is empty");
        }
        return remove(0);
    }

    /**
     * 获取指定位置的元素
     *
     * @param index 元素的索引
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E get(int index) {
        // 检查索引越界
        checkElementIndex(index);
        return data[index];
    }

    /**
     * 修改指定位置的元素
     *
     * @param index   要修改的元素的索引
     * @param element 新的元素值
     * @return 原来的元素值
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E set(int index, E element) {
        // 检查索引越界
        checkElementIndex(index);
        // 修改数据
        E oldVal = data[index];
        data[index] = element;
        return oldVal;
    }

    /**
     * 返回数组中元素的个数
     *
     * @return 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 判断数组是否为空
     *
     * @return 如果数组为空，返回true；否则返回false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 确保容量足够，如果不够则进行扩容
     *
     * @param minCapacity 所需的最小容量
     */
    private void ensureCapacity(int minCapacity) {
        // 如果当前容量不足，进行扩容
        if (minCapacity > data.length) {
            int newCapacity = Math.max(data.length * 2, minCapacity);
            resize(newCapacity);
        }
    }

    /**
     * 调整数组容量
     *
     * @param newCapacity 新的容量
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] temp = (E[]) new Object[newCapacity];
        // 复制原数组中的元素到新数组
        System.arraycopy(data, 0, temp, 0, size);
        data = temp;
    }

    /**
     * 判断索引是否在有效元素范围内
     *
     * @param index 要检查的索引
     * @return 如果索引有效，返回true；否则返回false
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * 判断索引是否在有效位置范围内（包括size位置）
     *
     * @param index 要检查的索引
     * @return 如果索引有效，返回true；否则返回false
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * 检查元素索引是否有效，无效则抛出异常
     *
     * @param index 要检查的索引
     * @throws IndexOutOfBoundsException 如果索引无效
     */
    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 检查位置索引是否有效，无效则抛出异常
     *
     * @param index 要检查的索引
     * @throws IndexOutOfBoundsException 如果索引无效
     */
    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 打印数组信息，用于调试
     */
    public void display() {
        System.out.println("size: " + size + ", capacity: " + data.length);
        System.out.println(Arrays.toString(Arrays.copyOf(data, size)));
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        MyArrayList<Integer> arrayList = new MyArrayList<>(3);
        System.out.println("=== 添加元素测试 ===");
        for (int i = 0; i <= 5; i++) {
            arrayList.addLast(i);
            arrayList.display();
        }

        System.out.println("\n=== 删除元素测试 ===");
        arrayList.remove(3);
        arrayList.display();

        System.out.println("\n=== 插入元素测试 ===");
        arrayList.add(1, 9);
        arrayList.display();

        System.out.println("\n=== 在头部添加元素 ===");
        arrayList.addFirst(100);
        arrayList.display();

        System.out.println("\n=== 删除尾部元素 ===");
        int val = arrayList.removeLast();
        System.out.println("移除的元素: " + val);
        arrayList.display();

        System.out.println("\n=== 遍历元素 ===");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println("元素[" + i + "]: " + arrayList.get(i));
        }
    }
}
