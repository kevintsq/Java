package lab.lab6.gcsimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyJvm myJvm = new MyJvm();
        System.out.println("Start JVM Garbage Collection Simulation.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String operation = scanner.next();
            switch (operation) {
                case "CreateObject":
                    int count = scanner.nextInt();
                    myJvm.createObject(count);
                    System.out.println("Create " + count + " Objects.");
                    break;
                case "SetUnreferenced":
                    List<Integer> unrefList = new ArrayList<>();
                    while (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        unrefList.add(id);
                        System.out.println("Set id: " + id + " Unreferenced Object.");
                    }
                    myJvm.setUnreferenced(unrefList);
                    break;
                case "RemoveUnreferenced":
                    myJvm.removeUnreferenced();
                    System.out.println("Remove Unreferenced Object.");
                    break;
                case "MinorGC":
                    myJvm.minorGC();
                    System.out.println("Execute Minor Garbage Collection.");
                    break;
                case "MajorGC":
                    myJvm.majorGC();
                    System.out.println("Execute Major Garbage Collection.");
                    break;
                case "SnapShot":
                    myJvm.getSnapShot();
                    break;
                default:
                    System.err.println("Invalid operation.");
                    break;
            }
        }
        scanner.close();
        System.out.println("End of JVM Garbage Collection Simulation.");
    }
}