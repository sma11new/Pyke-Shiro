## Pyke-Shiro：复杂请求下的Shiro反序列化利用工具

由于其他Shiro工具对复杂请求支持比较差，因此在ShiroAttack基础上编写该Shiro工具，并且从Pyke中拆分出来作为单独工具使用。

## 打包

maven：

```
mvn package assembly:single
```

## 更新记录
- v0.3 (2024-03-12)

      新增可选保留Cookie：复杂请求下可选择是否保留原始数据包中的Cookie内容
      新增自定义请求超时，在“设置-超时”菜单中
      修改命令执行时的参数放在Authorizations而不是Authorization，避免与身份认证冲突

- v0.2 (2024-03-07)

      修复bug：修复批量检测时请求头、请求体信息没带入数据包。

- v0.1 (2024-03-01)

      发布初版，实现基本功能。

## 使用
复杂请求可以是GET或POST，所有请求信息会被携带，可指定https，同时可选择是否保留原始数据包中的Cookie内容

![image_2024-03-12_15-38-59](https://github.com/sma11new/Pyke-Shiro/assets/53944964/d16a17a4-d286-48dd-908e-ac9ea7dc9f9e)


欢迎提issues完善工具。

参考：https://github.com/SummerSec/ShiroAttack2

## 免责声明

本工具仅用于内部网络安全自查及授权项目，请勿非法使用，否则后果自负，使用前请认真阅读相关协议。

![image](https://github.com/sma11new/Pyke-Shiro/assets/53944964/e1f4d4a1-ec26-4b20-8882-209799676b47)
