package derivative.utility;

import java.math.BigInteger;

public class FormatChecker {
    /**
     * No instantiation of this pure helper class!!!
     *
     * REMEMBER: Calling to `IterableString::next` will always return the current character before
     *           the  cursor   moves  to  the  next  position,   so   caller  should  never   call
     *           `IterableString::next` to check syntax because callee calls `IterableString::next`
     *           to check syntax.
     *
     * Formalized Representation for Expression:
     *
     *     <Expression> ::= <WhiteSpace> [<AddSub> <WhiteSpace>] <Term>
     *         {<WhiteSpace> <AddSub> <WhiteSpace> <Term>} <WhiteSpace>
     *     <Term> ::= [<AddSub> <WhiteSpace>] <Factor> {<WhiteSpace> "*" <WhiteSpace> <Factor>}
     *     <Factor> ::= <VariableFactor> | <Constant> | <ExpressionFactor>
     *     <VariableFactor> ::= <PowerFunction> | <TrigFunction>
     *     <Constant> ::= [<AddSub>] <Integer>
     *     <ExpressionFactor> ::= "(" <Expression> ")"
     *     <TrigFunction> ::= "sin" | "cos"
     *         <WhiteSpace> "(" <WhiteSpace> <Factor> <WhiteSpace> ")" [<WhiteSpace> <Exponent>]
     *     <PowerFunction> ::= "x" [<WhiteSpace> <Exponent>]
     *     <Exponent> ::= "**" <WhiteSpace> <Constant>
     *     <Integer> ::= <<Digits>> // this is not BNF formula
     *     <WhiteSpace> ::= {" " | "\t"}
     *     <AddSub> ::= "+" | "-"
     */

    private static final BigInteger FIFTY = BigInteger.valueOf(50);

    public static void check(String string) throws WrongFormatException {
        if (string.equals("")) {
            throw new WrongFormatException();
        }
        IterableString input = new IterableString(string);
        checkExpression(input);
        if (input.hasNext()) {
            throw new WrongFormatException();
        }
    }

    /**
     *  <Expression> ::= <WhiteSpace> [<AddSub> <WhiteSpace>] <Term>
     *      {<WhiteSpace> <AddSub> <WhiteSpace> <Term>} <WhiteSpace>
     */
    private static void checkExpression(IterableString str) throws WrongFormatException {
        checkWhiteSpace(str);
        char current = str.current();
        if (current == '+' || current == '-') {
            str.next();
            checkWhiteSpace(str);
        }
        checkTerm(str);
        for (current = str.current(); current == '+' || current == '-'; current = str.current()) {
            str.next();
            checkWhiteSpace(str);
            checkTerm(str);
            checkWhiteSpace(str);
        }
    }

    /**
     * <Term> ::= [<AddSub> <WhiteSpace>] <Factor> {<WhiteSpace> "*" <WhiteSpace> <Factor>}
     */
    private static void checkTerm(IterableString input) throws WrongFormatException {
        char current = input.current();
        if (current == '+' || current == '-') {
            input.next();
            checkWhiteSpace(input);
        }
        checkFactor(input);
        checkWhiteSpace(input);
        while (input.current() == '*') {
            input.next();
            checkWhiteSpace(input);
            checkFactor(input);
            checkWhiteSpace(input);
        }
    }

    /**
     * <Factor> ::= <VariableFactor> | <ConstantFactor> | <ExpressionFactor>
     */
    private static void checkFactor(IterableString input) throws WrongFormatException {
        char current = input.current();
        if (current == '+' || current == '-' || Character.isDigit(current)) {
            checkConstant(input);
        } else if (current == '(') {
            checkExpressionFactor(input);
        } else {
            checkVariable(input);
        }
    }

    private static void checkExpressionFactor(IterableString input) throws WrongFormatException {
        if (input.next() != '(') {
            throw new WrongFormatException();
        }
        checkExpression(input);
        if (input.next() != ')') {
            throw new WrongFormatException();
        }
    }

    /**
     * <VariableFactor> ::= <PowerFunction> | <TrigFunction>
     */
    private static void checkVariable(IterableString input) throws WrongFormatException {
        if (input.current() == 'x') {
            checkPowerFunction(input);
        } else {
            checkTrigFunction(input);
        }
    }

    /**
     * <TrigFunction> ::= "sin" | "cos"
     *      <WhiteSpace> "(" <WhiteSpace> <Factor> <WhiteSpace> ")" [<WhiteSpace> <Exponent>]
     */
    private static void checkTrigFunction(IterableString input) throws WrongFormatException {
        if (!input.startsWith("sin") && !input.startsWith("cos")) {
            throw new WrongFormatException();
        }
        input.skipCharsBy(3);
        checkWhiteSpace(input);
        if (input.next() != '(') {
            throw new WrongFormatException();
        }
        checkWhiteSpace(input);
        checkFactor(input);
        checkWhiteSpace(input);
        if (input.next() != ')') {
            throw new WrongFormatException();
        }
        checkWhiteSpace(input);
        if (input.startsWith("**")) {
            checkExponent(input);
        }
    }

    /**
     * <PowerFunction> ::= "x" [<WhiteSpace> <Exponent>]
     */
    private static void checkPowerFunction(IterableString input) throws WrongFormatException {
        if (input.next() != 'x') {
            throw new WrongFormatException();
        }
        checkWhiteSpace(input);
        if (input.startsWith("**")) {  // "**" can be omitted
            checkExponent(input);
        }
    }

    /**
     * <Exponent> ::= "**" <WhiteSpace> <Constant>
     */
    private static void checkExponent(IterableString input) throws WrongFormatException {
        if (!input.startsWith("**")) {
            throw new WrongFormatException();
        }
        input.skipCharsBy(2); // "**"
        checkWhiteSpace(input);
        if (checkConstant(input).abs().compareTo(FIFTY) > 0) {
            throw new WrongFormatException();
        }
    }

    /**
     * <Constant> ::= [<AddSub>] <Integer>
     */
    private static BigInteger checkConstant(IterableString input) throws WrongFormatException {
        StringBuilder sb = new StringBuilder();
        char current = input.next();
        if (current == '+' || current == '-' || Character.isDigit(current)) {
            sb.append(current);
            for (char ch : input) {
                if (!Character.isDigit(ch)) {
                    input.previous();
                    break;
                }
                sb.append(ch);
            }
            try {
                return new BigInteger(sb.toString());
            } catch (NumberFormatException e) {
                throw new WrongFormatException();
            }
        } else {
            throw new WrongFormatException();
        }
    }

    private static void checkWhiteSpace(IterableString input) {
        for (char current : input) {
            if (current != ' ' && current != '\t') {
                input.previous();
                return;
            }
        }
    }
}
