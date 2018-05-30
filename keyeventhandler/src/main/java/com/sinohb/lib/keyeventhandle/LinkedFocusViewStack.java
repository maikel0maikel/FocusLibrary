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

    public boolean add(FocusView<T> node, T value) {
        FocusView<T> newNode = new FocusView<T>(value, node.mUpFocusView, node,size);
        node.mUpFocusView.mDownFocusView = newNode;
        node.mUpFocusView = newNode;
        this.size++;
        return true;
    }
    public int remove(T obj)//删除指定value的节点
    {
        FocusView<T> node;
//        for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
//        {
//            if(node.mFocusView == obj || (obj!=null && obj.equals(node.mFocusView)))
//            {
//                remove(node);
//                return true;
//            }
//        }
        int index = 0;
        if(obj==null)
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                index++;
                if(node.mFocusView == null)
                {
                    remove(node);
                    return index;
                }
            }
        }
        else
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                index++;
                if(node.mFocusView == obj || obj.equals(node.mFocusView))
                {
                    remove(node);
                    return index;
                }
            }
        }

        return -1;
    }

    public int findFocusView(T obj){
        FocusView<T> node;
        int index = 0;
        if(obj==null)
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                index++;
                if(node.mFocusView == null)
                {
                    return index;
                }
            }
        }
        else
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                index++;
                if(node.mFocusView == obj || obj.equals(node.mFocusView))
                {
                    return index;
                }
            }
        }

        return -1;
    }

    public FocusView getFocusView(T obj){
        FocusView<T> node;
        if(obj==null)
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                if(node.mFocusView == null)
                {
                    return node;
                }
            }
        }
        else
        {
            for(node = header.mDownFocusView; node!=header; node=node.mDownFocusView)
            {
                if(node.mFocusView == obj || obj.equals(node.mFocusView))
                {
                    return node;
                }
            }
        }

        return null;
    }


    public FocusView<T> get(int index) {
        return entry(index);
    }

    public FocusView<T> getHeader() {
        return header;
    }

    public T remove(FocusView<T> node) {
        node.mUpFocusView.mDownFocusView = node.mDownFocusView;
        node.mDownFocusView.mUpFocusView = node.mUpFocusView;
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
