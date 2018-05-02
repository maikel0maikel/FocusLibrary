package com.sinohb.lib.keyeventhandle;


import com.sinohb.lib.keyeventhandle.bean.FocusView;

public class LinkedFocusViewStack<T> {
    private FocusView header;//header
    private int size = 0;//长度

    public LinkedFocusViewStack() {
        header = new FocusView(null);
        header.mUpFocusView = header.mDownFocusView = header;//head and tail connect
    }

    public boolean add(T value) {
        return add(header, value);
    }

    public boolean add(int index, T value) {
        return add(entry(index), value);
    }

    private FocusView<T> entry(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index is wrong please check");
        }
        FocusView<T> node = this.header;
        if (index < (this.size >> 1)) {//half
            for (int i = 0; i <= index; i++) {
                node = node.mDownFocusView;
            }
        } else {
            for (int i = this.size - 1; i >= index; i--) {
                node = node.mUpFocusView;
            }
        }
        return node;
    }

    private boolean add(FocusView<T> node, T value) {
        //新建一个节点，新节点的next指向node，新节点的pre指向node的pre
        //完成指向过程node.pre←newNode→node
        FocusView<T> newNode = new FocusView<T>(value, node.mUpFocusView, node);
        //维持双向链表的指向，将node的pre节点的next指向新节点,完成指向过程node.pre→newNode
        node.mUpFocusView.mDownFocusView = newNode;
        //node节点的前一个节点指向新节点，完成指向过程newNode←node
        node.mUpFocusView = newNode;
        //上面两行代码不能颠倒，否则node的前一个节点会被覆盖成新节点，会丢失node原来的前一个节点的next指向
        //上述代码完成了在node节点和node前一个节点之间加入一个新节点，并维护了双向关系
        this.size++;
        return true;
    }

    public FocusView<T> get(int index) {
        return entry(index);
    }

    public FocusView<T> getHeader() {
        return header;
    }

    private T remove(FocusView<T> node) {
        //node的前一个节点next指向node的下一个节点
        //node的下一个节点pre指向node的前一个节点
        //A→node←B改成A→←B
        node.mUpFocusView.mDownFocusView = node.mDownFocusView;
        node.mDownFocusView.mUpFocusView = node.mUpFocusView;
        //node的前后指向null
        //A←node→B改成null←node→null
        node.mUpFocusView = node.mDownFocusView = null;
        T value = node.mFocusView;
        node.mFocusView = null;
        this.size--;
        return value;
    }

    public void clear() {
        FocusView<T> node = header.mDownFocusView;
        //将每一个节点的双向指向都清空，这样每个节点都没有被引用，可以方便垃圾回收器回收内存
        while (node != header) {
            //将node的下一个节点临时保存起来
            FocusView<T> tempNode = node.mDownFocusView;
            //将node的下一个节点和上一个节点置空
            node.mDownFocusView = node.mUpFocusView = null;
            //将node的值也置空
            node.mFocusView = null;
            //将node移动到下一个节点
            node = tempNode;
        }
        //清空header的双向指向null
        this.header.mDownFocusView = this.header.mUpFocusView = this.header;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }
}
