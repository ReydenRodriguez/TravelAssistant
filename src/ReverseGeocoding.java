import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReverseGeocoding {
    public String getCountryCode(double latitude, double longitude) {
        // url for api
        String lat = Double.toString(latitude);
        String lng = Double.toString(longitude);
        String urlString = "http://api.geonames.org/countryCode?lat=" + lat + "&lng=" + lng + "&username=mkwon01";

        try {
            // using the api url to access country code of the lat/long
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch exchange rates. HTTP error code: " + conn.getResponseCode());
                return "error";
            }

            // parsing
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String responseText = response.toString();
            return responseText;
        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        return "error";
    }
}