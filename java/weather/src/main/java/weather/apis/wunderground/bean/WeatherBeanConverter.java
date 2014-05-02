package weather.apis.wunderground.bean;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class WeatherBeanConverter {

    public WeatherBean convert(InputStream in)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        WeatherBean weather = mapper.readValue(in, WeatherBean.class);
        return weather;
    }

}
