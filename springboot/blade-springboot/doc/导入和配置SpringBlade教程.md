## IDEA导入SpringBlade工程

远程仓库地址: https://gitee.com/qwqweqwe/blade-springboot.git

## 修改密钥配置

1. 打开 application.yml，可以看到需要配置两个key
   ![img](imgs\9dde353d41b32f0e2c9e186d49be234d_2332x1634.png)
2. 我们打开sm2Key生成器，执行后复制key到配置文件（请不要复制本文档的key，请务必亲自通过生成器生成，否则容易被人用这个key渗透攻击）
   ![img](imgs\f08019eeb4d055bf23a51050fed24738_3204x1840.png)
   ![img](imgs\2779cd43503bf5482b6e7a1c76e8ca8b_1614x1626.png)
3. 我们打开signKey生成器，执行后复制key到配置文件（请不要复制本文档的key，请务必亲自通过生成器生成，否则容易被人用这个key渗透攻击）
   ![img](imgs\f50b620bd915e595d764c3629951df32_2406x1502.png)
4. 前端(客户端)也需要将这个同样的key配置到对应的参数
   ![img](D:\gitee\SpringBlade\doc\imgs\1fc39e102c3230be771bef86718978fe_1902x1230.png)