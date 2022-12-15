package Puzzle1;

public class Main {

    public static void main(String[] args) {
        B aB = new B();
        h(aB);
    }

    static void h(A x) { x.g(); }
}

class A {
    void f() {
        System.out.println("A.f");
    }
    void g() { f(); /* or this.f() */ }
}

class B extends A {
    void f() {
        System.out.println("B.f");
    }
}