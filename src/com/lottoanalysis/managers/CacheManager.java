package com.lottoanalysis.managers;

import com.lottoanalysis.interfaces.Cacheable;

import java.util.*;

@SuppressWarnings("unchecked")
public class CacheManager {

    private static Map<Object, List<Map<Integer, Cacheable>>> cacheHashMap = new HashMap<>();

    public static void putCache(Cacheable object, int drawIndex) {

        List<Map<Integer,Cacheable>> cacheableMapOne = (List<Map<Integer, Cacheable>>) cacheHashMap.get(object.getIdentifier());
        if(cacheableMapOne == null){

            List<Map<Integer,Cacheable>>  mapData = new ArrayList<>();
            Map<Integer, Cacheable> newIndexData = new HashMap<>();
            newIndexData.put(drawIndex, object);
            mapData.add(newIndexData);
            cacheHashMap.put(object.getIdentifier(), mapData);

        }
        else{
            ListIterator<Map<Integer,Cacheable>> data = cacheHashMap.get(object.getIdentifier()).listIterator();
            Set<Integer> keyHolder = new HashSet<>();

            while (data.hasNext()){

                Map<Integer,Cacheable> cacheableMap = data.next();
                cacheableMap.forEach( (k,v) -> keyHolder.add(k));
            }

            if(!keyHolder.contains(drawIndex)){

                List<Map<Integer,Cacheable>> newData = cacheHashMap.get(object.getIdentifier());
                Map<Integer, Cacheable> newIndexData = new HashMap<>();
                newIndexData.put(drawIndex, object);
                newData.add(newIndexData);
                cacheHashMap.put(object.getIdentifier(), newData);
            }
        }
    }

    public static Cacheable getCache(Object identifier, int position) {

        List<Map<Integer,Cacheable>> object = (List<Map<Integer,Cacheable>>) cacheHashMap.get(identifier);

        if(object != null) {

            for(Map<Integer,Cacheable> data : object) {

                for (Integer integer : data.keySet()) {

                    Cacheable cacheable = data.get(integer);

                    if (cacheable == null) {
                        return null;
                    }

                    if (cacheable.isExpired()) {
                        cacheHashMap.remove(identifier);
                        return null;
                    } else if (cacheable.getDrawPosition() == position) {
                        return cacheable;
                    }

                }

            }

        }

        return null;
    }
}
