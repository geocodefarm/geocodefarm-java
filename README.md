# Geocode.Farm Java SDK

The official Java SDK for integrating with the Geocode.Farm API. This SDK provides methods for forward and reverse geocoding, allowing easy access to geolocation data from Geocode.Farm.

## Features

- **Forward Geocoding**: Convert an address into latitude and longitude.
- **Reverse Geocoding**: Convert latitude and longitude into a human-readable address.
- **Error Handling**: Handles errors gracefully, returning informative messages for easy debugging.

## Installation

To use the SDK in your Java project, you can either download the `.jar` file (see Releases) or clone the repository and build it yourself.

### Gradle

```gradle
dependencies {
    implementation 'com.geocodefarm:geocodefarm-java:4.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.geocodefarm</groupId>
    <artifactId>geocodefarm-java</artifactId>
    <version>4.0</version>
</dependency>
```

## Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/geocodefarm/geocodefarm-java.git
    ```

2. Build the SDK (using your preferred method, such as Gradle or Maven).

3. Initialize the GeocodeFarm client with your API key:
    ```java
    GeocodeFarm geocodeFarm = new GeocodeFarm("your-api-key");
    ```

## Usage

### Forward Geocoding

Use the `forward()` method to convert an address into latitude and longitude.

```java
GeocodeFarm geocodeFarm = new GeocodeFarm("your-api-key");
GeocodeFarm.GeocodeResponse response = geocodeFarm.forward("1600 Pennsylvania Ave NW, Washington, DC");

if (response.isSuccess()) {
    System.out.println("Latitude: " + response.getAccuracy());
    System.out.println("Longitude: " + response.getFullAddress());
} else {
    System.out.println("Error: " + response.getError());
}
```

### Reverse Geocoding

Use the `reverse()` method to convert latitude and longitude into a human-readable address.

```java
GeocodeFarm geocodeFarm = new GeocodeFarm("your-api-key");
GeocodeFarm.GeocodeResponse response = geocodeFarm.reverse(38.8977, -77.0365);

if (response.isSuccess()) {
    System.out.println("Address: " + response.getFullAddress());
} else {
    System.out.println("Error: " + response.getError());
}
```

## Contributing

We are open to contributions! If you find any bugs or would like to add new features, feel free to submit a pull request.

## License

This project is licensed under the [The Unlicense](http://unlicense.org/).

## Contact

For support, please reach out to us at [support@geocode.farm](mailto:support@geocode.farm).
