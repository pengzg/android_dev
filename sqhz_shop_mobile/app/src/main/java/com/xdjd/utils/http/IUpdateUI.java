package com.xdjd.utils.http;


public interface IUpdateUI<T> {
	void updata(T jsonStr);
	
	void sendFail(ExceptionType s);

	void finish();
}
