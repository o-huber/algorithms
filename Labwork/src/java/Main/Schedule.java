package Main;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private String day;
    private int dayHash;
    private List<String> lessons;

    public Schedule(String day) {
        this.day = day;
        this.dayHash = day.hashCode();
        lessons = new ArrayList<>();
    }

    public String getDay() {
        return day;
    }

    public int getDayHash() {
        return dayHash;
    }

    public List<String> getLessons() {
        return lessons;
    }

    public void  setLessons(List<String> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(String lesson) {
        lessons.add(lesson);
    }

    public void clearSchedule() {
        lessons.clear();
    }
}