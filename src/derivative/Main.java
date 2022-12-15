package derivative;

import derivative.utility.ExpressionParser;
import derivative.utility.WrongFormatException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        try {
            Compound expression = ExpressionParser.parse(string);
            expression = expression.takeDerivative();
            System.out.println(expression);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}
