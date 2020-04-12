package com.palace.dream.simple.bean;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.alibaba.fastjson.JSONObject;

public class MapExt {

	Map<String, Object> map;

	public static MapExt ins() {
		return new MapExt(new HashMap<String,Object>());
	}
	public static MapExt ins(Map<String, Object> map) {
		return new MapExt(map);
	}
	public static MapExt of(Object ...objs) {
		if(objs.length % 2 != 0) {
			throw new IllegalArgumentException("参数个数错误");
		}
		Map<String,Object> map = new HashMap();
		for(int i=0;i<objs.length;) {
			map.put((String)objs[i],objs[i+1]);
			i+=2;
		}
		return new MapExt(map);
	}
	public MapExt copyMap(Map<String,Object> map) {
		if(map != null && !map.isEmpty()) {
			map.forEach((key,value)->{
				this.map.put(key,value);
			});
		}
		return this;
	}
	public MapExt(Map<String, Object> map) {
		this.map = map;
	}
	public Map<String,Object> getData(){
		return this.map;
	}
	public MapExt put(String key,Object obj) {
		this.map.put(key,obj);
		return this;
	}
	public MapExt getMap(String key){
		Map<String,Object> map = (Map<String, Object>) this.map.get(key);
		return new MapExt(map);
	}
	public List<Map<String,Object>> getMapList(String key){
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) this.map.get(key);
		if(mapList == null) {
			return new ArrayList<Map<String,Object>>();
		}
		return mapList;
	}
	public void forEachList(String key,Consumer<MapExt> action) {
        for(Map<String,Object> map : this.getMapList(key)){
            action.accept(new MapExt(map));
        }
    }
    public void forEachMap(BiConsumer<String,Object> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }
	public String getString(String key) {
		if (map != null) {
			final Object answer = map.get(key);
			if (answer != null) {
				return answer.toString();
			}
		}
		return null;
	}

	public Integer getInt(String key) {
		final Number answer = getNumber(map, key);
		if (answer == null) {
			return null;
		}
		if (answer instanceof Integer) {
			return (Integer) answer;
		}
		return Integer.valueOf(answer.intValue());
	}

	public Long getLong(String key) {
		final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Long) {
            return (Long) answer;
        }
        return Long.valueOf(answer.longValue());
	}
	public static <K> Number getNumber(final Map<? super K, ?> map, final K key) {
		if (map != null) {
			final Object answer = map.get(key);
			if (answer != null) {
				if (answer instanceof Number) {
					return (Number) answer;
				}
				if (answer instanceof String) {
					try {
						final String text = (String) answer;
						return NumberFormat.getInstance().parse(text);
					} catch (final ParseException e) { // NOPMD
						// failure means null is returned
					}
				}
			}
		}
		return null;
	}
	
	public String jsonString() {
		return JSONObject.toJSONString(this.map);
	}

}
