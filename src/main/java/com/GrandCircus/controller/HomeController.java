package com.GrandCircus.controller;

import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apis.LyftPublicApi;
import com.lyft.networking.apis.LyftPublicApiRx; //thought maybe this needed to be imported? guess not...
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.List;



@Controller
public class HomeController {

    @RequestMapping("/")
    public String lyftCode(Model model) { //testing the 'nearby driver' function

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("mZOUI6oBEYPd")
                .setClientToken("gAAAAABZH1Z6trZYDn3zSUpGIU6ctNuIDDzaXo0kUJW7Q4jdcCIv2eycPxtRZmic_br1YZfeQWkqurVcEW2t5uL3IVdO1XH9huKDW4tG0-Ya5xyUv_-95eQmHlRGgB8kFSrNxoCa-OQdvSP_ApTngzBZr5yDDkhKx_KIxXRS6E_U46tgc1z9fcM=")
                .build();
        try {
        LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
        Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(37.7833, -122.4167, "CLASSIC", 37.7794703, -122.4233223);
            Response<CostEstimateResponse> results = costEstimateCall.execute();
            CostEstimateResponse body = results.body();
            List<CostEstimate> prices = body.cost_estimates;
            for (CostEstimate i: prices){
                System.out.println(i.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "welcome";
    }
}