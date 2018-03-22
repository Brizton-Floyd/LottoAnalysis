package com.lottoanalysis.managers;

import java.util.*;

public class CacheManager{

    private static Map<Object,Cacheable> cacheHashMap = new HashMap<>();


    public static void putCache(Cacheable object)
    {
      // Remember if the HashMap previously contains a mapping for the key the old value
      // will be replaced.  This is valid functioning.
      cacheHashMap.put(object.getIdentifier(), object);
    }

    public static Cacheable getCache(Object identifier, int position)
    {
        Cacheable object = (Cacheable)cacheHashMap.get(identifier.getIdentifier());
        if (object == null){
            return null;
        }
       
        if (object.isExpired())
        {
            cacheHashMap.remove(identifier);
            return null;
        }
        else if(object.getDrawPosition() == position)
        {
             return object;   
        }    

        return null;
    }
}
