package wordHelper;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.dictionary.StringDictionary;
import com.hankcs.hanlp.utility.Predefine;

import java.util.*;

/**
 * @author stefan
 * @date 2021/12/9 10:28
 */
public class WordHelper {
    /**
     * 注：HanLP内存要求120MB以上（-Xms120m -Xmx120m -Xmn64m）
     * HanLP首次需要加载词库到内存中，所以首次会较慢，几十ms或者几百ms
     * 之后转换就很快了，在几ms左右
     *
     * 繁体 ---> 简体
     * @param content
     * @return
     */
    public static String convertToSimplifiedChinese(String content) {
        return TRAD_TO_SIMP.convertTo(content);
    }

    /**
     * 内存要求120MB以上（-Xms120m -Xmx120m -Xmn64m）
     * 繁体 ---> 简体
     * @param content 需要转换的文本
     * @param destOriginMap key为 转换之后字符在转换文本的位置，value为[转换之后字符, 原字符]
     * @return
     */
    public static String convertToSimplifiedChinese(String content, LinkedHashMap<Integer, String[]> destOriginMap) {
        return TRAD_TO_SIMP.convertTo(content, destOriginMap);
    }

    /**
     * 内存要求120MB以上（-Xms120m -Xmx120m -Xmn64m）
     * 简体 ---> 繁体
     * @param content
     * @return
     */
    public static String convertToTraditionalChinese(String content) {
        return SIMP_TO_TRAD.convertTo(content);
    }

    /**
     * 内存要求120MB以上（-Xms120m -Xmx120m -Xmn64m）
     * 简体 ---> 繁体
     * @param content 需要转换的文本
     * @param destOriginMap key为 转换之后字符在转换文本的位置，value为[转换之后字符, 原字符]
     * @return
     */
    public static String convertToTraditionalChinese(String content, LinkedHashMap<Integer, String[]> destOriginMap) {
        return SIMP_TO_TRAD.convertTo(content, destOriginMap);
    }

    /**
     * 倒带
     * 繁简互转后无法回到原文，此方法是可以将转换之后的文本回到原文
     * @param destContent 目的文本
     * @param destOriginMap convertToSimplifiedChinese or convertToTraditionalChinese 获得的 destOriginMap
     * @return
     */
    public static String rewind(String destContent, Map<Integer, String[]> destOriginMap) {
        char[] contentArr = destContent.toCharArray();
        int len = contentArr.length;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String[] destOrigin = destOriginMap.get(i);
            if (destOrigin == null) {
                result.append(contentArr[i]);
            } else {
                result.append(destOrigin[1]);
                i = i + destOrigin[0].length() - 1;
            }
        }
        return result.toString();
    }

    /**
     * 单个 词组 倒带
     * @param word
     * @param destIndex 该词组在目的文本的位置
     * @param destOriginMap
     * @return
     */
    public static String rewind(String word, int destIndex, Map<Integer, String[]> destOriginMap) {
        char[] wordArr = word.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < wordArr.length; i++) {
            String[] dest_origin = destOriginMap.get(destIndex);
            if (dest_origin == null) {
                result.append(wordArr[i]);
                destIndex++;
            } else {
                String origin = dest_origin[1];
                String dest = dest_origin[0];
                result.append(origin);
                destIndex = destIndex + dest.length();
                i = i + dest.length() - 1;
            }
        }
        return result.toString();
    }

    /**
     * 默认无需load
     * 有自定义词典时调用，但是也只是在第一次使用前load有效，不会重复load
     * @param customMap  自定义词组，key-繁体字，value-简体字
     * @param excludedMap 排除词组，key-繁体字，value-简体字，从词典中排除时，必须key和value都相等
     * @return
     */
    public static boolean loadTraditionalChineseDictionary(Map<String, String> customMap, Map<String, String> excludedMap) {
        return TRAD_TO_SIMP.load(customMap, excludedMap);
    }

    public static boolean loadTraditionalChineseDictionary(Map<String, String> customMap) {
        return loadTraditionalChineseDictionary(customMap, null);
    }

    public static boolean loadSimplifiedChineseDictionary(Map<String, String> customMap, Map<String, String> excludedMap) {
        return SIMP_TO_TRAD.load(customMap, excludedMap);
    }

    public static boolean loadSimplifiedChineseDictionary(Map<String, String> customMap) {
        return loadSimplifiedChineseDictionary(customMap, null);
    }


    /**
     * 繁简词典
     */
    private static class TraditionalChineseDictionary {
        private String path;
        private volatile AhoCorasickDoubleArrayTrie<String> trie;

        public TraditionalChineseDictionary(String path) {
            this.path = path;
        }

        public boolean load() {
            return load(null, null);
        }

        public boolean load(Map<String, String> customMap) {
            return load(customMap, null);
        }

        public boolean load(Map<String, String> customMap, Map<String, String> excludedMap) {
            if (trie != null) {
                return true;
            }
            synchronized (TraditionalChineseDictionary.class) {
                if (trie != null) {
                    return true;
                }
                if (path == null || path == "") {
                    Predefine.logger.info("load err, path is null");
                    return false;
                }
                long start = System.currentTimeMillis();
                TreeMap<String, String> map = new TreeMap();
                if (!load0(map, path)) {
                    Predefine.logger.info("load err, path=" + path);
                    return false;
                }

                if (customMap != null) {
                    map.putAll(customMap);
                }
                if (excludedMap != null) {
                    Set<String> keys = excludedMap.keySet();
                    for (String key : keys) {
                        map.remove(key, excludedMap.get(key));
                    }
                }

                trie = new AhoCorasickDoubleArrayTrie(map);
                Predefine.logger.info("load ok, path=" + path + "，耗时" + (System.currentTimeMillis() - start) + "ms");
                return true;
            }
        }

        public String convertTo(String traditionalChineseString) {
            return convertTo(traditionalChineseString, null);
        }

        public String convertTo(String traditionalChineseString, final Map<Integer, String[]> destOriginMap) {
            if (!load(null)) {
                throw new IllegalArgumentException("load err, path=" + path);
            }
            return segLongest(traditionalChineseString.toCharArray(), trie, destOriginMap);
        }

        private String segLongest(char[] charArray, AhoCorasickDoubleArrayTrie<String> trie, final Map<Integer, String[]> destOriginMap) {
            final String[] wordNet = new String[charArray.length];
            final int[] lengthNet = new int[charArray.length];
            trie.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<String>() {
                public void hit(int begin, int end, String value) {
                    int length = end - begin;
                    if (length > lengthNet[begin]) {
                        wordNet[begin] = value;
                        lengthNet[begin] = length;
                    }

                }
            });
            StringBuilder sb = new StringBuilder(charArray.length);
            int offset = 0;

            while(offset < wordNet.length) {
                if (wordNet[offset] == null) {
                    sb.append(charArray[offset]);
                    ++offset;
                } else {
                    int begin = offset;
                    String dest = wordNet[begin];
                    if (destOriginMap != null) {
                        String origin = new String(charArray, begin, lengthNet[begin]);
                        if (!dest.equals(origin)) {
                            // 排除一些 简繁一样的
                            String[] dest_originLen = {dest, new String(charArray, begin, lengthNet[begin])};
                            destOriginMap.put(sb.length(), dest_originLen);
                        }
                    }
                    sb.append(dest);
                    offset += lengthNet[offset];

                }
            }
            return sb.toString();
        }

        private boolean load0(Map<String, String> storage, String... path) {
            StringDictionary dictionary = new StringDictionary("=");
            String[] pathArray = path;
            int pathLen = pathArray.length;

            for(int i = 0; i < pathLen; ++i) {
                String pathTmp = pathArray[i];
                if (!dictionary.load(pathTmp)) {
                    return false;
                }
            }

            Set<Map.Entry<String, String>> entrySet = dictionary.entrySet();
            Iterator iteratorDictionary = entrySet.iterator();

            while(iteratorDictionary.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)iteratorDictionary.next();
                storage.put(entry.getKey(), entry.getValue());
            }
            return true;
        }
    }

    /**
     * 简繁词典
     */
    private static class SimplifiedChineseDictionary extends TraditionalChineseDictionary{
        public SimplifiedChineseDictionary(String path) {
            super(path);
        }
    }

    private static final String TRAD_TO_SIMP_PATH = HanLP.Config.tcDictionaryRoot + "t2s.txt";
    private static final String SIMP_TO_TRAD_PATH = HanLP.Config.tcDictionaryRoot + "s2t.txt";

    private static final TraditionalChineseDictionary TRAD_TO_SIMP = new TraditionalChineseDictionary(TRAD_TO_SIMP_PATH);
    private static final SimplifiedChineseDictionary SIMP_TO_TRAD = new SimplifiedChineseDictionary(SIMP_TO_TRAD_PATH);

}
