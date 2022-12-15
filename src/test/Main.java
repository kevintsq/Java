package test;  // TODO: delete this line

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//import static java.lang.Math.abs;

public class Main {
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static ArrayList<Integer> getPrimes(int n) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
//        Scanner in = new Scanner(System.in);
//        int n = in.nextInt();
//        ArrayList<Integer> primes = getPrimes(n);
//        BigInteger sum = new BigInteger("0");
//        HashMap<Integer, BigInteger> cache = new HashMap<>();
//        for (int i = 1; i <= n; i++) {
//            for (int j: primes) {
//                if (j < i) {
//                    if (cache.containsKey(j)) {
//                        sum = sum.add(cache.get(j));
//                    } else {
//                        BigInteger tmp = new BigInteger(String.valueOf(j)).pow(2);
////                    System.out.println(tmp);
//                        sum = sum.add(tmp);
//                        cache.put(j, tmp);
//                    }
//                }
//            }
//            if (cache.containsKey(i)) {
//                sum = sum.add(cache.get(i));
//            } else {
//                BigInteger tmp = new BigInteger(String.valueOf(i)).pow(2);
////                    System.out.println(tmp);
//                sum = sum.add(tmp);
//                cache.put(i, tmp);
//            }
//        }
////        System.out.println(sum);
//        System.out.println(sum.mod(new BigInteger("1000000007")));
    }
}
