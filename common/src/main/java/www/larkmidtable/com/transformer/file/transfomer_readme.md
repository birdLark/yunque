# Transformer

## Transformer定义

在数据同步、传输过程中，存在用户对于数据传输进行特殊定制化的需求场景，包括裁剪列、转换列等工作，可以借助ETL的T过程实现(Transformer)。

## 目前已支持组件
1.dx_digest 加密组件

## UDF手册

1. dx_digest
* 参数：transformerParameterInfo
    * columnName：列名称 。
    * paras：第一个参数 hash类型，md5、sha1;
             第二个参数 hash值大小写 toUpperCase（大写）、toLowerCase（小写）
* 返回： 返回指定类型的hashHex,
* 举例：
```
dx_digest("md5","toUpperCase"), column 1的值为 xyzzzzz => 9CDFFC4FA4E45A99DB8BBCD762ACFFA2
```


## 配置文件定义
* 本例中，配置1个加密组件。

```
transformer:
  - {"name": "dx_digest","parameter":{ "columnName":"role_key","paras":["md5", "toLowerCase"] }}
```

