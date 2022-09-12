package com.mobile.app.library;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class CacheManager<T> {

    private Map<String, T> keyObjectMap = new ConcurrentHashMap<>();
    private Map<String, Long> keyExpireTimeMap = new LinkedHashMap<>();
    private long cacheMilliSecond = 0;
    private Timer timer;

    public CacheManager(long cacheMinute) {
        cacheMilliSecond = cacheMinute * 60 * 1000;
    }

    public static void main(String[] args) {

        CacheManager<String> cacheManager = new CacheManager(1);

        for (int i = 0; i < 10; i++) {
            String line = "Line " + i;
            cacheManager.cacheMilliSecond = (i + 5) * 1000;
            cacheManager.save(i + "", line);
        }

    }

    public synchronized void save(String key, T t) {
        keyObjectMap.put(key, t);
        keyExpireTimeMap.put(key, System.currentTimeMillis() + cacheMilliSecond);
    }

    public synchronized T get(String key) {
        T t = keyObjectMap.get(key);
        return t;
    }
    public synchronized void clearCache() {

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = keyExpireTimeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (currentTime > entry.getValue()) {
                String key = entry.getKey();
                keyObjectMap.remove(key);
                iterator.remove();
            } else {
                break;
            }
        }
        
        System.out.println(keyObjectMap.toString());

    }

    public Timer getTimer() {
        return timer;
    }

    public void enableSelfTimer(){
        if(timer != null){
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearCache();
            }
        },0,60000);
    }
    private static void printMemory() {

        Runtime runtime  = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory()/1000000L;
        long freeMemory  = runtime.freeMemory()/1000000L;

        String result = "Memory: Used=" + (totalMemory - freeMemory) + "MB, Total=" + totalMemory + "MB, Free=" + freeMemory + "MB";
        System.out.println(result);

    }
}
