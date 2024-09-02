package io.bootify.visitor_managment_system.util;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1,1,5,2,4,5,1,6,3,9);

        var a =list.stream().collect(Collectors
                        .groupingBy(i->i, LinkedHashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(m->m.getValue()==1)
                .findFirst().get()
                .getKey();


        System.out.println(a);
//        LinkedHashMap<Integer, Integer> mapD = new LinkedHashMap<>();
//        HashMap<Integer, Integer> mapP = new HashMap<>();
//        for(Integer a : list){
//            if(mapD.containsKey(a)){
//                mapD.put(a,mapD.get(a)+1);
//            }
//            else {
//                mapD.put(a,1);
//            }
//        }
////        for(Integer a : list){
////            if(mapP.containsKey(a)){
////                mapP.put(a,mapP.get(a)+1);
////            }
////            else {
////                mapP.put(a,1);
////            }
////        }
//
//        for(Integer d: mapD.keySet()){
//            if(mapD.get(d)==1){
//                System.out.println(d);
//                break;
//            }
//        }

//        System.out.println(mapD);
//        System.out.println(mapP);
    }
}
