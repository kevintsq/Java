package lab.poly;

import java.util.Objects;

public final class Term {
    private final int coe;
    private final int exp;

    public Term(int coe, int exp) {
        this.coe = coe;
        this.exp = exp;
    }

    public int getCoe() {
        return coe;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Term)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Term term = (Term) o;
        return term.coe == coe && term.exp == exp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coe, exp);
    }

    @Override
    public String toString() {
        if (coe == 0) {
            return "";
        } else if (exp == 0) {
            return String.valueOf(coe);
        } else if (coe == 1 && exp == 1) {
            return "x";
        } else if (coe == 1) {
            return "x**" + exp;
        } else if (coe == -1 && exp == 1) {
            return "-x";
        } else if (coe == -1) {
            return "-x**" + exp;
        } else if (exp == 1) {
            return coe + "*x";
        } else {
            return coe + "*x**" + exp;
        }
    }
}
