package sd.insoft.repo;

import sd.insoft.model.Weather;

public interface WeatherRepository extends HttpObjectRepository{
    Weather getWeather();
}
