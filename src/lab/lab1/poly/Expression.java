package lab.poly;

import java.util.Objects;
import java.util.TreeMap;

public final class Expression {
    private final TreeMap<Integer, Term> expToTerm = new TreeMap<>();

    public Expression(String str) {
        String[] coeAndExp = str.split(" ");
        for (int i = 0; i < coeAndExp.length; ) {
            // 解析出系数 coe 和指数 exp
            int coe = Integer.parseInt(coeAndExp[i++]);
            int exp = Integer.parseInt(coeAndExp[i++]);
            // 添加到 TreeMap 中，将指数相同的项进行合并
            Term toMerge = expToTerm.get(exp);
            Term toPut;
            if (toMerge == null) {
                toPut = new Term(coe, exp);
            } else {
                toPut = new Term(coe + toMerge.getCoe(), exp);
            }
            expToTerm.put(exp, toPut);
        }
        // 去除系数为0的项
        expToTerm.entrySet().removeIf(entry -> entry.getValue().getCoe() == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Expression)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Expression that = (Expression) o;
        return Objects.equals(expToTerm, that.expToTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expToTerm);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (expToTerm.isEmpty()) {
            return "0";
        } else {
            for (Integer i : expToTerm.keySet()) {
                Term term = expToTerm.get(i);
                if (term.getCoe() > 0) {
                    sb.append("+");
                }
                sb.append(term.toString());
            }
            String tmp = sb.toString();
            if (tmp.startsWith("+")) {
                return tmp.substring(1);
            } else {
                return tmp;
            }
        }
    }
}
