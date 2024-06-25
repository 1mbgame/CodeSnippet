package com.ngwisefood.app.utility;

import org.springframework.data.repository.CrudRepository;

import java.util.*;

public class CacheSaveEntity<T> {

    private String tag = "[Save]";
    private List<T> itemList = new ArrayList<>();
    private long cacheMilliSecond = 0;
    private Timer timer;
    private boolean isRunning = false;
    private CrudRepository repository;

    public CacheSaveEntity(String tag,int minute) {
        this.tag = tag;
        cacheMilliSecond = minute * 60 * 1000;
        enableSelfTimer();
    }

    public void enableSelfTimer(){
        if(timer != null){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int nextMinute = 5 - (currentMinute % 5);
        calendar.add(Calendar.MINUTE,1);
        Date firstDate = calendar.getTime();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearCacheAndSave();
            }
        },firstDate,cacheMilliSecond);

        System.out.println("Next Clear Cache Date is : " + firstDate);
    }

    public void clearCacheAndSave() {

        if(isRunning){
            System.out.println("Saving the data into database");
            return;
        }
        if(itemList.size() == 0){
            System.out.println("No data yet");
            return;
        }

        isRunning = true;
        System.out.println("Start Saving the data into database ...");

        boolean isSavingDataSuccess = true;

        try{

            List<T> itemList = getAllAndClear();

            // Save all the data into database
            if(itemList.size() > 0){
                this.repository.saveAll(itemList);
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

    public void save(CrudRepository repository, T t) {
        this.repository = repository;
        itemList.add(t);
    }

    public synchronized List<T> getAllAndClear(){
        List<T> tList = new ArrayList<>();
        tList.addAll(itemList);
        itemList.clear();
        return tList;
    }

    public int size(){
        return this.itemList.size();
    }

    public void setRepository(CrudRepository repository) {
        this.repository = repository;
    }
}
