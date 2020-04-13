package com.xdjd.utils.quickindex;

import java.util.Comparator;

import com.xdjd.distribution.bean.AddressListBean;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<AddressListBean> {

	public int compare(AddressListBean o1, AddressListBean o2) {
		if (o1.getPinyin().equals("@")
				|| o2.getPinyin().equals("#")) {
			return -1;
		} else if (o1.getPinyin().equals("#")
				|| o2.getPinyin().equals("@")) {
			return 1;
		} else {
			return o1.getPinyin().compareTo(o2.getPinyin());
		}
	}

}
