package training.training1.basic.src;

import java.util.ArrayList;

public class Input {
    public static void main(final String[] args) {
        Cuboid box1 = new Cuboid(2, 3, 4);
        System.out.println(box1.getVolume());//TODO: 1
        Cuboid box2 = new Cube(2);
        System.out.println(box2.getVolume());//TODO: 2
        ArrayList<Cuboid> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new Cube(i));
        }
        ArrayList<Cuboid> newList1 = list;
        ArrayList<Cuboid> newList2 = new ArrayList<>(list);
        ArrayList<Cuboid> newList3 = (ArrayList<Cuboid>) list.clone();
        newList1.remove(2);
        newList2.remove(3);
        newList3.remove(4);
        System.out.println(newList1.get(2).getVolume());//TODO: 3
        System.out.println(newList2.get(2).getVolume());//TODO: 4
        System.out.println(newList3.get(2).getVolume());//TODO: 5
    }
}
