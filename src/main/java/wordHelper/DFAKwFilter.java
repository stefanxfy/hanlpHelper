package wordHelper;

import java.util.*;

/**
 * @author stefan
 * @date 2021/12/9 10:01
 */
public class DFAKwFilter {

	public void init(Set<String> words) {
		// 将敏感词库加入到HashMap中
		if (m_kwWordMap != null) {
			return;
		}
		m_kwWordMap = addWordToHashMap(words);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reInit(Set<String> words) {
		Map wordMap = new HashMap(words.size());
		for (String word : words) {
			Map nowMap = wordMap;
			for (int i = 0; i < word.length(); i++) {
				// 转换成char型
				char keyChar = word.charAt(i);
				// 获取
				Object tempMap = nowMap.get(keyChar);
				// 如果存在该key，直接赋值
				if (tempMap != null) {
					nowMap = (Map) tempMap;
				}
				// 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
				else {
					// 设置标志位
					Map<String, String> newMap = new HashMap<String, String>();
					newMap.put("isEnd", "0");
					// 添加到集合
					nowMap.put(keyChar, newMap);
					nowMap = newMap;
				}
				// 最后一个
				if (i == word.length() - 1) {
					nowMap.put("isEnd", "1");
				}
			}
		}
		m_kwWordMap = wordMap;
	}

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
	 * 中 = { isEnd = 0 国 = {<br>
	 * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd =
	 * 1 } } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1
	 * } } } }
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized Map addWordToHashMap(Set<String> wordSet) {
		if (m_kwWordMap != null){
			return m_kwWordMap;
		}
		Map wordMap = new HashMap(wordSet.size());
		for (String word : wordSet) {
			Map nowMap = wordMap;
			for (int i = 0; i < word.length(); i++) {
				// 转换成char型
				char keyChar = word.charAt(i);
				// 获取
				Object tempMap = nowMap.get(keyChar);
				// 如果存在该key，直接赋值
				if (tempMap != null) {
					nowMap = (Map) tempMap;
				}
				// 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
				else {
					// 设置标志位
					Map<String, String> newMap = new HashMap<String, String>();
					newMap.put("isEnd", "0");
					// 添加到集合
					nowMap.put(keyChar, newMap);
					nowMap = newMap;
				}
				// 最后一个
				if (i == word.length() - 1) {
					nowMap.put("isEnd", "1");
				}
			}
		}
		return wordMap;
	}


	/**
	 * 判断文字是否包含敏感字符
	 *
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public boolean isHasWord(String txt, int matchType) {
		boolean flag = false;
		for (int i = 0; i < txt.length(); i++) {

			// 判断是否包含敏感字符
			int matchFlag = this.checkWord(txt, i, matchType);

			// 大于0存在，返回true
			if (matchFlag > 0) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取文字中的敏感词
	 *
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public Set<String> getWord(String txt, int matchType) {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < txt.length(); i++) {
			// 判断是否包含敏感字符
			int length = checkWord(txt, i, matchType);
			// 存在,加入list中
			if (length > 0) {
				set.add(txt.substring(i, i + length));
				// 减1的原因，是因为for会自增
				i = i + length - 1;
			}
		}
		return set;
	}

	/**
	 * 获取文字中的敏感词list
	 *
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public List<String> getWordList(String txt, int matchType) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < txt.length(); i++) {
			// 判断是否包含敏感字符
			int length = checkWord(txt, i, matchType);
			// 存在,加入list中
			if (length > 0) {
				list.add(txt.substring(i, i + length));
				// 减1的原因，是因为for会自增
				i = i + length - 1;
			}
		}
		return list;
	}

	/**
	 * 获取敏感字的map
	 * @param txt
	 * @param matchType
	 * @return
	 */
	public Map<String, Integer> getWordMap(String txt, int matchType) {
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		List<String> list = getWordList(txt, matchType);
		for (String str: list) {
			if (wordMap.containsKey(str)) {
				int count = wordMap.get(str) + 1;
				wordMap.put(str, count);
			} else {
				wordMap.put(str, 1);
			}
		}
		return wordMap;
	}

	public Map<String, List<Integer>> getWordAndIndexMap(String txt, int matchType) {
		Map<String, List<Integer>> wordMap = new LinkedHashMap<String, List<Integer>>();
		for (int i = 0; i < txt.length(); i++) {
			// 判断是否包含敏感字符
			int length = checkWord(txt, i, matchType);
			// 存在,加入list中
			if (length > 0) {
				String matchWord = txt.substring(i, i + length);
				List<Integer> indexList = wordMap.get(matchWord);
				if (indexList == null) {
					indexList = new ArrayList<Integer>();
				}
				indexList.add(i);
				wordMap.put(matchWord, indexList);
				// 减1的原因，是因为for会自增
				i = i + length - 1;
			}
		}
		return wordMap;
	}



	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：
	 * 如果存在，则返回敏感词字符的长度，不存在返回0
	 *
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return
	 */
	public int checkWord(String txt, int beginIndex, int matchType) {
		// 匹配标识数默认为0
		int matchFlag = 0;
		int maxEndMatchFlag = 0;
		Map nowMap = m_kwWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			char word = txt.charAt(i);
			// 获取指定key
			nowMap = (Map) nowMap.get(word);
			// 存在，则判断是否为最后一个
			if (nowMap != null) {
				// 找到相应key，匹配标识+1
				matchFlag++;
				// 如果为最后一个匹配规则,结束循环，返回匹配标识数
				if ("1".equals(nowMap.get("isEnd"))) {
					maxEndMatchFlag = matchFlag;
					// 最小规则，直接返回,最大规则还需继续查找
					if (MIN_MATCH_TYPE == matchType) {
						break;
					}
				}
			}
			// 不存在，返回maxEndMatchFlag
			else {
				break;
			}
		}
		return maxEndMatchFlag;
	}

	public static final int MIN_MATCH_TYPE = 1;	// 最小匹配规则
	public static final int MAX_MATCH_TYPE = 2;	// 最大匹配规则
	private Map m_kwWordMap = null;//敏感词Map
}
