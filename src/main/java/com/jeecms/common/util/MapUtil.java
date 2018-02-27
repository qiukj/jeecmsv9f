package com.jeecms.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
	
	/** 
     * 使用 Map按key进行排序 
     * @param map 
     * @return 
     */  
    public static Map<String, String> sortMapByKey(Map<String, String> map) {  
        return sortMap(1, map) ; 
    }
    
	/** 
     * 使用 Map按value进行排序 
     * @param map 
     * @return 
     */  
    public static Map<String, String> sortMapByValue(Map<String, String> map) {  
        return sortMap(2, map) ;
    } 
    
    private static Map<String, String> sortMap(int sortBy,Map<String, String> map) {  
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(map.entrySet());  
        if(sortBy==1){
        	 Collections.sort(entryList, new MapKeyComparator());  
        }else{
        	Collections.sort(entryList, new MapValueComparator());  
        }
        Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
        Map.Entry<String, String> tmpEntry = null;  
        while (iter.hasNext()) {  
            tmpEntry = iter.next();  
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
        }  
        return sortedMap;  
    } 
    
    public static Map<String, Long> sortMapByLongValue(Map<String, Long> map) {  
        return sortLongMap(2, map) ;
    } 
    
    private static Map<String, Long> sortLongMap(int sortBy,Map<String, Long> map) {  
        if (map == null || map.isEmpty()) {  
            return map;  
        }  
        Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();  
        List<Map.Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(map.entrySet());  
        if(sortBy==1){
        	 Collections.sort(entryList, new MapLongValueComparator());  
        }else{
        	Collections.sort(entryList, new MapLongValueComparator());  
        }
        Iterator<Map.Entry<String, Long>> iter = entryList.iterator();  
        Map.Entry<String, Long> tmpEntry = null;  
        while (iter.hasNext()) {  
            tmpEntry = iter.next();  
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
        }  
        return sortedMap;  
    }
} 



class MapValueComparator implements Comparator<Map.Entry<String, String>> {  
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {  
        return me1.getValue().compareTo(me2.getValue());  
    }  
}  

class MapKeyComparator implements Comparator<Map.Entry<String, String>> {  
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {  
        return me1.getKey().compareTo(me2.getKey());  
    }  
} 

class MapLongKeyComparator implements Comparator<Map.Entry<Long, String>> {  
    public int compare(Entry<Long, String> me1, Entry<Long, String> me2) {  
        return me1.getKey().compareTo(me2.getKey());  
    }  
} 

class MapLongValueComparator implements Comparator<Map.Entry<String, Long>> {  
    public int compare(Entry<String, Long> me1, Entry<String, Long> me2) {  
        return me2.getValue().compareTo(me1.getValue());  
    }  
}  
