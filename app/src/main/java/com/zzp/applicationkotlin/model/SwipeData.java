package com.zzp.applicationkotlin.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by samzhang on 2021/12/8.
 */

public class SwipeData {

    public static final int MAX_COUNT = 10;


    private int index = 0;
    private String name = "";

    private static Object mLock = new Object();

    private SwipeData next;

    private static int count;
    private static SwipeData mPool;

    public SwipeData(){
        System.out.println("------SwipeData create------");
    }

    public static SwipeData obtain(){
        synchronized (mLock) {
            if (mPool != null) {
                SwipeData outData = mPool;
                mPool = mPool.next;
                outData.next = null;
                count--;
                return outData;
            }
        }
        return new SwipeData();
    }

    public void recycle(){
        synchronized (mLock) {
            if(mPool != null){
                if(count < MAX_COUNT){
                    SwipeData nextData = mPool;
                    mPool = this;
                    mPool.next = nextData;
                    mPool.index = 0;
                    mPool.name = null;
                    count++;
                }
            }else{
                mPool = this;
                count++;
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("------SwipeData finalize------");
    }

    public static SwipeData fromGson(String json){
        try {
            SwipeData data  = (SwipeData) mTypeAdapter.fromJson(json);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toGson(SwipeData data){
        return mTypeAdapter.toJson(data);
    }

    static TypeAdapter mTypeAdapter = new TypeAdapter<SwipeData>(){

        @Override
        public void write(JsonWriter out, SwipeData value) throws IOException {
            out.beginObject();
            out.name("index").value(value.getIndex());
            out.name("name").value(value.getName());
            out.endObject();
        }

        @Override
        public SwipeData read(JsonReader in) throws IOException {
            final SwipeData swipeData = SwipeData.obtain();

            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "index":
                        swipeData.setIndex(in.nextInt());
                        break;
                    case "name":
                        swipeData.setName(in.nextString());
                        break;
                }
            }
            in.endObject();

            return swipeData;
        }
    };
}

