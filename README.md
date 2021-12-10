# WordHelper

## 介绍
文本操作工具，引用工具包：[https://github.com/hankcs/HanLP](https://github.com/hankcs/HanLP)

```
<dependency>
    <groupId>com.hankcs</groupId>
    <artifactId>hanlp</artifactId>
    <version>portable-1.8.2</version>
</dependency>
```

HanLP是一系列模型与算法组成的NLP工具包，目标是普及自然语言处理在生产环境中的应用。HanLP具备功能完善、性能高效、架构清晰、语料时新、可自定义的特点。内部算法经过工业界和学术界考验，配套书籍[《自然语言处理入门》](http://nlp.hankcs.com/book.php)已经出版。目前，基于深度学习的[HanLP 2.x](https://github.com/hankcs/HanLP/tree/doc-zh)已正式发布，次世代最先进的NLP技术，支持包括简繁中英日俄法德在内的104种语言上的联合任务。

HanLP提供下列功能：

* 中文分词
    * HMM-Bigram（速度与精度最佳平衡；一百兆内存）
        * [最短路分词](https://github.com/hankcs/HanLP/tree/1.x#1-%E7%AC%AC%E4%B8%80%E4%B8%AAdemo)、[N-最短路分词](https://github.com/hankcs/HanLP/tree/1.x#5-n-%E6%9C%80%E7%9F%AD%E8%B7%AF%E5%BE%84%E5%88%86%E8%AF%8D)
    * 由字构词（侧重精度，全世界最大语料库，可识别新词；适合NLP任务）
        * [感知机分词](https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6)、[CRF分词](https://github.com/hankcs/HanLP/tree/1.x#6-crf%E5%88%86%E8%AF%8D)
    * 词典分词（侧重速度，每秒数千万字符；省内存）
        * [极速词典分词](https://github.com/hankcs/HanLP/tree/1.x#7-%E6%9E%81%E9%80%9F%E8%AF%8D%E5%85%B8%E5%88%86%E8%AF%8D)
    * 所有分词器都支持：
        * [索引全切分模式](https://github.com/hankcs/HanLP/tree/1.x#4-%E7%B4%A2%E5%BC%95%E5%88%86%E8%AF%8D)
        * [用户自定义词典](https://github.com/hankcs/HanLP/tree/1.x#8-%E7%94%A8%E6%88%B7%E8%87%AA%E5%AE%9A%E4%B9%89%E8%AF%8D%E5%85%B8)
        * [兼容繁体中文](https://github.com/hankcs/HanLP/blob/1.x/src/test/java/com/hankcs/demo/DemoPerceptronLexicalAnalyzer.java#L29)
        * [训练用户自己的领域模型](https://github.com/hankcs/HanLP/wiki)
* 词性标注
    * [HMM词性标注](https://github.com/hankcs/HanLP/blob/1.x/src/main/java/com/hankcs/hanlp/seg/Segment.java#L584)（速度快）
    * [感知机词性标注](https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6)、[CRF词性标注](https://github.com/hankcs/HanLP/wiki/CRF%E8%AF%8D%E6%B3%95%E5%88%86%E6%9E%90)（精度高）
* 命名实体识别
    * 基于HMM角色标注的命名实体识别 （速度快）
        * [中国人名识别](https://github.com/hankcs/HanLP/tree/1.x#9-%E4%B8%AD%E5%9B%BD%E4%BA%BA%E5%90%8D%E8%AF%86%E5%88%AB)、[音译人名识别](https://github.com/hankcs/HanLP/tree/1.x#10-%E9%9F%B3%E8%AF%91%E4%BA%BA%E5%90%8D%E8%AF%86%E5%88%AB)、[日本人名识别](https://github.com/hankcs/HanLP/tree/1.x#11-%E6%97%A5%E6%9C%AC%E4%BA%BA%E5%90%8D%E8%AF%86%E5%88%AB)、[地名识别](https://github.com/hankcs/HanLP/tree/1.x#12-%E5%9C%B0%E5%90%8D%E8%AF%86%E5%88%AB)、[实体机构名识别](https://github.com/hankcs/HanLP/tree/1.x#13-%E6%9C%BA%E6%9E%84%E5%90%8D%E8%AF%86%E5%88%AB)
    * 基于线性模型的命名实体识别（精度高）
        * [感知机命名实体识别](https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6)、[CRF命名实体识别](https://github.com/hankcs/HanLP/wiki/CRF%E8%AF%8D%E6%B3%95%E5%88%86%E6%9E%90)
* 关键词提取
    * [TextRank关键词提取](https://github.com/hankcs/HanLP/tree/1.x#14-%E5%85%B3%E9%94%AE%E8%AF%8D%E6%8F%90%E5%8F%96)
* 自动摘要
    * [TextRank自动摘要](https://github.com/hankcs/HanLP/tree/1.x#15-%E8%87%AA%E5%8A%A8%E6%91%98%E8%A6%81)
* 短语提取
    * [基于互信息和左右信息熵的短语提取](https://github.com/hankcs/HanLP/tree/1.x#16-%E7%9F%AD%E8%AF%AD%E6%8F%90%E5%8F%96)
* [拼音转换](https://github.com/hankcs/HanLP/tree/1.x#17-%E6%8B%BC%E9%9F%B3%E8%BD%AC%E6%8D%A2)
    * 多音字、声母、韵母、声调
* [简繁转换](https://github.com/hankcs/HanLP/tree/1.x#18-%E7%AE%80%E7%B9%81%E8%BD%AC%E6%8D%A2)
    * 简繁分歧词（简体、繁体、臺灣正體、香港繁體）
* [文本推荐](https://github.com/hankcs/HanLP/tree/1.x#19-%E6%96%87%E6%9C%AC%E6%8E%A8%E8%8D%90)
    * 语义推荐、拼音推荐、字词推荐
* 依存句法分析
    * [基于神经网络的高性能依存句法分析器](https://github.com/hankcs/HanLP/tree/1.x#21-%E4%BE%9D%E5%AD%98%E5%8F%A5%E6%B3%95%E5%88%86%E6%9E%90)
    * [基于ArcEager转移系统的柱搜索依存句法分析器](https://github.com/hankcs/HanLP/blob/1.x/src/test/java/com/hankcs/demo/DemoDependencyParser.java#L34)
* [文本分类](https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90)
    * [情感分析](https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90#%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90)
* [文本聚类](https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E8%81%9A%E7%B1%BB)
    - KMeans、Repeated Bisection、自动推断聚类数目k
* [word2vec](https://github.com/hankcs/HanLP/wiki/word2vec)
    * 词向量训练、加载、词语相似度计算、语义运算、查询、KMeans聚类
    * 文档语义相似度计算
* [语料库工具](https://github.com/hankcs/HanLP/tree/1.x/src/main/java/com/hankcs/hanlp/corpus)
    - 部分默认模型训练自小型语料库，鼓励用户自行训练。所有模块提供[训练接口](https://github.com/hankcs/HanLP/wiki)，语料可参考[98年人民日报语料库](http://file.hankcs.com/corpus/pku98.zip)。

在提供丰富功能的同时，HanLP内部模块坚持低耦合、模型坚持惰性加载、服务坚持静态提供、词典坚持明文发布，使用非常方便。默认模型训练自全世界最大规模的中文语料库，同时自带一些语料处理工具，帮助用户训练自己的模型。

## 功能

繁简互转、获取转换字符位置、转换文本倒带。

因为hanlp提供的繁简互转无法获取被转换的字符以及位置，也无法做到将文本再转回去（倒带），所以特此重新做了简单封装。

使用方式见测试用例[WordHelperTest](src/test/java/wordHelper/WordHelperTest.java)。
