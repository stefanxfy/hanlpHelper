package wordHelper;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.junit.Test;

import java.util.*;

/**
 * @author stefan
 * @date 2021/12/9 11:53
 */
public class WordHelperTest {
    public static AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie();

    @Test
    public void convertToSimplifiedChinese() {
        LinkedHashMap<Integer, String[]> destOriginMap = new LinkedHashMap<Integer, String[]>();
        String content = "白皮書說，書鐵桿部隊憤怒情緒集結書馬英九腹背受敵。以後等妳當上皇后，就能買士多啤梨慶祝了";
        String rt = WordHelper.convertToSimplifiedChinese(content, destOriginMap);
        for (Map.Entry<Integer, String[]> integerEntry : destOriginMap.entrySet()) {
            System.out.println(integerEntry.getKey() + "::" + integerEntry.getValue()[0] + "=" + integerEntry.getValue()[1]);
        }
        System.out.println(content);
        System.out.println(rt);
        // 倒带恢复原文
        String rewindContent = WordHelper.rewind(rt, destOriginMap);
        System.out.println(rewindContent);
    }

    @Test
    public void convertToTraditionalChinese() {
        LinkedHashMap<Integer, String[]> destOriginMap = new LinkedHashMap<Integer, String[]>();
        String content = "白皮書說，民主是全人類的共同價值，是中國共產黨和中國人民始終不渝堅持的重要理念。100年來，黨高舉人民民主旗幟，領導人民在一個有幾千年封建社會歷史、近代成爲半殖民地半封建社會的國家實現了人民當家作主，中國人民真正成爲國家、社會和自己命運的主人。\n" +
                "白皮書介紹，中國的民主是人民民主，人民當家作主是中國民主的本質和核心。全過程人民民主，實現了過程民主和成果民主、程序民主和實質民主、直接民主和間接民主、人民民主和國家意志相統一，是全鏈條、全方位、全覆蓋的民主，是最廣泛、最真實、最管用的社會主義民主。\n" +
                "白皮书说，民主是全人类的共同价值，是中国共产党和中国人民始终不渝坚持的重要理念。100年来，党高举人民民主旗帜，领导人民在一个有几千年封建社会历史、近代成为半殖民地半封建社会的国家实现了人民当家作主，中国人民真正成为国家、社会和自己命运的主人。\n" +
                "白皮书介绍，中国的民主是人民民主，人民当家作主是中国民主的本质和核心。全过程人民民主，实现了过程民主和成果民主、程序民主和实质民主、直接民主和间接民主、人民民主和国家意志相统一，是全链条、全方位、全覆盖的民主，是最广泛、最真实、最管用的社会主义民主。\n" +
                "白皮书强调，民主是历史的、具体的、发展的，各国民主植根于本国的历史文化传统，成长于本国人民的实践探索和智慧创造，民主道路不同，民主形态各异。民主不是装饰品，不是用来做摆设的，而是要用来解决人民需要解决的问题的。民主是各国人民的权利，而不是少数国家的专利。\n" +
                "白皮书指出，一个国家是不是民主，应该由这个国家的人民来评判，而不应该由外部少数人指手画脚来评判。";
        String rt = WordHelper.convertToTraditionalChinese(content, destOriginMap);
        for (Map.Entry<Integer, String[]> integerEntry : destOriginMap.entrySet()) {
            System.out.println(integerEntry.getKey() + "::" + integerEntry.getValue()[0] + "=" + integerEntry.getValue()[1]);
        }
        System.out.println(content);
        System.out.println("--------------------------------------------");
        System.out.println(rt);
        System.out.println("--------------------------------------------");
        // 倒带恢复原文
        String rewindContent = WordHelper.rewind(rt, destOriginMap);
        System.out.println(rewindContent);

    }

    @Test
    public void testDFAKwFilter() {
        DFAKwFilter gfwKWFilter = new DFAKwFilter();
        Set<String> words = new HashSet<String>();
        words.add("马英九腹背受敌");
        words.add("买草莓庆祝");
        gfwKWFilter.init(words);
        String content = "白皮書說，書鐵桿部隊憤怒情緒集結書馬英九腹背受敵。買草莓庆祝，以後等妳當上皇后，就能買士多啤梨慶祝了";

        // 转换
        LinkedHashMap<Integer, String[]> destOriginMap = new LinkedHashMap<Integer, String[]>();

        String rt = WordHelper.convertToSimplifiedChinese(content, destOriginMap);

        for (Map.Entry<Integer, String[]> destOriginEntry : destOriginMap.entrySet()) {
            System.out.println(destOriginEntry.getKey() + "::" + destOriginEntry.getValue()[0] + "=" + destOriginEntry.getValue()[1]);
        }
        // 匹配关键词
        Map<String, List<Integer>> matchWords2 = gfwKWFilter.getWordAndIndexMap(rt, DFAKwFilter.MIN_MATCH_TYPE);

        // 关键词 倒带回原文
        Map<String, List<String>> listMap = new LinkedHashMap<String, List<String>>();
        for (Map.Entry<String, List<Integer>> stringListEntry : matchWords2.entrySet()) {
            String word = stringListEntry.getKey();
            List<String> originWordList = new ArrayList<String>();

            for (Integer index : stringListEntry.getValue()) {
                originWordList.add(WordHelper.rewind(word, index, destOriginMap));
            }
            listMap.put(word, originWordList);
        }

        System.out.println(content);
        System.out.println(rt);
        System.out.println(matchWords2);

        System.out.println(listMap);
    }
}
