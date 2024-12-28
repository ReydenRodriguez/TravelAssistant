import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {
    public double getExchangeRate(String sourceCurrency, String targetCurrency) {
        // url for api
        String urlString = "http://api.currencylayer.com/live?access_key=d9f9bf05382c72723495e02c4f6beb6b";

        try {
            // using the api url to access the JSON file with exchange rates
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch exchange rates. HTTP error code: " + conn.getResponseCode());
                return -1;
            }

            // manually parsing the JSON file as a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String responseText = response.toString();

            // using the string to get rates
            return calculateRate(responseText, sourceCurrency, targetCurrency);

        }
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        return -1;
    }

    // free tier of api only provides exchange rates from USD
    // requires some logic to get other exchange rates
    private double calculateRate(String response, String sourceCurrency, String targetCurrency) {
        try {
            // exchange rates from USD
            if (sourceCurrency.equals("USD") && !targetCurrency.equals("USD")) {
                return extractRate(response, targetCurrency);
            }
            // exchange rates to USD
            else if (!sourceCurrency.equals("USD") && targetCurrency.equals("USD")) {
                return 1 / extractRate(response, sourceCurrency);
            }
            // exchange rates of two foreign currencies
            else if (!sourceCurrency.equals("USD") && !targetCurrency.equals("USD")) {
                double targetRate = extractRate(response, targetCurrency);
                double sourceRate = extractRate(response, sourceCurrency);
                return targetRate / sourceRate;
            }
            // exchange rates from USD to USD
            else {
                return 1;
            }
        }
        catch (Exception e) {
            System.err.println("Error calculating exchange rate: " + e.getMessage());
            return -1;
        }
    }

    // manually searching for the rates in the string
    private double extractRate(String response, String currency) {
        try {
            String searchString = "\"" + "USD" + currency + "\":";
            int startIndex = response.indexOf(searchString);
            if (startIndex == -1) {
                System.err.println("Currency code not found: " + currency);
                return -1;
            }

            int rateStart = startIndex + searchString.length();
            int rateEnd = response.indexOf(",", rateStart);
            if (rateEnd == -1) {
                rateEnd = response.indexOf("}", rateStart);
            }

            String rateString = response.substring(rateStart, rateEnd).trim();
            return Double.parseDouble(rateString);
        }
        catch (Exception e) {
            System.err.println("Error extracting rate for currency: " + currency + ". " + e.getMessage());
        }
        return -1;
    }

    // performing the currency conversion
    public double convertCurrency(double amount, double exchangeRate) {
        if (exchangeRate == -1) {
            System.err.println("Invalid exchange rate. Conversion cannot be performed.");
            return -1;
        }
        return amount * exchangeRate;
    }
}