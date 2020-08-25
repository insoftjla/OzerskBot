package sd.insoft.model;

public class Weather {
    private double temp;
    private int feelsLike;
    private double tempMin;
    private double tempMax;
    private int pressure;
    private int humidity;
    private double windSpeed;

    public Weather(double temp, int feelsLike, double tempMin, double tempMax, int pressure, int humidity, double windSpeed) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public double getTemp() {
        return Math.round(temp);
    }

    public int getFeelsLike() {
        return Math.round(feelsLike);
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public int getPressure() {
        return (int) (pressure * 0.76d);
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return Math.round(windSpeed);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temp=" + temp +
                ", feelsLike=" + feelsLike +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
