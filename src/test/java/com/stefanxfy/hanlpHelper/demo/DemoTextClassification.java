package com.stefanxfy.hanlpHelper.demo;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stefan
 * @date 2021/12/11 14:30
 */
public class DemoTextClassification {
    public static final String MODEL_PATH = "mydata/classificationModel/";

    @Test
    public void testClassifier() {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH + "test-model.ser");
        if (model == null) {
            model = train();
        }

        IClassifier classifier = new NaiveBayesClassifier(model);

        predict(classifier, "周杰伦已经有五年没有出专辑了。");
        predict(classifier, "《三重门》是韩寒写的吗？");
        predict(classifier, "习主席在延川下过乡。");
    }

    private NaiveBayesModel train() {
        IClassifier classifier = new NaiveBayesClassifier(); // 创建分类器，更高级的功能请参考IClassifier的接口定义
        Map<String, String[]> data = new HashMap<String, String[]>();
        data.put("明星", new String[]{"胡歌", "周杰伦", "林依晨"});
        data.put("政府官员", new String[]{"习主席", "周恩来", "毛泽东"});
        data.put("作家", new String[]{"韩寒", "周国平", "杨绛"});
        classifier.train(data);
        // 保存训练模型
        NaiveBayesModel model = (NaiveBayesModel) classifier.getModel();
        IOUtil.saveObjectTo(model, MODEL_PATH + "test-model.ser");
        return model;
    }

    private static void predict(IClassifier classifier, String text)
    {
        System.out.printf("《%s》 属于分类 【%s】\n", text, classifier.classify(text));
    }
}
