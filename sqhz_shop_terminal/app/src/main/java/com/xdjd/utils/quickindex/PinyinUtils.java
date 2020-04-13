package com.xdjd.utils.quickindex;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.xdjd.utils.LogUtils;

public class PinyinUtils {

	/**
	 * 根据传入的字符串(包含汉字),得到拼音
	 * 黑马 -> HEIMA
	 * 黑  马*& -> HEIMA
	 * 黑马f5 -> HEIMA
	 * @param str 字符串
	 * @return
	 */
	public static String getPinyin(String str) {
		
		/*HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		StringBuilder sb = new StringBuilder();
		
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// 如果是空格, 跳过
			if(Character.isWhitespace(c)){
				continue;
			}
			if(c >= -127 && c < 128){
				// 肯定不是汉字
				sb.append(c);
			}else {
				String s = "";
				try {
					// 通过char得到拼音集合. 单 -> dan, shan 
					s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					sb.append(s);
				}
			}
		}*/
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(PinyinHelper.getShortPinyin(str).toString()); //你好世界--nhsj
		} catch (PinyinException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}
