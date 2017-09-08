package com.xjy.adm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class My170817 {
    public static void main(String[] args) {
        List<Persion> list = new ArrayList<>();
        list.add(new Persion(15));
        list.add(new Persion(19));
        list.add(new Persion(12));
//        list.forEach((p) -> System.out.println(p.getAge()));
//        list.forEach(System.out::println);
//        System.out.println("-----------------");
//        Collections.sort(list,(p1,p2)-> p2.getAge()-p1.getAge());
        Collections.sort(list,new PersionComparator());
//        list.forEach((p) -> System.out.println(p.getAge()));
//        list.forEach(System.out::println);
        /*Persion[] arr = new Persion[]{new Persion(15),new Persion(19),new Persion(12)};
        for (Persion p: arr) {
            System.out.println(p.getAge());
        }
        Arrays.sort(arr,comparator);
        for (Persion p: arr) {
            System.out.println(p.getAge());
        }*/
    }
}
class Persion {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Persion(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Persion{" +
                "age=" + age +
                '}';
    }
}

class PersionComparator implements Comparator<Persion> {
    @Override
    public int compare(Persion o1, Persion o2) {
        return o1.getAge()-o2.getAge();
    }
}