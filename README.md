## Pyke-Shieo：Pyke衍生Shiro反序列化利用工具

由于其他Shiro工具对复杂请求支持比较差，因此在ShiroAttack基础上编写该Shiro工具，并且从Pyke中拆分出来作为单独工具使用。

## 打包

maven：

```
mvn package assembly:single
```

## 更新记录
- v0.2 (2024-03-07)

      修复bug：修复批量检测时请求头、请求体信息没带入数据包。

- v0.1 (2024-03-01)

      发布初版，实现基本功能。

## 使用
复杂请求可以是GET或POST，所有请求信息会被携带，可指定https

![image](https://github.com/sma11new/Pyke-Shiro/assets/53944964/03f3826e-6e2b-4397-b975-56732a84c972)

欢迎提issues完善工具。

参考：https://github.com/SummerSec/ShiroAttack2
