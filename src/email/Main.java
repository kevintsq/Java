package email;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;

public class Main {
    public static TreeMap<String, Email> readFrom(Scanner scanner) {
        TreeMap<String, Email> info = new TreeMap<>();
        String line = "";
        while (!line.equals("END_OF_INFORMATION")) {
            line = scanner.nextLine();
            final Matcher matcher = Email.EMAIL_PATTERN.matcher(line);
            while (matcher.find()) {
                String username = matcher.group("user");
                info.put(username.toLowerCase(), new Email(username,
                        matcher.group("domain"),
                        matcher.group("dType"),
                        matcher.group("year"),
                        matcher.group("month"),
                        matcher.group("day"),
                        matcher.group("hour"),
                        matcher.group("minute"),
                        matcher.group("second"),
                        matcher.group("place")));
            }
        }
        return info;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TreeMap<String, Email> info = readFrom(scanner);
        while (scanner.hasNext()) {
            String query = scanner.next();
            List<String> cmd = Arrays.asList(scanner.nextLine().split("\\s+"));
            if (cmd.size() == 3) {
                String name = cmd.get(1);
                boolean foundPlace = false;
                boolean foundEmail = false;
                for (Map.Entry<String, Email> entry : info.entrySet()) {
                    Email email = entry.getValue();
                    if (email.getPlace().equals(cmd.get(2))) {
                        if (email.getUsername().equals(cmd.get(1))) {
                            if (query.equals("del")) {
                                info.entrySet().removeIf(e ->
                                        e.getValue().getUsername().equals(name));
                            } else {
                                System.out.println(email.query(query));
                            }
                            foundEmail = true;
                        }
                        foundPlace = true;
                        if (foundEmail) {
                            break;
                        }
                    }
                }
                if (!query.equals("del")) {
                    if (!foundPlace) {
                        System.out.println("no place exists");
                    } else if (!foundEmail) {
                        System.out.println("no username exists");
                    }
                }
            } else if (query.equals("del")) {
                info.entrySet().removeIf(entry ->
                        entry.getValue().getPlace().equals(cmd.get(3)) &&
                                entry.getValue().getDate().equals(cmd.get(2)));
            } else if (query.equals("qutime")) {
                boolean foundDate = false;
                boolean foundPlace = false;
                for (Map.Entry<String, Email> entry : info.entrySet()) {
                    Email email = entry.getValue();
                    if (email.getPlace().equals(cmd.get(3))) {
                        if (email.getDate().equals(cmd.get(2))) {
                            System.out.println(email);
                            foundDate = true;
                        }
                        foundPlace = true;
                    }
                }
                if (!foundPlace) {
                    System.out.println("no place exists");
                } else if (!foundDate) {
                    System.out.println("no email exists");
                }
            }
        }
    }
}
