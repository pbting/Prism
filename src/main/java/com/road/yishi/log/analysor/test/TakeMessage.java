package com.road.yishi.log.analysor.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import com.road.yishi.log.Log;
import com.road.yishi.log.core.TaskExecuteException;
import com.road.yishi.log.core.quence.Action;

public class TakeMessage extends Action {
	private static final Map<String, Integer> count = new ConcurrentHashMap<String, Integer>();
	String topic;
	String key;

	public TakeMessage(String topic, String key) {
		this.topic = topic;
		this.key = key;
	}

	@Override
	public long execute() throws TaskExecuteException {
		// System.out.println("消费：----->"+key+"--["+topic+"]-->"+TopicHouseMgr.getPersons(topic, key).size());
		Integer c = (count.get(key) == null ? 0 : count.get(key));
		count.put(key, c + TopicHouseMgr.getPersons(topic, key).size());
		System.err.println("出现幸运数字：----->" + key + "--[" + topic + "]-->次数：" + count.get(key));
		flush();
		return 0;
	}

	private void flush() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File("D:/take_result"),false));
			writer.write(new Gson().toJson(count));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			Log.error("", e);
		}
	}
}
