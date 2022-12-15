package Puzzle2;

public class Main {
    public static void main(String[] args) {
        B aB = new B();
        h(aB);
    }
    static void h(A x) { A.g(x); } // x.g(x) also legal here
}

class A {
    void f() {
        System.out.println("A.f");
    }
    static void g(A y) { y.f(); }
}
class B extends A {
    void f() {
        System.out.println("B.f");
    }
}