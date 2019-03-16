package ro.cosma.andrei.sunnydays.bean;

/*Created by Cosma Andrei
* 9/22/2017*/
public class ChartEntryBean {


    double value;
    String time;

    public ChartEntryBean(double value, String time) {
        this.value = value;
        this.time = time;

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
