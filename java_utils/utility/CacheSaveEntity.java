package com.ngwisefood.app.utility;

import com.google.gson.Gson;
import com.ngwisefood.app.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class CacheSaveEntity<T> {

    private Map<String, T> keyObjectMap = new ConcurrentHashMap<>();
    private long cacheMilliSecond = 0;
    private Timer timer;
    private boolean isRunning = false;
    private CrudRepository repository;

    public CacheSaveEntity(CrudRepository repository) {
        this.repository = repository;
        cacheMilliSecond = 5 * 60 * 1000;
        enableSelfTimer();
    }

    public void enableSelfTimer(){
        if(timer != null){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int nextMinute = 5 - (currentMinute % 5);
        calendar.add(Calendar.MINUTE,nextMinute);
        Date firstDate = calendar.getTime();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearCache();
            }
        },firstDate,cacheMilliSecond);

        System.out.println("Next Clear Cache Date is : " + firstDate);
    }

    public void clearCache() {

        if(isRunning){
            System.out.println("Saving the data into database");
            return;
        }
        isRunning = true;
        System.out.println("Start Saving the data into database ...");

        boolean isSavingDataSuccess = true;

        try{
            Map<String, T> keyObjectMapTemp = new ConcurrentHashMap<>();
            List<T> itemList = new ArrayList<>();
            // Clone the Map
            keyObjectMapTemp.putAll(keyObjectMap);
            for(Map.Entry<String, T> entry : keyObjectMapTemp.entrySet()){
                itemList.add(entry.getValue());
            }

            // Save all the data into database
            if(itemList.size() > 0){
                this.repository.saveAll(itemList);
            }


            for(Map.Entry<String, T> entry : keyObjectMapTemp.entrySet()){
                keyObjectMap.remove(entry.getKey());
            }

        }catch (Exception e){
            e.printStackTrace();
            isSavingDataSuccess = false;
        }

        isRunning = false;

        if(isSavingDataSuccess){
            System.out.println("Saving the data success ");
        }else{
            System.out.println("Saving the data failure ");
        }


    }

    public void save(String key, T t) {
        keyObjectMap.put(key, t);
    }

    public synchronized List<T> getAllAndClear(){
        List<T> tList = new ArrayList<>(keyObjectMap.values());
        clear();
        return tList;
    }

    public void clear(){
        keyObjectMap.clear();
    }

    public void remove(String key){
        try{
            keyObjectMap.remove(key);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int size(){
        return this.keyObjectMap.size();
    }

    public void saveTest(CrudRepository repository){
        Gson gson = new Gson();
        gson.fromJson("", UserEntity.class);
        repository.save(keyObjectMap.get(""));
    }

}
