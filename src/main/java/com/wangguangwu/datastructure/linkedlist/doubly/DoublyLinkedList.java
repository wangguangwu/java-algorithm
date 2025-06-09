package com.wangguangwu.datastructure.linkedlist.doubly;

import java.util.NoSuchElementException;

/**
 * 双链表实现
 * <p>
 * 使用虚拟头尾节点简化边界情况处理
 * 线程不安全
 *
 * @param <E> 元素类型
 * @author wangguangwu
 */
public class DoublyLinkedList<E> {

    /**
     * 虚拟头尾节点
     */
    final private Node<E> head, tail;

    /**
     * 元素个数
     */
    private int size;

    /**
     * 双链表节点
     *
     * @param <E> 元素类型
     */
    private static class Node<E> {

        /**
         * 节点存储的值
         */
        E val;
        /**
         * 后继节点
         */
        Node<E> next;
        /**
         * 前驱节点
         */
        Node<E> prev;

        /**
         * 构造函数
         *
         * @param val 节点值
         */
        Node(E val) {
            this.val = val;
        }
    }

    /**
     * 构造函数初始化虚拟头尾节点
     */
    public DoublyLinkedList() {
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
        this.size = 0;
    }

    /**
     * 在链表末尾添加元素
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果元素为空
     */
    public void addLast(E e) {
        checkNotNull(e);
        linkBefore(e, tail);
    }

    /**
     * 在链表开头添加元素
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果元素为空
     */
    public void addFirst(E e) {
        checkNotNull(e);
        linkBefore(e, head.next);
    }

    /**
     * 在指定位置插入元素
     *
     * @param index   插入位置的索引
     * @param element 要插入的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws NullPointerException      如果元素为空
     */
    public void add(int index, E element) {
        checkPositionIndex(index);
        checkNotNull(element);
        
        if (index == size) {
            linkBefore(element, tail);
        } else {
            linkBefore(element, getNode(index));
        }
    }

    /**
     * 在指定节点之前插入新节点
     *
     * @param e    要插入的元素
     * @param succ 在此节点之前插入新节点
     */
    private void linkBefore(E e, Node<E> succ) {
        // 获取前驱节点
        Node<E> pred = succ.prev;
        // 创建新节点
        Node<E> newNode = new Node<>(e);
        
        // 更新引用关系
        newNode.prev = pred;
        newNode.next = succ;
        pred.next = newNode;
        succ.prev = newNode;
        
        size++;
    }

    /**
     * 删除并返回链表的第一个元素
     *
     * @return 被删除的元素
     * @throws NoSuchElementException 如果链表为空
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("链表为空");
        }
        return unlink(head.next);
    }

    /**
     * 删除并返回链表的最后一个元素
     *
     * @return 被删除的元素
     * @throws NoSuchElementException 如果链表为空
     */
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("链表为空");
        }
        return unlink(tail.prev);
    }

    /**
     * 删除指定位置的元素
     *
     * @param index 要删除元素的索引
     * @return 被删除的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(getNode(index));
    }

    /**
     * 从链表中移除指定节点
     *
     * @param x 要移除的节点
     * @return 被移除节点的值
     */
    private E unlink(Node<E> x) {
        E result = x.val;
        
        // 获取前后节点
        Node<E> prev = x.prev;
        Node<E> next = x.next;
        
        // 更新前后节点的引用
        prev.next = next;
        next.prev = prev;

        // 帮助GC
        x.prev = null;
        x.next = null;
        x.val = null;

        size--;
        return result;
    }

    /**
     * 获取指定位置的元素
     *
     * @param index 元素的索引
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E get(int index) {
        checkElementIndex(index);
        return getNode(index).val;
    }

    /**
     * 获取链表的第一个元素
     *
     * @return 链表的第一个元素
     * @throws NoSuchElementException 如果链表为空
     */
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("链表为空");
        }
        return head.next.val;
    }

    /**
     * 获取链表的最后一个元素
     *
     * @return 链表的最后一个元素
     * @throws NoSuchElementException 如果链表为空
     */
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("链表为空");
        }
        return tail.prev.val;
    }

    /**
     * 修改指定位置的元素
     *
     * @param index 要修改元素的索引
     * @param val   新的元素值
     * @return 原来的元素值
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws NullPointerException      如果元素为空
     */
    public E set(int index, E val) {
        checkElementIndex(index);
        checkNotNull(val);
        
        // 找到 index 对应的 Node
        Node<E> p = getNode(index);
        E oldVal = p.val;
        p.val = val;
        
        return oldVal;
    }

    /**
     * 返回链表中元素的个数
     *
     * @return 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 判断链表是否为空
     *
     * @return 如果链表为空，返回true；否则返回false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 检查元素是否为null
     *
     * @param e 要检查的元素
     * @throws NullPointerException 如果元素为null
     */
    private void checkNotNull(E e) {
        if (e == null) {
            throw new NullPointerException("元素不能为空");
        }
    }

    /**
     * 获取指定索引位置的节点
     * 优化了查找逻辑，根据索引位置决定从头部还是尾部开始查找
     *
     * @param index 节点的索引
     * @return 指定索引位置的节点
     */
    private Node<E> getNode(int index) {
        checkElementIndex(index);
        
        // 优化：根据索引位置决定从头部还是尾部开始查找
        if (index < (size >> 1)) {
            // 从头部开始查找
            Node<E> x = head.next;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        } else {
            // 从尾部开始查找
            Node<E> x = tail.prev;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
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
     * 检查索引是否在有效元素范围内，无效则抛出异常
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
     * 检查索引是否在有效位置范围内，无效则抛出异常
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
     * 打印链表信息，用于调试
     */
    public void display() {
        System.out.println("size = " + size);
        System.out.print("null <-> ");
        for (Node<E> p = head.next; p != tail; p = p.next) {
            System.out.print(p.val + " <-> ");
        }
        System.out.println("null");
        System.out.println();
    }

    /**
     * 清空链表
     */
    public void clear() {
        // 从头节点开始，逐个删除节点
        Node<E> x = head.next;
        while (x != tail) {
            Node<E> next = x.next;
            x.prev = null;
            x.next = null;
            x.val = null;
            x = next;
        }
        
        // 重置头尾节点的连接
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        
        System.out.println("=== 添加元素测试 ===");
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.display();
        
        System.out.println("=== 在头部添加元素 ===");
        list.addFirst(0);
        list.display();
        
        System.out.println("=== 在指定位置添加元素 ===");
        list.add(2, 9);
        list.display();
        
        System.out.println("=== 删除头部元素 ===");
        int first = list.removeFirst();
        System.out.println("删除的元素: " + first);
        list.display();
        
        System.out.println("=== 删除尾部元素 ===");
        int last = list.removeLast();
        System.out.println("删除的元素: " + last);
        list.display();
        
        System.out.println("=== 删除指定位置元素 ===");
        int removed = list.remove(1);
        System.out.println("删除的元素: " + removed);
        list.display();
        
        System.out.println("=== 获取元素 ===");
        System.out.println("第一个元素: " + list.getFirst());
        System.out.println("最后一个元素: " + list.getLast());
        System.out.println("索引1的元素: " + list.get(1));
        
        System.out.println("=== 修改元素 ===");
        int oldVal = list.set(0, 100);
        System.out.println("原来的值: " + oldVal + ", 修改后: " + list.get(0));
        list.display();
        
        System.out.println("=== 清空链表 ===");
        list.clear();
        System.out.println("链表是否为空: " + list.isEmpty());
        list.display();
        
        System.out.println("=== 重新添加元素 ===");
        list.addLast(5);
        list.addLast(6);
        list.display();
    }
}
