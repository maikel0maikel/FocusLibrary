# FocusLibrary
按鍵统一处理
思想从ViewGroup中按顺序添加有设置onClickListener的View做成双向循环列表
按上下键相应的View会获取焦点，按确认键会执行onClick事件
使用步骤
一、activity中继承BaseFoucsHandlerActivity
```java
public class MainActivity extends BaseFoucsHandlerActivity



