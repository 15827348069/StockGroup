package com.zbmf.StocksMatch.listener;


import com.zbmf.StocksMatch.widget.SwipeLayout;

public interface SwipeLayoutInterface {

	SwipeLayout.Status getCurrentStatus();
	
	void close();
	
	void open();
}
