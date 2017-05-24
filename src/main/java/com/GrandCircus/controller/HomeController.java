package com.GrandCircus.controller;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.*;
import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx; //thought maybe this needed to be imported? guess not...
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;


@Controller
public class HomeController {

    @RequestMapping("/")
    public String lyftPrices(Model model) { //CODE FOR EST MIN/MAX PRICE FOR RIDE; BEFORE PURCHASE

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(37.7833, -122.4167, "lyft", 37.7794703, -122.4233223);
            Response<CostEstimateResponse> results = costEstimateCall.execute();
            CostEstimateResponse body = results.body();
            List<CostEstimate> prices = body.cost_estimates;
            String displayPriceMin = "";
            String displayPriceMax = "";

            for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                displayPriceMin = (String.valueOf(costEstimate.estimated_cost_cents_min / 100) + " $");
                displayPriceMax = (String.valueOf(costEstimate.estimated_cost_cents_max / 100) + " $");
            }

            model.addAttribute("displayPriceMin", displayPriceMin);
            model.addAttribute("displayPriceMax", displayPriceMax);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(37.7833, -122.4167, "lyft_plus", 37.7794703, -122.4233223);
            Response<CostEstimateResponse> results = costEstimateCall.execute();
            CostEstimateResponse body = results.body();
            List<CostEstimate> prices = body.cost_estimates;
            String displayPriceMinPlus = "";
            String displayPriceMaxPlus = "";

            for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                displayPriceMinPlus = (String.valueOf(costEstimate.estimated_cost_cents_min / 100) + " $");
                displayPriceMaxPlus = (String.valueOf(costEstimate.estimated_cost_cents_max / 100) + " $");
            }

            model.addAttribute("displayPriceMinPlus", displayPriceMinPlus);
            model.addAttribute("displayPriceMaxPlus", displayPriceMaxPlus);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return "lyftPrices";
    }

    @RequestMapping("/driverETA")
    public String lyftTime(Model model) { //CODE FOR DRIVER ETA
        //ADDED THIS FOR GEOCODING TEST
        try {
            HttpClient http = HttpClientBuilder.create().build();
            HttpHost host = new HttpHost("maps.googleapis.com", 443, "https");
            HttpGet getPage = new HttpGet("/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&" +
                    "key=AIzaSyDCxhWezLy106rEfJyq-R6iPIYY0tK6lLw");
            HttpResponse resp = http.execute(host, getPage);

            String jsonString = EntityUtils.toString(resp.getEntity());
            System.out.println(jsonString);

            JSONObject json = new JSONObject(jsonString);
            String out1 = json.get("results").toString();
            System.out.println(out1);

            JSONObject ar = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                    getJSONObject("location");

            String lat = ar.get("lat").toString();
            Double latDouble = Double.valueOf(ar.get("lat").toString());

            String lng = ar.get("lng").toString();
            Double lngDouble = Double.valueOf(ar.get("lng").toString());

            ApiConfig apiConfig = new ApiConfig.Builder()
                    .setClientId("mZOUI6oBEYPd")
                    .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                    .build();


            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(latDouble, lngDouble, null);

            Response<EtaEstimateResponse> results = etaCall.execute();
            EtaEstimateResponse body = results.body();
            List<Eta> time = body.eta_estimates;
            String displayTime = "";

            for (Eta eta : body.eta_estimates) {
                displayTime = (String.valueOf(eta.eta_seconds / 60) + " min");
            }

            model.addAttribute("driverETA", displayTime);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "driverETA";
    }







    @RequestMapping("/rideTypes")
    public String lyftRideTypes(Model model) { //code for lyft ride types

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<RideTypesResponse> rideTypesCall = lyftPublicApi.getRidetypes(37.7833, -122.4167, null);
            Response<RideTypesResponse> results = rideTypesCall.execute();
            RideTypesResponse body = results.body();
            List<RideType> types = body.ride_types;
            String displayTypes = "";
            for (int i = 0; i < types.size(); i++) {
                displayTypes += (types.get(i));
            }
            model.addAttribute("types", displayTypes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "rideTypes";
    }

    @RequestMapping("/nearbyDriver")
    public String nearybyD(Model model) { //CODE FOR DISTANCE IN MILES AND MINUTES OF DRIVER FROM CUSTOMER, BEFORE PICKUP

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<NearbyDriversResponse> nearbyDriversCall = lyftPublicApi.getDrivers(37.7833, -122.4167);
            Response<NearbyDriversResponse> results = nearbyDriversCall.execute();

            NearbyDriversResponse body = results.body();
            List<NearbyDriversByRideType> places = body.nearby_drivers;
            //  String displayPlaces = "";
            String displayPlaces2 = "";

//            for (int i =0; i<places.size();i++) { //THIS PRINTS ALL RIDERS IN ONE STRING
//                displayPlaces += (places.get(i));
//            }

            NearbyDriversByRideType result = results.body().nearby_drivers.get(0); //THIS PRINTS ONLY ONE DRIVER AVAILABLE
            for (NearbyDriver driver : result.drivers) {                            // BUT BETTER FORMATTING
                displayPlaces2 += (driver.locations.toString());
            }

            // model.addAttribute("displayPlaces",displayPlaces);
            model.addAttribute("displayPlaces2", displayPlaces2);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "nearbyDriver";

    }

    @RequestMapping("/tilesTester")
    public String tilesTester(Model model) {
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(37.7833, -122.4167, "lyft", 37.7794703, -122.4233223);
            Response<CostEstimateResponse> results = costEstimateCall.execute();
            CostEstimateResponse body = results.body();
            List<CostEstimate> prices = body.cost_estimates;
            String displayPriceMin = "";
            String displayPriceMax = "";

            for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                displayPriceMin = (String.valueOf(costEstimate.estimated_cost_cents_min / 100) + " $");
                displayPriceMax = (String.valueOf(costEstimate.estimated_cost_cents_max / 100) + " $");
            }

            model.addAttribute("displayPriceMin", displayPriceMin);
            model.addAttribute("displayPriceMax", displayPriceMax);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(37.7833, -122.4167, "lyft_plus", 37.7794703, -122.4233223);
            Response<CostEstimateResponse> results = costEstimateCall.execute();
            CostEstimateResponse body = results.body();
            List<CostEstimate> prices = body.cost_estimates;
            String displayPriceMinPlus = "";
            String displayPriceMaxPlus = "";

            for (CostEstimate costEstimate : body.cost_estimates) { //tried 'prices' rather than 'body' but didn't like....
                displayPriceMinPlus = (String.valueOf(costEstimate.estimated_cost_cents_min / 100) + " $");
                displayPriceMaxPlus = (String.valueOf(costEstimate.estimated_cost_cents_max / 100) + " $");
            }

            model.addAttribute("displayPriceMinPlus", displayPriceMinPlus);
            model.addAttribute("displayPriceMaxPlus", displayPriceMaxPlus);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(37.7833, -122.4167, null);
            Response<EtaEstimateResponse> results = etaCall.execute();
            EtaEstimateResponse body = results.body();
            List<Eta> time = body.eta_estimates;
            String displayTime = "";

            for (Eta eta : body.eta_estimates) {
                displayTime = (String.valueOf(eta.eta_seconds / 60) + " min");
            }

            model.addAttribute("driverETA", displayTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "tilesTester";
    }

    @RequestMapping("/geoCoding")
    public String geoCoding(Model model) {
        try {
            HttpClient http = HttpClientBuilder.create().build();
            HttpHost host = new HttpHost("maps.googleapis.com", 443, "https");
            HttpGet getPage = new HttpGet("/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&" +
                    "key=AIzaSyDCxhWezLy106rEfJyq-R6iPIYY0tK6lLw");
            HttpResponse resp = http.execute(host, getPage);

            String jsonString = EntityUtils.toString(resp.getEntity());
            System.out.println(jsonString);

            JSONObject json = new JSONObject(jsonString);
            String out1 = json.get("results").toString();
            System.out.println(out1);

            JSONObject ar = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").
                    getJSONObject("location");
            System.out.println(ar.toString());
            Double lng = Double.valueOf(ar.get("lng").toString());
            Double lat = Double.valueOf(ar.get("lat").toString());
            System.out.println("lng = " + lng);
            System.out.println("lat = " + lat);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return"geoCoding";
    }

}


