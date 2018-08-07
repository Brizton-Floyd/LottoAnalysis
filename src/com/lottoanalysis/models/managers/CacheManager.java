package com.lottoanalysis.models.managers;

import com.lottoanalysis.interfaces.Cacheable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CacheManager {

    private static Map<Object, Cacheable> cacheMap = new HashMap<>();

    public static void putCache(Cacheable object) {

        cacheMap.put(object.getIdentifier(), object);

    }

    public static Cacheable getCache(Cacheable identifier) {

        if(cacheMap.containsKey(identifier.getIdentifier())){

            return cacheMap.get( identifier.getIdentifier() );
        }

        return null;
    }

    public static void clearMap(){
        cacheMap.clear();
    }
}
