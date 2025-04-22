package com.restapi.assignment.services;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TourismService {
    private final RestTemplate rest;
    @Value("${rapidapi.key}")
    String rapidKey;
    @Value("${google.key}")
    String googleKey;

    public TourismResponse fetch(String city, LocalDate in, LocalDate out,
                                 BigDecimal min, BigDecimal max) {

        List<HotelDTO> hotels = searchHotels(city, in, out, min, max);
        LatLng centre        = hotels.isEmpty()
                ? geocode(city)
                : new LatLng(hotels.get(0).lat(), hotels.get(0).lng());

        List<PoiDTO> pois    = searchAttractions(centre);

        return new TourismResponse(hotels, pois);
    }

    private List<HotelDTO> searchHotels(String city, LocalDate in, LocalDate out,
                                        BigDecimal min, BigDecimal max) {

        HttpHeaders h = new HttpHeaders();
        h.set("X-RapidAPI-Host", "hotels-com-provider.p.rapidapi.com");
        h.set("X-RapidAPI-Key",  rapidKey);

        String url = UriComponentsBuilder
                .fromUriString("https://hotels-com-provider.p.rapidapi.com/v2/hotel/rooms")
                .queryParam("destination", city)
                .queryParam("checkin_date", in)
                .queryParam("checkout_date", out)
                .queryParam("adults_number", 2)
                .toUriString();

        ResponseEntity<JsonNode> res =
                rest.exchange(url, GET, new HttpEntity<>(h), JsonNode.class);

        return StreamSupport.stream(res.getBody().spliterator(), false)
                .map(j -> new HotelDTO(
                        j.at("/id").asText(),
                        j.at("/property_name").asText(),
                        new BigDecimal(j.at("/ratesSummary/minRate/value").asText("0")),
                        j.at("/map_coordinates/latitude").asDouble(),
                        j.at("/map_coordinates/longitude").asDouble(),
                        j.at("/review/score").asDouble()))
                .filter(hotel -> hotel.price().compareTo(min) >= 0 &&
                        hotel.price().compareTo(max) <= 0)
                .toList();
    }

    private List<PoiDTO> searchAttractions(LatLng centre) {
        String url = UriComponentsBuilder
                .fromUriString("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .queryParam("location", centre.lat() + "," + centre.lng())
                .queryParam("radius", 5000)
                .queryParam("type", "tourist_attraction")
                .queryParam("key", googleKey)
                .toUriString();

        JsonNode body = rest.getForObject(url, JsonNode.class);

        return StreamSupport.stream(body.at("/results").spliterator(), false)
                .limit(20)
                .map(p -> new PoiDTO(
                        p.at("/place_id").asText(),
                        p.at("/name").asText(),
                        p.at("/vicinity").asText(),
                        p.at("/geometry/location/lat").asDouble(),
                        p.at("/geometry/location/lng").asDouble()))
                .toList();
    }

    private record LatLng(double lat, double lng) {}
    private LatLng geocode(String city) { /* call Google Geocoding */ }
}
