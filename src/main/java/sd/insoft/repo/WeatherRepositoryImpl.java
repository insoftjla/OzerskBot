package sd.insoft.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import sd.insoft.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@Repository
@PropertySource("classpath:httpQuery.properties")
public class WeatherRepositoryImpl implements WeatherRepository {

    Logger log = LoggerFactory.getLogger(WeatherRepositoryImpl.class);

    @Value("${weather.url}")
    private String url;


    @Override
    public Weather getWeather() {

        JsonNode mainNode = null;
        JsonNode windNode = null;
        try {
            String jsonString = getJsonString(url);
            mainNode = new ObjectMapper().readTree(jsonString).get("main");
            windNode = new ObjectMapper().readTree(jsonString).get("wind");
        } catch (IOException e) {
            log.error("Exception:{}",e);
        }

        Weather weather = new Weather(
                mainNode.get("temp").asDouble(),
                mainNode.get("feels_like").asInt(),
                mainNode.get("temp_min").asDouble(),
                mainNode.get("temp_max").asDouble(),
                mainNode.get("pressure").asInt(),
                mainNode.get("humidity").asInt(),
                windNode.get("speed").asDouble());

        return weather;
    }
}
