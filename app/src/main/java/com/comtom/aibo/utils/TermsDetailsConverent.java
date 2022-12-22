package com.comtom.aibo.utils;

import com.comtom.aibo.entity.TermsDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TermsDetailsConverent implements PropertyConverter<List<TermsDetails>,String> {

    private final Gson mGson;

    public TermsDetailsConverent() {
        mGson = new Gson();
    }

    @Override
    public List<TermsDetails> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<TermsDetails>>() {
        }.getType();
        ArrayList<TermsDetails> itemList= mGson.fromJson(databaseValue, type);
        return itemList;
    }


    @Override
    public String convertToDatabaseValue(List<TermsDetails> entityProperty) {

        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
