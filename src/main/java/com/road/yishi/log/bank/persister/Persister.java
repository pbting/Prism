package com.road.yishi.log.bank.persister;

import com.road.yishi.log.bank.DataBankMgr;
import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;

public class Persister extends Action{
	private String topic;
	private Object key;
	private Object message;
	private String filePath;
	private short type;
	public Persister(String topic, Object key, Object message, String filePath, short type) {
		this.topic = topic;
		this.key = key;
		this.message = message;
		this.filePath = filePath;
		this.type = type;
	}

	@Override
	public long execute() throws TaskExecuteException {
		DataBankMgr.store(topic,key,message, filePath,type);
		return 1 ;
	}
}
