import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Test {

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {

        JsonNode jsonTree = null;
        System.out.println("Please select origin: ");
        String origin = scanner.nextLine();
        System.out.println("Please select destination: ");
        String destination = scanner.nextLine();
        System.out.println("Please select departure date(f.e. 2023-07-03): ");
        String departDate = scanner.nextLine();
        System.out.println("Please select return date(f.e. 2023-07-03): ");
        String returnDate = scanner.nextLine();
        System.out.println("Information succesfully written to csv file");
        System.out.println("Please fill additional information");
        jsonTree = setSearchParameters(origin, destination, departDate, returnDate);
        saveToCsvFile(jsonTree);
        saveToCsvWithSearchParameters(jsonTree);
        System.out.println("You flights filtered by you search parameters");
    }


    public static void saveToCsvWithSearchParameters(JsonNode jsonTree) throws IOException {

        FileWriter recipesFile = new FileWriter("export.csv", true);
        recipesFile.write("price, tax, out first airport, out first time depar, in first airport, " +
                "in first time arrival, first flight number, out second airport, out second time departure, in second airport arrival, in second time arrival, second flight number\n");
        ObjectMapper mapper = new JsonMapper();
        JsonNode node = mapper.readTree(String.valueOf(jsonTree));

        int i = 0;
        if (node.get("body").get("data").get("totalAvailabilities").get(i).size() > 2) {
            System.out.println("Search not available with two stops!");
            return;
        }
        System.out.println("You maximum price: ");
        double maxPrice = scanner.nextDouble();
        System.out.println("You maximum tax: ");
        double maxTax = scanner.nextDouble();
        System.out.println("Departed airport(MAD,JFK,CPH): ");
        scanner.nextLine();
        String departedAirport = scanner.nextLine();
        System.out.println("Arrived airport(AUH,FUE,MAD): ");
        String arrivedAirport = scanner.nextLine();
        System.out.println("Flight with maksmimum stops(direct/one stop): ");
        String stopNumber = scanner.nextLine();
        System.out.println("Flight class(Economy, Bussines): ");
        String flightClass = scanner.nextLine();
        System.out.println("Baggage maksimum amount: ");
        int baggageMax = scanner.nextInt();
        System.out.println("Baggage maksimum in cabin: ");
        int caibBaggageMax = scanner.nextInt();

        while (node.get("body").get("data").get("totalAvailabilities").get(i) != null) {
            String outboundSecondAirport = null;
            String outboundSecondTimeDeparture = null;
            String inboundSecondAirport = null;
            String inboundSecondTimeArrival = null;
            String secondFlightNumber = null;
            String outboundSecondAirportBack = null;
            String outboundSecondTimeDepartureBack = null;
            String inboundSecondAirportBack = null;
            String inboundSecondTimeArrivalBack = null;
            String secondFlightNumberBack = null;

            double price = node.get("body").get("data").get("totalAvailabilities").get(i).get("total").asDouble();
            double tax = node.get("body").get("data").get("journeys").get(i).get("importTaxAdl").asDouble();
            String outboundFirstAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("airportDeparture").get("code").asText();
            String outboundFirstTimeDeparture = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("dateDeparture").asText();
            String inboundFirstAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("airportArrival").get("code").asText();
            String inboundFirstTimeArrival = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("dateArrival").asText();
            String firstFlightNumber = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("number").asText();
            String flightClassScrappedInfo = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("cabinClass").get("description").asText();
            double baggageWeight = node.get("body").get("data").get("journeys").get(0).get("franchiseInformation").get("baggageWeight").get("amount").asDouble();
            double cabinBaggageWieght = node.get("body").get("data").get("journeys").get(0).get("cabinInformation").get("baggageWeight").get("amount").asDouble();

            if (node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null) {
                outboundSecondAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("airportDeparture").get("code").asText();
                outboundSecondTimeDeparture = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("dateDeparture").asText();
                inboundSecondAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("airportArrival").get("code").asText();
                inboundSecondTimeArrival = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("dateArrival").asText();
                secondFlightNumber = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("number").asText();
            }
            double taxBack = node.get("body").get("data").get("journeys").get(i + 1).get("importTaxAdl").asDouble();
            String outboundFirstAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("airportDeparture").get("code").asText();
            String outboundFirstTimeDepartureBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("dateDeparture").asText();
            String inboundFirstAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("airportArrival").get("code").asText();
            String inboundFirstTimeArrivalBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("dateArrival").asText();
            String firstFlightNumberBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("number").asText();
            if (node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null) {
                outboundSecondAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("airportDeparture").get("code").asText();
                outboundSecondTimeDepartureBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("dateDeparture").asText();
                inboundSecondAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("airportArrival").get("code").asText();
                inboundSecondTimeArrivalBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("dateArrival").asText();
                secondFlightNumberBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("number").asText();
            }
            if (maxPrice > price && maxTax > tax && departedAirport.equals(outboundFirstAirport) && arrivedAirport.equals(inboundSecondAirport) &&
                    node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null && stopNumber.equals("one stop") &&
                    flightClass.equals(flightClassScrappedInfo)
                    && baggageMax > baggageWeight && caibBaggageMax > cabinBaggageWieght
            ) {
                recipesFile.write(price + ", " + tax + ", " + outboundFirstAirport + ", " + outboundFirstTimeDeparture + ", " + inboundFirstAirport +
                        ", " + inboundFirstTimeArrival + ", " + firstFlightNumber + ", " + outboundSecondAirport + ", " + outboundSecondTimeDeparture + ", "
                        + inboundSecondAirport + ", " + inboundSecondTimeArrival + ", " + secondFlightNumber + "\n");
            }
            if (maxPrice > price && maxTax > taxBack && departedAirport.equals(outboundFirstAirportBack) && arrivedAirport.equals(inboundSecondAirportBack)
                    &&
                    node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null && stopNumber.equals("one stop") &&
                    flightClass.equals(flightClassScrappedInfo) && baggageMax > baggageWeight && caibBaggageMax > cabinBaggageWieght
            ) {
                recipesFile.write(price + ", " + taxBack + ", " + outboundFirstAirportBack + ", " + outboundFirstTimeDepartureBack + ", " + inboundFirstAirportBack +
                        ", " + inboundFirstTimeArrivalBack + ", " + firstFlightNumberBack + ", " + outboundSecondAirportBack + ", " + outboundSecondTimeDepartureBack + ", "
                        + inboundSecondAirportBack + ", " + inboundSecondTimeArrivalBack + ", " + secondFlightNumberBack + "\n");

            }
            if (maxPrice > price && maxTax > tax && departedAirport.equals(outboundFirstAirport) && arrivedAirport.equals(inboundFirstAirport) &&
                    node.get("body").get("data").get("journeys").get(i).get("flights").get(1) == null && stopNumber.equals("direct") &&
                    flightClass.equals(flightClassScrappedInfo) && baggageMax > baggageWeight && caibBaggageMax > cabinBaggageWieght
            ) {
                recipesFile.write(price + ", " + tax + ", " + outboundFirstAirport + ", " + outboundFirstTimeDeparture + ", " + inboundFirstAirport +
                        ", " + inboundFirstTimeArrival + ", " + firstFlightNumber + "\n");
            }
            if (maxPrice > price && maxTax > taxBack && departedAirport.equals(outboundFirstAirportBack) && arrivedAirport.equals(inboundFirstAirportBack) &&
                    node.get("body").get("data").get("journeys").get(i).get("flights").get(1) == null && stopNumber.equals("direct") &&
                    flightClass.equals(flightClassScrappedInfo) && baggageMax > baggageWeight && caibBaggageMax > cabinBaggageWieght
            ) {
                recipesFile.write(price + ", " + taxBack + ", " + outboundFirstAirportBack + ", " + outboundFirstTimeDepartureBack + ", " + inboundFirstAirportBack +
                        ", " + inboundFirstTimeArrivalBack + ", " + firstFlightNumberBack + "\n");
            }
            i++;
        }
        recipesFile.close();
    }

    public static void saveToCsvFile(JsonNode jsonTree) throws IOException {

        FileWriter recipesFile = new FileWriter("export.csv", true);
        recipesFile.write("price, tax, out first airport, out first time depar, in first airport, " +
                "in first time arrival, first flight number, out second airport, out second time departure, in second airport arrival, in second time arrival, second flight number\n");
        ObjectMapper mapper = new JsonMapper();
        JsonNode node = mapper.readTree(String.valueOf(jsonTree));

        int i = 0;
        if (node.get("body").get("data").get("totalAvailabilities").get(i).size() > 2) {
            System.out.println("Search not available with two stops!");
            return;
        }
        while (node.get("body").get("data").get("totalAvailabilities").get(i) != null) {
            String outboundSecondAirport = null;
            String outboundSecondTimeDeparture = null;
            String inboundSecondAirport = null;
            String inboundSecondTimeArrival = null;
            String secondFlightNumber = null;
            String outboundSecondAirportBack = null;
            String outboundSecondTimeDepartureBack = null;
            String inboundSecondAirportBack = null;
            String inboundSecondTimeArrivalBack = null;
            String secondFlightNumberBack = null;

            double price = node.get("body").get("data").get("totalAvailabilities").get(i).get("total").asDouble();
            double tax = node.get("body").get("data").get("journeys").get(i).get("importTaxAdl").asDouble();
            String outboundFirstAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("airportDeparture").get("code").asText();
            String outboundFirstTimeDeparture = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("dateDeparture").asText();
            String inboundFirstAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("airportArrival").get("code").asText();
            String inboundFirstTimeArrival = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("dateArrival").asText();
            String firstFlightNumber = node.get("body").get("data").get("journeys").get(i).get("flights").get(0).get("number").asText();
            if (node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null) {
                outboundSecondAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("airportDeparture").get("code").asText();
                outboundSecondTimeDeparture = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("dateDeparture").asText();
                inboundSecondAirport = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("airportArrival").get("code").asText();
                inboundSecondTimeArrival = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("dateArrival").asText();
                secondFlightNumber = node.get("body").get("data").get("journeys").get(i).get("flights").get(1).get("number").asText();
            }
            double taxBack = node.get("body").get("data").get("journeys").get(i + 1).get("importTaxAdl").asDouble();
            String outboundFirstAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("airportDeparture").get("code").asText();
            String outboundFirstTimeDepartureBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("dateDeparture").asText();
            String inboundFirstAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("airportArrival").get("code").asText();
            String inboundFirstTimeArrivalBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("dateArrival").asText();
            String firstFlightNumberBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(0).get("number").asText();
            if (node.get("body").get("data").get("journeys").get(i).get("flights").get(1) != null) {
                outboundSecondAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("airportDeparture").get("code").asText();
                outboundSecondTimeDepartureBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("dateDeparture").asText();
                inboundSecondAirportBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("airportArrival").get("code").asText();
                inboundSecondTimeArrivalBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("dateArrival").asText();
                secondFlightNumberBack = node.get("body").get("data").get("journeys").get(i + 1).get("flights").get(1).get("number").asText();
            }
            recipesFile.write(price + ", " + tax + ", " + outboundFirstAirport + ", " + outboundFirstTimeDeparture + ", " + inboundFirstAirport +
                    ", " + inboundFirstTimeArrival + ", " + firstFlightNumber + ", " + outboundSecondAirport + ", " + outboundSecondTimeDeparture + ", "
                    + inboundSecondAirport + ", " + inboundSecondTimeArrival + ", " + secondFlightNumber + "\n");
            recipesFile.write(price + ", " + taxBack + ", " + outboundFirstAirportBack + ", " + outboundFirstTimeDepartureBack + ", " + inboundFirstAirportBack +
                    ", " + inboundFirstTimeArrivalBack + ", " + firstFlightNumberBack + ", " + outboundSecondAirportBack + ", " + outboundSecondTimeDepartureBack + ", "
                    + inboundSecondAirportBack + ", " + inboundSecondTimeArrivalBack + ", " + secondFlightNumberBack + "\n");
            i++;
        }
        recipesFile.close();
    }

    public static JsonNode setSearchParameters(String origin, String destination, String departDate, String returnDate) throws IOException {
        String url = "http://homeworktask.infare.lt/search.php?from=" + origin + "&to=" + destination + "&depart=" + departDate + "&return=" + returnDate;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestProperty("User-Agent", "Chrome");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String html = response.toString();
        JsonNode jsonTree = new ObjectMapper().readTree(html);
        ObjectMapper mapper = new JsonMapper();
        JsonNode node = mapper.readTree(String.valueOf(jsonTree));
        return node;
    }
}
