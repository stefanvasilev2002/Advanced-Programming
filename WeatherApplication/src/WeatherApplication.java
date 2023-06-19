import java.util.Scanner;

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}
class CurrentConditionsDisplay{
    static WeatherDispatcher dispatcher;
    static boolean active;

    public CurrentConditionsDisplay(WeatherDispatcher dispatcher) {
        CurrentConditionsDisplay.dispatcher = dispatcher;
        active = true;
    }
    public static void print(){
        if (!active){
            return;
        }
        System.out.printf("Temperature: %.1fF\n", dispatcher.temperature);
        System.out.printf("Humidity: %.1f%%\n", dispatcher.humidity);
    }
}
class ForecastDisplay{
    static WeatherDispatcher dispatcher;
    static float lastPressure;
    static boolean active;
    public ForecastDisplay(WeatherDispatcher dispatcher) {
        ForecastDisplay.dispatcher = dispatcher;
        lastPressure = 0;
        active = true;
    }
    public static void print() {
        if (!active){
            return;
        }
        String forecast;
        if(lastPressure == dispatcher.pressure){
            forecast = "Same";
        }
        else if(lastPressure < dispatcher.pressure){
            forecast = "Improving";
        }
        else {
            forecast = "Cooler";
        }
        System.out.printf("Forecast: %s\n", forecast);
        lastPressure = dispatcher.pressure;
    }
}
class WeatherDispatcher {
    float temperature;
    float humidity;
    float pressure;
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.humidity = humidity;
        this.temperature = temperature;
        this.pressure = pressure;

        CurrentConditionsDisplay.print();
        ForecastDisplay.print();
        System.out.println();
    }

    public void remove(ForecastDisplay forecastDisplay) {
        ForecastDisplay.active = false;
    }
    public void remove(CurrentConditionsDisplay current) {
        CurrentConditionsDisplay.active = false;
    }
    public void register(ForecastDisplay forecastDisplay) {
        ForecastDisplay.active = true;
    }
    public void register(CurrentConditionsDisplay current) {
        CurrentConditionsDisplay.active = true;
    }
}