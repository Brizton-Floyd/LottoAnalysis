package com.lottoanalysis.interfaces;

public interface Cacheable{

    public boolean isExpired();
    
    public Object getIdentifier();
}
