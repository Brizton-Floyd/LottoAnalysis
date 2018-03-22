package com.lottoanalysis.interfaces;

public interface Cacheable{

    public boolean isExpired();
    public Object getIdentifier();
    public Object getObject();
    public int getDrawPosition();
    
}
