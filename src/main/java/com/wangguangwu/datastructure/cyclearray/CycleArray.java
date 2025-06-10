package com.wangguangwu.datastructure.cyclearray;

/**
 * 环形数组实现
 * <p>
 * 特点：
 * 1. 支持动态扩容和缩容
 * 2. 头尾操作均为O(1)时间复杂度
 * 3. 内存利用率高，可重复使用空间
 * 4. 线程不安全
 *
 * @param <E> 元素类型
 * @author wangguangwu
 */
public class CycleArray<E> {

    /**
     * 存储元素的数组
     */
    private E[] elements;

    /**
     * 队列头部索引（闭区间）
     */
    private int head;

    /**
     * 队列尾部索引（开区间）
     */
    private int tail;

    /**
     * 当前元素数量
     */
    private int size;

    /**
     * 数组容量
     */
    private int capacity;

    /**
     * 默认初始容量
     */
    private static final int DEFAULT_CAPACITY = 8;

    /**
     * 最小容量
     */
    private static final int MIN_CAPACITY = 4;

    /**
     * 使用默认容量创建环形数组
     */
    public CycleArray() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 使用指定容量创建环形数组
     *
     * @param initialCapacity 初始容量
     * @throws IllegalArgumentException 如果初始容量小于1
     */
    @SuppressWarnings("unchecked")
    public CycleArray(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("初始容量必须大于0");
        }

        this.capacity = Math.max(initialCapacity, MIN_CAPACITY);
        // 因为Java不支持直接创建泛型数组，所以这里使用了类型转换
        this.elements = (E[]) new Object[capacity];
        // head指向第一个有效元素的索引（闭区间）
        this.head = 0;
        // tail是开区间，指向最后一个有效元素的下一个位置
        this.tail = 0;
        this.size = 0;
    }

    /**
     * 自动调整数组容量
     *
     * @param newCapacity 新的容量大小
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        if (newCapacity < MIN_CAPACITY) {
            newCapacity = MIN_CAPACITY;
        }

        // 创建新的数组
        E[] newElements = (E[]) new Object[newCapacity];

        // 将旧数组的元素复制到新数组中
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % capacity];
        }

        // 更新引用和索引
        elements = newElements;
        head = 0;
        tail = size;
        capacity = newCapacity;
    }

    /**
     * 在数组头部添加元素，时间复杂度O(1)
     *
     * @param element 要添加的元素
     * @throws NullPointerException 如果元素为null
     */
    public void addFirst(E element) {
        checkNotNull(element);

        // 当数组满时，扩容为原来的两倍
        if (isFull()) {
            resize(capacity * 2);
        }

        // 因为head是闭区间，所以先左移，再赋值
        head = (head - 1 + capacity) % capacity;
        elements[head] = element;
        size++;
    }

    /**
     * 删除数组头部元素，时间复杂度O(1)
     *
     * @return 被删除的元素
     * @throws IllegalStateException 如果数组为空
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("数组为空");
        }

        // 保存要返回的元素
        E result = elements[head];

        // 清除引用，帮助GC
        elements[head] = null;

        // 因为head是闭区间，所以先赋值为null，再右移
        head = (head + 1) % capacity;
        size--;

        // 如果数组元素数量减少到容量的四分之一，则减小数组容量为一半
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }

        return result;
    }

    /**
     * 在数组尾部添加元素，时间复杂度O(1)
     *
     * @param element 要添加的元素
     * @throws NullPointerException 如果元素为null
     */
    public void addLast(E element) {
        checkNotNull(element);

        if (isFull()) {
            resize(capacity * 2);
        }

        // 因为tail是开区间，所以是先赋值，再右移
        elements[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
    }

    /**
     * 删除数组尾部元素，时间复杂度O(1)
     *
     * @return 被删除的元素
     * @throws IllegalStateException 如果数组为空
     */
    public E removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("数组为空");
        }

        // 因为tail是开区间，所以先左移
        tail = (tail - 1 + capacity) % capacity;

        // 保存要返回的元素
        E result = elements[tail];

        // 清除引用，帮助GC
        elements[tail] = null;
        size--;

        // 缩容
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }

        return result;
    }

    /**
     * 获取数组头部元素，时间复杂度O(1)
     *
     * @return 头部元素
     * @throws IllegalStateException 如果数组为空
     */
    public E getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("数组为空");
        }
        return elements[head];
    }

    /**
     * 获取数组尾部元素，时间复杂度O(1)
     *
     * @return 尾部元素
     * @throws IllegalStateException 如果数组为空
     */
    public E getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("数组为空");
        }
        // tail是开区间，指向的是下一个元素的位置，所以要减1
        return elements[(tail - 1 + capacity) % capacity];
    }

    /**
     * 获取指定索引位置的元素，时间复杂度O(1)
     *
     * @param index 索引位置
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public E get(int index) {
        checkElementIndex(index);
        return elements[(head + index) % capacity];
    }

    /**
     * 修改指定索引位置的元素，时间复杂度O(1)
     *
     * @param index   索引位置
     * @param element 新元素
     * @return 原来的元素
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws NullPointerException      如果新元素为null
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        checkNotNull(element);

        int actualIndex = (head + index) % capacity;
        E oldValue = elements[actualIndex];
        elements[actualIndex] = element;
        return oldValue;
    }

    /**
     * 检查数组是否已满
     *
     * @return 如果数组已满，返回true；否则返回false
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * 获取数组中元素的数量
     *
     * @return 元素数量
     */
    public int size() {
        return size;
    }

    /**
     * 获取数组的容量
     *
     * @return 数组容量
     */
    public int capacity() {
        return capacity;
    }

    /**
     * 检查数组是否为空
     *
     * @return 如果数组为空，返回true；否则返回false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 清空数组
     */
    public void clear() {
        // 清除所有元素引用，帮助GC
        for (int i = 0; i < size; i++) {
            elements[(head + i) % capacity] = null;
        }
        head = 0;
        tail = 0;
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
     * 检查索引是否在有效范围内
     *
     * @param index 要检查的索引
     * @throws IndexOutOfBoundsException 如果索引无效
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * 打印环形数组的状态，用于调试
     */
    public void display() {
        System.out.println("容量: " + capacity + ", 元素数量: " + size +
                ", 头部索引: " + head + ", 尾部索引: " + tail);
        System.out.print("元素: [");
        for (int i = 0; i < size; i++) {
            System.out.print(elements[(head + i) % capacity]);
            if (i < size - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        CycleArray<Integer> array = new CycleArray<>(4);

        System.out.println("=== 添加元素测试 ===");
        array.addLast(1);
        array.addLast(2);
        array.addLast(3);
        array.display();

        array.addFirst(0);
        array.display();

        System.out.println("=== 自动扩容测试 ===");
        array.addLast(4);
        array.display();

        System.out.println("=== 获取元素测试 ===");
        System.out.println("第一个元素: " + array.getFirst());
        System.out.println("最后一个元素: " + array.getLast());
        System.out.println("索引2的元素: " + array.get(2));

        System.out.println("=== 修改元素测试 ===");
        int oldVal = array.set(1, 100);
        System.out.println("原来的值: " + oldVal + ", 修改后: " + array.get(1));
        array.display();

        System.out.println("=== 删除元素测试 ===");
        System.out.println("删除的第一个元素: " + array.removeFirst());
        array.display();

        System.out.println("删除的最后一个元素: " + array.removeLast());
        array.display();

        System.out.println("=== 自动缩容测试 ===");
        array.removeFirst();
        array.removeFirst();  // 触发缩容
        array.display();

        System.out.println("=== 清空数组测试 ===");
        array.clear();
        System.out.println("数组是否为空: " + array.isEmpty());
        array.display();

        System.out.println("=== 重新添加元素 ===");
        array.addLast(5);
        array.addLast(6);
        array.display();
    }
}
