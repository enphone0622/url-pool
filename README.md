# 域名服务
接收长域名生成短域名，接收短域名返回长域名
* 通过对URL的哈希值转换为62进制数作为其短地址放入缓存
* 接收短地址从缓存中取出返回原生地址
* 对HashMap添加线程安全和容量控制作为缓存存储
* ![Swagger截图1](https://github.com/enphone0622/url-pool/blob/main/src/main/resources/snapshot/swagger1.png)
