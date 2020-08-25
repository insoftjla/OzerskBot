package sd.insoft.model;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Company {

    private String name;
    private String description;
    private String address;
    private String url;
    private String phone;
    private String hoursText;
    private Map<String, WorkingDay> workingDays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHoursText() {
        return hoursText;
    }

    public void setHoursText(String hoursText) {
        this.hoursText = hoursText;
    }

    public Map<String, WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Map<String, WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public void addWorkingDay(String name, String from, String to){
        if (workingDays == null) workingDays = new HashMap<>();
        workingDays.put(name, new WorkingDay(name, from, to));
    }

    public WorkingDay getWorkingDaysByWeekDayName(String weekDayName){
        if(workingDays == null) return null;
        return workingDays.get(weekDayName);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", hoursText='" + hoursText + '\'' +
                ", workingDays=" + workingDays +
                '}';
    }

    static class WorkingDay {

        private final String name;
        private final LocalTime from;
        private final LocalTime to;

        WorkingDay(String name, String from, String to){
            this.name = name;
            String[] fromSplit = from.split(":");
            String[] toSplit = to.split(":");
            this.from = LocalTime.of(
                    Integer.parseInt(fromSplit[0]),
                    Integer.parseInt(fromSplit[1])
            );
            this.to = LocalTime.of(
                    Integer.parseInt(toSplit[0]),
                    Integer.parseInt(toSplit[1])
            );
        }

        public String getName() {
            return name;
        }

        public LocalTime getFrom() {
            return from;
        }

        public LocalTime getTo() {
            return to;
        }
    }
}
