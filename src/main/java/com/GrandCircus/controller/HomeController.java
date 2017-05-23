package com.GrandCircus.controller;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
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
    public String lyftPrices(Model model) { //CODE FOR LYFT PRICES

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
            String displayPrice = "";
           // for (CostEstimate i : prices) { //does this need to be a regular for loop?
            for (int i = 0; i < prices.size(); i++ ){
               displayPrice += (prices.get(i));
                //System.out.println(String.valueOf(i.estimated_distance_miles)); DOCS SEEM TO SUGGEST THIS FORMAT
            }
            model.addAttribute("prices", displayPrice);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "lyftPrices";
    }

    @RequestMapping("/driverETA")
    public String lyftTime(Model model) { //CODE FOR driver ETA

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
            for (int i = 0; i < time.size(); i++ ){
                displayTime += (time.get(i));
            }
            model.addAttribute("driverETA", displayTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "driverETA";
    }




}