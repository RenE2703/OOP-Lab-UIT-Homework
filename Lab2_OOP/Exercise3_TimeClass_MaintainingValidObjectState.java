class Time {
    private int hour;
    private int minute;
    private int second;

    public Time() {
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
    }

    public Time(int hour, int minute, int second) {
        this.hour = (hour >= 0 && hour <= 23) ? hour : 0;
        this.minute = (minute >= 0 && minute <= 59) ? minute : 0;
        this.second = (second >= 0 && second <= 59) ? second : 0;
    }

    private int toTotalSeconds() {
        return hour * 3600 + minute * 60 + second;
    }

    private void setFromTotalSeconds(int total) {
        int daySeconds = 24 * 3600;
        total = ((total % daySeconds) + daySeconds) % daySeconds;
        hour = total / 3600;
        minute = (total % 3600) / 60;
        second = total % 60;
    }

    public void addSeconds(int seconds) {
        setFromTotalSeconds(toTotalSeconds() + seconds);
    }

    public void subtractSeconds(int seconds) {
        setFromTotalSeconds(toTotalSeconds() - seconds);
    }

    public void display() {
        System.out.println(String.format("%02d:%02d:%02d", hour, minute, second));
    }
}

public class Exercise3_TimeClass_MaintainingValidObjectState {
    public static void main(String[] args) {
        Time t0 = new Time();
        System.out.print("Default time: ");
        t0.display();

        Time invalid = new Time(25, 70, -5);
        System.out.print("Invalid input replaced by: ");
        invalid.display();

        Time t1 = new Time(23, 59, 50);
        System.out.print("Before: ");
        t1.display();
        t1.addSeconds(20);
        System.out.print("23:59:50 + 20 seconds = ");
        t1.display();

        Time t2 = new Time(0, 0, 10);
        System.out.print("Before: ");
        t2.display();
        t2.subtractSeconds(20);
        System.out.print("00:00:10 - 20 seconds = ");
        t2.display();

        Time t3 = new Time(12, 30, 45);
        t3.addSeconds(90000);
        System.out.print("12:30:45 + 90000 seconds = ");
        t3.display();
    }
}
