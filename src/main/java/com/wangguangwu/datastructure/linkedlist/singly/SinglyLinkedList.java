package com.wangguangwu.datastructure.linkedlist.singly;

import java.util.NoSuchElementException;

/**
 * 单链表实现
 * <p>
 * 使用虚拟头节点简化边界情况处理
 * 维护尾节点引用提高尾部操作效率
 * 线程不安全
 *
 * @param <E> 元素类型
 * @author wangguangwu
 */
public class SinglyLinkedList<E> {

    /**
     * 链表节点类
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
         * 构造函数
         *
         * @param val 节点值
         */
        Node(E val) {
            this.val = val;
            this.next = null;
        }
    }

    /**
     * 虚拟头节点
     */
    private final Node<E> head;
    
    /**
     * 尾节点引用，提高尾部操作效率
     */
    private Node<E> tail;
    
    /**
     * 元素个数
     */
    private int size;

    /**
     * 构造函数，初始化空链表
     */
    public SinglyLinkedList() {
        this.head = new Node<>(null);
        this.tail = head;
        this.size = 0;
    }

    /**
     * 在链表头部添加元素
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果元素为空
     */
    public void addFirst(E e) {
        checkNotNull(e);
        
        Node<E> newNode = new Node<>(e);
        newNode.next = head.next;
        head.next = newNode;
        
        // 如果是第一个元素，需要更新尾节点
        if (size == 0) {
            tail = newNode;
        }
        size++;
    }

    /**
     * 在链表尾部添加元素
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果元素为空
     */
    public void addLast(E e) {
        checkNotNull(e);
        
        Node<E> newNode = new Node<>(e);
        tail.next = newNode;
        tail = newNode;
        size++;
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
            addLast(element);
            return;
        }

        // 找到前驱节点
        Node<E> pred = getPredNode(index);
        
        // 创建新节点并插入
        Node<E> newNode = new Node<>(element);
        newNode.next = pred.next;
        pred.next = newNode;
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
        
        Node<E> first = head.next;
        head.next = first.next;
        
        // 如果删除后链表为空，需要更新尾节点
        if (size == 1) {
            tail = head;
        }
        
        // 保存结果并帮助GC
        E result = first.val;
        first.next = null;
        first.val = null;
        
        size--;
        return result;
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

        // 找到尾节点的前驱
        Node<E> prev = head;
        while (prev.next != tail) {
            prev = prev.next;
        }
        
        // 保存结果
        E result = tail.val;
        
        // 更新尾节点
        prev.next = null;
        tail = prev;
        
        size--;
        return result;
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

        // 找到前驱节点
        Node<E> pred = getPredNode(index);
        Node<E> nodeToRemove = pred.next;
        
        // 更新引用
        pred.next = nodeToRemove.next;
        
        // 如果删除的是最后一个元素，需要更新尾节点
        if (index == size - 1) {
            tail = pred;
        }
        
        // 保存结果并帮助GC
        E result = nodeToRemove.val;
        nodeToRemove.next = null;
        nodeToRemove.val = null;
        
        size--;
        return result;
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
        return tail.val;
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
     * 修改指定位置的元素
     *
     * @param index   要修改元素的索引
     * @param element 新的元素值
     * @return 原来的元素值
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws NullPointerException      如果元素为空
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        checkNotNull(element);
        
        Node<E> node = getNode(index);
        E oldVal = node.val;
        node.val = element;
        
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
     * 清空链表
     */
    public void clear() {
        // 从头节点开始，逐个删除节点
        Node<E> x = head.next;
        while (x != null) {
            Node<E> next = x.next;
            x.val = null;
            x.next = null;
            x = next;
        }
        
        // 重置头尾节点的连接
        head.next = null;
        tail = head;
        size = 0;
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
     * 获取指定索引位置的节点
     *
     * @param index 节点的索引
     * @return 指定索引位置的节点
     */
    private Node<E> getNode(int index) {
        checkElementIndex(index);
        
        Node<E> p = head.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p;
    }
    
    /**
     * 获取指定索引位置节点的前驱节点
     * 注意：返回的是索引位置节点的前一个节点
     *
     * @param index 节点的索引
     * @return 指定索引位置节点的前驱节点
     */
    private Node<E> getPredNode(int index) {
        // 索引检查由调用方负责
        
        Node<E> pred = head;
        for (int i = 0; i < index; i++) {
            pred = pred.next;
        }
        return pred;
    }

    /**
     * 打印链表信息，用于调试
     */
    public void display() {
        System.out.println("size = " + size);
        System.out.print("head -> ");
        for (Node<E> p = head.next; p != null; p = p.next) {
            System.out.print(p.val + " -> ");
        }
        System.out.println("null");
        System.out.println();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        
        System.out.println("=== 添加元素测试 ===");
        list.addFirst(1);
        list.addFirst(2);
        list.display();
        
        list.addLast(3);
        list.addLast(4);
        list.display();
        
        list.add(2, 5);
        list.display();
        
        System.out.println("=== 删除元素测试 ===");
        System.out.println("删除的第一个元素: " + list.removeFirst());
        list.display();
        
        System.out.println("删除的最后一个元素: " + list.removeLast());
        list.display();
        
        System.out.println("删除索引1的元素: " + list.remove(1));
        list.display();
        
        System.out.println("=== 获取元素测试 ===");
        System.out.println("第一个元素: " + list.getFirst());
        System.out.println("最后一个元素: " + list.getLast());
        System.out.println("索引1的元素: " + list.get(1));
        
        System.out.println("=== 修改元素测试 ===");
        int oldVal = list.set(0, 100);
        System.out.println("原来的值: " + oldVal + ", 修改后: " + list.get(0));
        list.display();
        
        System.out.println("=== 清空链表测试 ===");
        list.clear();
        System.out.println("链表是否为空: " + list.isEmpty());
        list.display();
        
        System.out.println("=== 重新添加元素 ===");
        list.addLast(5);
        list.addLast(6);
        list.display();
    }
}
