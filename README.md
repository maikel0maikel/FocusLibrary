# FocusLibrary
按鍵统一处理
思想从ViewGroup中按顺序添加有设置onClickListener的View做成双向循环列表
按上下键相应的View会获取焦点，按确认键会执行onClick事件
使用步骤
一、在根目录的build.gradle里添加仓库。
```groovy
allprojects {
 repositories {
        maven {
             url "https://raw.githubusercontent.com/maikel0maikel/FocusLibrary/master"
         }
 }
```
二、在模块的build.gradle中添加依赖。

studio3.0以下
```groovy
dependencies {
     compile 'com.sinohb.lib:keyeventhandle:1.1.0'
}
```
 studio3.0以上（含3.0）
 ```groovy
 dependencies {
      implementation 'com.sinohb.lib:keyeventhandle:1.1.0'
 }
```
三、activity中使用继承BaseFoucsHandlerActivity
```java
public class MainActivity extends BaseFoucsHandlerActivity

  @Override
    public ViewGroup getContentViewGroup() {
        return null;//返回一个根ViewGroup activity可以不用返回
    }

    @Override
    protected void onStart() {
        super.onStart();
        //按钮顺序，按需求的顺序添加即可
        //复杂的布局中可以如下使用
        addPreparedFocusView(btn1);
        addPreparedFocusView(btn2);
        addPreparedFocusView(btn3);
        addPreparedFocusView(listView);
        addPreparedFocusView(RecycleView);
        .....

        startFocusPosition(0);//如果调用此方法则从加入顺序的第一个获取焦点，如果不调用此方法进去界面第一个不显示焦点需要按一次物理按键

        //简单的布局可以如下使用：
        addFocusView(ViewGroup);
        //或者使用addFilterListenerView
        addFilterListenerView(ViewGroup);//该方法会过滤没有Listener的View
    }
```
二、fragment中使用继承BaseFocusFragment（偏好设置的继承BaseFocusPreferenceFragment）
```java
         @Override
        public ViewGroup getContentViewGroup() {
            return null;//返回一个根ViewGroup
        }
  @Override
    protected void onStart() {
        super.onStart();
        //按钮顺序，按需求的顺序添加即可
        //复杂的布局中可以如下使用
        addPreparedFocusView(btn1);
        addPreparedFocusView(btn2);
        addPreparedFocusView(btn3);
        addPreparedFocusView(listView);
        addPreparedFocusView(RecycleView);
        .....

        startFocusPosition(0);//如果调用此方法则从加入顺序的第一个获取焦点，如果不调用此方法进去界面第一个不显示焦点需要按一次物理按键

        //简单的布局可以如下使用：
        addFocusView(ViewGroup);
        //或者使用addFilterListenerView
        addFilterListenerView(ViewGroup);//该方法会过滤没有Listener的View
    }
```
三、popupWindow中使用继承BaseFocusPopupWindow
```java
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setKeyListener();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void setKeyListener() {
        super.setKeyListener();
         //按钮顺序，按需求的顺序添加即可
           //复杂的布局中可以如下使用
           addPreparedFocusView(layout1);
           addPreparedFocusView(layout2);
           addPreparedFocusView(layout3);
           .....
    }
  ```

