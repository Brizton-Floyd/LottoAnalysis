package com.lottoanalysis.models;

import com.lottoanalysis.interfaces.Cacheable;

import java.util.*;

public class GameSelectionObject implements Cacheable {

    private Object identifier = null;
    private Object object;

    public GameSelectionObject(Object obj, Object id, int minutesToLive)
    {
        this.object = obj;
        this.identifier = id;
    }

    @Override
    public boolean isExpired(){

        return false;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Object getIdentifier(){

        return this.identifier;
    }

}
