package com.comtom.aibo.utils;

import com.comtom.aibo.entity.ProgInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MusicConverent implements PropertyConverter<List<ProgInfoBean>,String> {

    private final Gson mGson;

    public MusicConverent() {
        mGson = new Gson();
    }

    @Override
    public List<ProgInfoBean> convertToEntityProperty(String databaseValue) {

        Type type = new TypeToken<ArrayList<ProgInfoBean>>() {
        }.getType();
        ArrayList<ProgInfoBean> itemList= mGson.fromJson(databaseValue, type);
        return itemList;
    }


    @Override
    public String convertToDatabaseValue(List<ProgInfoBean> entityProperty) {

        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
