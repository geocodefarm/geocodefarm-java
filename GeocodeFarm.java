import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeocodeFarm {

    private String apiKey;

    public GeocodeFarm(String apiKey) {
        this.apiKey = apiKey;
    }

    // Forward Geocoding
    public GeocodeResponse forward(String address) {
        try {
            String url = "https://api.geocode.farm/forward/?key=" + this.apiKey + "&addr=" + address;
            String response = makeRequest(url);
            JSONObject jsonResponse = new JSONObject(response);
            return parseResponse(jsonResponse, "forward");
        } catch (Exception e) {
            e.printStackTrace();
            return new GeocodeResponse(false, "Request failed or timed out", null, null, null, null, null);
        }
    }

    // Reverse Geocoding
    public GeocodeResponse reverse(double lat, double lon) {
        try {
            String url = "https://api.geocode.farm/reverse/?key=" + this.apiKey + "&lat=" + lat + "&lon=" + lon;
            String response = makeRequest(url);
            JSONObject jsonResponse = new JSONObject(response);
            return parseResponse(jsonResponse, "reverse");
        } catch (Exception e) {
            e.printStackTrace();
            return new GeocodeResponse(false, "Request failed or timed out", null, null, null, null, null);
        }
    }

    // Make the HTTP request
    private String makeRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "GeocodeFarmSDK/1.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    // Parse the response and break it down
    private GeocodeResponse parseResponse(JSONObject data, String type) {
        if (data == null || !data.has("RESULTS")) {
            return new GeocodeResponse(false, "Invalid response from server", null, null, null, null, null);
        }

        JSONObject status = data.getJSONObject("STATUS");
        String statusCode = status.getString("status");
        if (!statusCode.equals("SUCCESS")) {
            return new GeocodeResponse(false, "API returned failure: " + statusCode, null, null, null, null, null);
        }

        JSONArray resultArray = data.getJSONObject("RESULTS").getJSONArray("result");
        if (resultArray.length() == 0) {
            return new GeocodeResponse(false, "No results found", null, null, null, null, null);
        }

        JSONObject resultData = resultArray.getJSONObject(0);

        if (type.equals("reverse")) {
            return parseReverse(resultData);
        } else {
            return parseForward(resultData);
        }
    }

    // Parse reverse geocoding result
    private GeocodeResponse parseReverse(JSONObject resultData) {
        return new GeocodeResponse(
            true,
            null,
            resultData.optString("house_number", null),
            resultData.optString("street_name", null),
            resultData.optString("locality", null),
            resultData.optString("formatted_address", null),
            resultData.optDouble("accuracy", 0.0)
        );
    }

    // Parse forward geocoding result
    private GeocodeResponse parseForward(JSONObject resultData) {
        JSONObject coordinates = resultData.optJSONObject("coordinates");
        return new GeocodeResponse(
            true,
            null,
            resultData.optString("address", null),
            resultData.optString("locality", null),
            resultData.optString("formatted_address", null),
            coordinates != null ? coordinates.optString("full_address", null) : null,
            resultData.optDouble("accuracy", 0.0)
        );
    }

    // GeocodeResponse class to structure the response
    public static class GeocodeResponse {
        private boolean success;
        private String error;
        private String houseNumber;
        private String streetName;
        private String locality;
        private String fullAddress;
        private double accuracy;

        public GeocodeResponse(boolean success, String error, String houseNumber, String streetName, String locality, String fullAddress, double accuracy) {
            this.success = success;
            this.error = error;
            this.houseNumber = houseNumber;
            this.streetName = streetName;
            this.locality = locality;
            this.fullAddress = fullAddress;
            this.accuracy = accuracy;
        }

        // Getters for the response data
        public boolean isSuccess() {
            return success;
        }

        public String getError() {
            return error;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public String getStreetName() {
            return streetName;
        }

        public String getLocality() {
            return locality;
        }

        public String getFullAddress() {
            return fullAddress;
        }

        public double getAccuracy() {
            return accuracy;
        }
    }
}
