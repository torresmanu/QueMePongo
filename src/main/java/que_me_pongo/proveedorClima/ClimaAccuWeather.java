package que_me_pongo.proveedorClima;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ClimaAccuWeather extends APIProviders  {
	@Override
	protected String baseURL() {
		//Si hace falta se puede hardcodear, 05/08/2019 es 7894
		String cityCode = "7894";//getCityCode();
		//awForecastAPIURL=http://dataservice.accuweather.com/forecasts/v1/daily/5day
		return System.getenv("awForecastAPIURL") + "/" + cityCode;
	}

	@Override
	protected MultivaluedMap<String, String> queryParams() {
		//awAPIKey=YvIhxAway23Sl6WGzmUMltTS2ZpAn0oM
		MultivaluedMap<String, String> map = new MultivaluedMapImpl();
		map.add("apikey", System.getenv("awAPIKey"));
		map.add("metric", "true");
		return map;
	}

	@Override
	protected int maxAmountOfDays() {
		return 4;
	}

	@Override
	protected List<PronosticoClima> proccessJson(JsonElement obj) {
		JsonArray dailyWeathers = obj.getAsJsonObject().get("DailyForecasts").getAsJsonArray();
    
    return mapToEstadoClima(dailyWeathers);
		
	}
	
	private String getCityCode() {
		//awSearchAPIURL=http://dataservice.accuweather.com/locations/v1/cities/search
		String codeURL = System.getenv("awSearchAPIURL");
		
		MultivaluedMap<String, String> map = new MultivaluedMapImpl();
		map.add("apikey", System.getenv("awAPIKey"));
		map.add("q", "buenos aires, argentina");
		
		JsonElement je = runQuery(codeURL, map);
		
		return je.getAsJsonArray().get(0).getAsJsonObject().get("Key").getAsString();
	}
	
	
	private List<PronosticoClima> mapToEstadoClima(JsonArray weathers) {
		List<PronosticoClima> retorno = new LinkedList<PronosticoClima>();
		for(JsonElement weather : weathers) {
			JsonObject obj = weather.getAsJsonObject();
			LocalDateTime fecha = LocalDateTime.parse(obj.get("Date").getAsString().substring(0, 19));
			JsonObject temperatura = obj.get("Temperature").getAsJsonObject();
			double maxima = temperatura.get("Maximum").getAsJsonObject().get("Value").getAsDouble(), 
						 minima = temperatura.get("Minimum").getAsJsonObject().get("Value").getAsDouble();
			retorno.add(new PronosticoClima(fecha, (maxima + minima) / 2));
		}
		return retorno;
	}
	
	//TODO sacar estos metodos si ya no sirven
	private double temperaturaPromedio(JsonArray weathers, LocalDate date) {
  	List<Double> predictions = StreamSupport.stream(weathers.spliterator(), false).
  													filter(dailyWeather -> this.weatherIsOf(date, dailyWeather)).
  													<Double>map(dailyWeather -> this.getTempOfWeather(dailyWeather)).
  													collect(Collectors.toList());
  													
  	return predictions.stream().reduce(.0, Double::sum) / predictions.size();
  }
  
  private boolean weatherIsOf(LocalDate date, JsonElement dailyWeather) {
  	String dailyDate = dailyWeather.getAsJsonObject().get("Date").getAsString();
  	return LocalDate.parse(dailyDate.substring(0, 10)).isEqual(date);
  }
  
  private double getTempOfWeather(JsonElement dailyWeather) {
  	JsonObject temperatura = dailyWeather.getAsJsonObject().get("Temperature").getAsJsonObject();
  	double minima = temperatura.get("Minimum").getAsJsonObject().get("Value").getAsDouble();
  	double maxima = temperatura.get("Maximum").getAsJsonObject().get("Value").getAsDouble();
  	return (minima + maxima) / 2;
  }

}
