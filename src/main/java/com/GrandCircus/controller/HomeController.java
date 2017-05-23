package com.GrandCircus.controller;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.*;
import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx; //thought maybe this needed to be imported? guess not...
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

            for(CostEstimate costEstimate : body.cost_estimates){ //tried 'prices' rather than 'body' but didn't like....
                displayPriceMin = (String.valueOf(costEstimate.estimated_cost_cents_min/100) + " $");
                displayPriceMax = (String.valueOf(costEstimate.estimated_cost_cents_max/100) + " $");
            }

            model.addAttribute("displayPriceMin", displayPriceMin);
            model.addAttribute("displayPriceMax", displayPriceMax);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "lyftPrices";
    }

    @RequestMapping("/driverETA")
    public String lyftTime(Model model) { //CODE FOR DRIVER ETA

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
            LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
            Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(37.7833, -122.4167, null);
            Response<EtaEstimateResponse> results = etaCall.execute();
            EtaEstimateResponse body = results.body();
            List<Eta> time = body.eta_estimates;
            String displayTime = "";

            for(Eta eta : body.eta_estimates){
                displayTime = (String.valueOf(eta.eta_seconds/60) + " min");
            }

            model.addAttribute("driverETA", displayTime);
        } catch (IOException e) {
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
            for (int i = 0; i < types.size(); i++ ){
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
            for(NearbyDriver driver : result.drivers) {                            // BUT BETTER FORMATTING
                displayPlaces2 += (driver.locations.toString());
            }

           // model.addAttribute("displayPlaces",displayPlaces);
            model.addAttribute("displayPlaces2", displayPlaces2);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "nearbyDriver";

    }







}