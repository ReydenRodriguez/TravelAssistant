import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TravelAdvisory {
    public String getTravelAdvisory(String countryName) {
        // url for api
        String urlString = "https://www.travel-advisory.info/api";

        try {
            // using the api url to access the JSON file with exchange rates
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch travel advisory. HTTP error code: " + conn.getResponseCode());
                return "error";
            }

            // manually parsing the JSON file as a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // using the string to get advisory message
            String responseText = response.toString();
            return  extractMessage(responseText, countryName);
        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        return "error";
    }

    // manually searching for the advisory message in the string
    private String extractMessage(String response, String countryName) {
        try {
            // locate the country
            String searchString = "\"name\":\"" + countryName + "\"";
            int countryStartIndex = response.indexOf(searchString);
            if (countryStartIndex == -1) {
                System.err.println("Country not found: " + countryName);
                return "error";
            }

            // locate the message
            int messageStartIndex = response.indexOf("\"message\":\"", countryStartIndex);
            messageStartIndex += "\"message\":\"".length();
            int messageEndIndex = response.indexOf("\"", messageStartIndex);
            String advisoryMessage = response.substring(messageStartIndex, messageEndIndex).trim();

            return advisoryMessage;
        }
        catch (Exception e) {
            System.err.println("Error extracting travel advisory for: " + countryName + ". " + e.getMessage());
        }
        return "error";
    }
}