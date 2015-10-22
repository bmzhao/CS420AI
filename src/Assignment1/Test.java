package Assignment1;

import java.util.*;

/**
 * Created by brianzhao on 10/20/15.
 */
public class Test {
    public static void main(String[] args) {
//        CustomInt customInt = new CustomInt(1);
//        CustomInt customInt1 = new CustomInt(1);
//        HashMap<CustomInt, Integer> set = new HashMap<>();
//        set.put(customInt, 1);
//        System.out.println(set.put(customInt1, 2));
////        System.out.println(System.identityHashCode(customInt));
////        System.out.println(System.identityHashCode(customInt1));
//
//        System.out.println(set.containsKey(customInt1));
//        System.out.println(set.containsKey(customInt));
//        System.out.println(set.get(customInt));
//        System.out.println(set.get(customInt1));
//
//        System.out.println(set.size());


        CustomInt customInt = new CustomInt(2);
        CustomInt customInt1 = new CustomInt(2);
        PriorityQueue<CustomInt> list = new PriorityQueue<>();
        list.add(customInt1);
//        list.remove(customInt);
        list.remove(new CustomInt(2));
        System.out.println(list.size());
//        list.add(customInt);
//        System.out.println(list.poll().myInt);
//        System.out.println(list.poll().myInt);
    }

    public static class CustomInt implements Comparable<CustomInt>{
        Integer myInt;

        public CustomInt(int x) {
            myInt = x;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CustomInt) {
                return myInt.equals(((CustomInt) obj).myInt);
            }
            return true;
        }

        @Override
        public int compareTo(CustomInt o) {
            return myInt - o.myInt;
        }

        @Override
        public int hashCode() {
            return myInt;
        }
    }


}
