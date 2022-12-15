package derivative.utility;

import derivative.Compound;
import derivative.compound.Add;
import derivative.compound.Cosine;
import derivative.compound.Multiply;
import derivative.compound.Negate;
import derivative.compound.Power;
import derivative.compound.Sine;
import derivative.atom.Constant;
import derivative.atom.Variable;

import java.math.BigInteger;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    /**
     * No instantiation of this pure helper class!!!
     */

    private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?\\d+");

    private static final char[][] PRIORITY_TABLE = { // [栈顶][当前]
        //[栈顶]   +    -    *    ^  neg  sin  cos  (    )   $ [当前]
        {/* + */ '>', '>', '<', '<', '<', '<', '<', '<', '>', '>'},
        {/* - */ '>', '>', '<', '<', '<', '<', '<', '<', '>', '>'},
        {/* * */ '>', '>', '>', '<', '<', '<', '<', '<', '>', '>'},
        {/* ^ */ '>', '>', '>', '>', '<', '<', '<', '<', '>', '>'},
        {/*neg*/ '>', '>', '>', '<', '<', '<', '<', '<', '>', '>'},
        {/*sin*/ '>', '>', '>', '>', ' ', ' ', ' ', '<', '>', '>'},
        {/*cos*/ '>', '>', '>', '>', ' ', ' ', ' ', '<', '>', '>'},
        {/* ( */ '<', '<', '<', '<', '<', '<', '<', '<', '=', ' '},
        {/* ) */ ' ', ' ', ' ', ' ', '<', ' ', ' ', ' ', ' ', ' '},
        {/* $ */ '<', '<', '<', '<', '<', '<', '<', '<', ' ', '='}};

    private static int operator2rank(char op) { //由运算符转译出编号
        switch (op) {
            case '+': return Operator.ADD.ordinal();
            case '-': return Operator.SUB.ordinal();
            case '*': return Operator.MUL.ordinal();
            case '^': return Operator.POW.ordinal();
            case '(': return Operator.L_P.ordinal();
            case ')': return Operator.R_P.ordinal();
            case 's': return Operator.SIN.ordinal();
            case 'c': return Operator.COS.ordinal();
            case 'n': return Operator.NEG.ordinal();
            case '$': return Operator.EOE.ordinal();
            default: return -1;
        }
    }

    private static char orderBetween(char op1, char op2) { //比较两个运算符之间的优先级
        return PRIORITY_TABLE[operator2rank(op1)][operator2rank(op2)];
    }

    private static String scanToEndOfInt(IterableString expression) {
        Matcher matcher = INTEGER_PATTERN.matcher(expression.toString());
        matcher.find();
        expression.skipCharsBy(matcher.end());
        return matcher.group();
    }

    public static String getRpnFrom(String expr) {
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('$');
        StringBuilder rpn = new StringBuilder(150);
        IterableString expression = new IterableString(expr + '$');
        while (!operatorStack.empty()) { //在运算符栈非空之前，逐个处理表达式中各字符
            char current = expression.current();
            if (Character.isDigit(current)) { //若当前字符为操作数
                rpn.append(scanToEndOfInt(expression));
                rpn.append(' ');
            } else if (current == 'x') {
                rpn.append('x');
                expression.next();
            } else { //若当前字符为运算符
                switch (orderBetween(operatorStack.lastElement(), current)) {
                    // 视其与栈顶运算符之间优先级高低分别处理
                    case '<': //栈顶运算符优先级更低时
                        operatorStack.push(current); //当前运算符进栈
                        expression.next();
                        break;
                    case '=': //优先级相等（当前运算符为右括号或者尾部哨兵'\0'）时
                        operatorStack.pop();
                        expression.next(); //脱括号并接收下一个字符
                        break;
                    case '>': //栈顶运算符优先级更高时
                        rpn.append(operatorStack.pop()); //栈顶运算符出栈进入 RPN
                        // expression.next();
                        break;
                    default:
                        System.exit(-1);
                }
            }
        }
        return rpn.toString();
    }

    public static Compound createTree(String string) {
        Stack<Compound> stack = new Stack<>();
        IterableString rpn = new IterableString(string);
        while (rpn.hasNext()) {
            char current = rpn.current();
            if (current == ' ') {
                rpn.next();
            } else if (Character.isDigit(current)) {
                stack.push(new Constant(new BigInteger(scanToEndOfInt(rpn))));
            } else if (current == 'x') {
                stack.push(new Variable());
                rpn.next();
            } else {
                Compound right;
                Compound left;
                switch (current) {
                    case '+':
                        right = stack.pop();
                        left = stack.pop();
                        stack.push(new Add(left, right));
                        break;
                    case '-':
                        right = stack.pop();
                        left = stack.pop();
                        stack.push(new Add(left, new Negate(right)));
                        break;
                    case '*':
                        right = stack.pop();
                        left = stack.pop();
                        stack.push(new Multiply(left, right));
                        break;
                    case '^':
                        right = stack.pop();
                        left = stack.pop();
                        stack.push(new Power(left, new Constant(new BigInteger(right.toString()))));
                        break;
                    case 's':
                        stack.push(new Sine(stack.pop()));
                        break;
                    case 'c':
                        stack.push(new Cosine(stack.pop()));
                        break;
                    case 'n':
                        stack.push(new Negate(stack.pop()));
                        break;
                    default:
                        System.exit(-1);
                }
                rpn.next();
            }
        }
        return stack.pop();
    }

    public static String preProcess(String string) {
        return string.replaceAll("\\s+", "")
                .replaceAll("\\+\\+", "+")
                .replaceAll("-\\+", "-")
                .replaceAll("--", "+")
                .replaceAll("\\+\\+", "+")
                .replaceAll("\\+-", "-")
                .replaceAll("\\*\\*", "^")
                .replaceAll("\\^-", "^n")
                .replaceAll("\\^\\+", "^")
                .replaceAll("\\(-", "(n")
                .replaceAll("\\(\\+", "(")
                .replaceAll("\\*-", "*n")
                .replaceAll("\\*\\+", "*")
                .replaceAll("^-", "n")
                .replaceAll("^\\+", "")
                .replaceAll("sin", "s")
                .replaceAll("cos", "c");
    }

    public static Compound parse(String string) throws WrongFormatException {
        FormatChecker.check(string);
        return createTree(getRpnFrom(preProcess(string)));
    }
}
