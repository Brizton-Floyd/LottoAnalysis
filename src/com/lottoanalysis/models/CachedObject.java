package com.lottoanalysis.models;

import com.lottoanalysis.interfaces.Cacheable;

import java.util.*;

public class CachedObject implements Cacheable {

    private Object identifier = null;
    private Object object;
    private int drawPosition;

    public CachedObject(Object obj, Object id, int drawPosition)
    {
        this.object = obj;
        this.identifier = id;
        this.drawPosition = drawPosition;
    }

    @Override
    public boolean isExpired(){

        return false;
    }

    @Override
    public Object getIdentifier(){

        return this.identifier;
    }

    @Override
    public Object getObject(){
        return this.object;
    }

    @Override
    public int getDrawPosition(){
        return this.drawPosition;
    }
}
