package email;

// import java.util.Comparator;

import java.util.regex.Pattern;

public class Email implements Comparable<Email> {
    static final String EMAIL_REGEX =
        "(?:(?:(?:(?<second>\\d{2}):)?(?<minute>\\d{2}):)?(?<hour>\\d{2})-)?" +
        "(?<day>\\d{2})-(?<month>\\d{2})-(?<year>\\d{4})-(?<user>[a-zA-Z-]+)" +
        "(?<domain>@(?<dType>[a-zA-Z\\d]+)\\.(?:[a-zA-Z\\d]+\\.)*[a-zA-Z\\d]+)-(?<place>[a-zA-Z]+)";
    static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    static final String REGEX_A = "a{2,3}b{2,4}a{2,4}c{2,3}";
    static final Pattern PATTERN_A = Pattern.compile(REGEX_A);
    static final String REGEX_B = "a{2,3}(?:ba){0,100}(?:bc){2,4}";
    static final Pattern PATTERN_B = Pattern.compile(REGEX_B);
    static final String REGEX_D_PREFIX = "^a{0,3}b{1,100}c{2,3}.*";
    static final Pattern PATTERN_D_PREFIX = Pattern.compile(REGEX_D_PREFIX);
    static final String REGEX_D_SUFFIX = ".*b{1,2}a{1,2}c{0,3}$";
    static final Pattern PATTERN_D_SUFFIX = Pattern.compile(REGEX_D_SUFFIX);
    static final String REGEX_E = "a.*b.*b.*c.*b.*c.*c.*";
    static final Pattern PATTERN_E = Pattern.compile(REGEX_E);

    private final String username;
    private final String fullDomain;
    private final String date;
    private final String domainType;
    private final String year;
    private final String month;
    private final String day;
    private final String hour;
    private final String minute;
    private final String second;
    private final String place;

    public Email(String username,
                 String fullDomain,
                 String domainType,
                 String year,
                 String month,
                 String day,
                 String hour,
                 String minute,
                 String second,
                 String place) {
        this.username = username.toLowerCase();
        this.fullDomain = fullDomain;
        this.date = year + "-" + month + "-" + day;
        this.domainType = domainType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getFullDomain() {
        return fullDomain;
    }

    public String getDomainType() {
        return domainType;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getSecond() {
        return second;
    }

    public String getPlace() {
        return place;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(username);
        sb.append(fullDomain);
        sb.append(" ");
        sb.append(date);
        if (hour != null) {
            sb.append("-");
            sb.append(hour);
            if (minute != null) {
                sb.append(":");
                sb.append(minute);
                if (second != null) {
                    sb.append(":");
                    sb.append(second);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Email o) {
        return username.compareTo(o.username);
    }

    public String query(String query) {
        switch (query) {
            case "qutype": return usernameType();
            case "qdtype": return domainType;
            case "qutime": return getDate();
            case "qyear": return year;
            case "qmonth": return month;
            case "qday": return day;
            case "qhour": return hour;
            case "qminute": return minute;
            case "qsec": return second;
            default: return null;
        }
    }

    private String usernameType() {
        int cnt = 0;
        StringBuilder type = new StringBuilder(" ");
        if (PATTERN_A.matcher(username).find()) {
            cnt++;
            type.append("A");
        }
        if (PATTERN_B.matcher(username).find()) {
            cnt++;
            type.append("B");
        }
        if (PATTERN_B.matcher(username.toLowerCase()).find()) {
            cnt++;
            type.append("C");
        }
        if (PATTERN_D_PREFIX.matcher(username).find() &&
            PATTERN_D_SUFFIX.matcher(username.toLowerCase()).find()) {
            cnt++;
            type.append("D");
        }
        if (PATTERN_E.matcher(username).find()) {
            cnt++;
            type.append("E");
        }
        return cnt + type.toString();
    }

    // public static class PassengerComparator implements Comparator<Email> {
    //     @Override
    //     public int compare(Email o1, Email o2) {
    //         return o1.compareTo(o2);
    //     }
    // }
}
