package steammachinist.mpagrengine.service.aggregation.advertisement;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.advertisement.AdCampaign;
import steammachinist.mpagrengine.dto.advertisement.AppPlatform;
import steammachinist.mpagrengine.dto.request.AdCampaignRequest;
import steammachinist.mpagrengine.dto.response.advertisement.fullinfo.AdCampaignInfoResponse;
import steammachinist.mpagrengine.dto.response.advertisement.fullinfo.AppStats;
import steammachinist.mpagrengine.service.aggregation.WbApiService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdCampaignStatsProviderService {
    private final String advertsFullInfoUri = "https://advert-api.wb.ru/adv/v1/fullstats";
    private final WbApiService apiService;

    public void provideCampaignStats(List<AdCampaign> campaigns, LocalDateTime dateTime) throws JsonProcessingException {
        ResponseEntity<AdCampaignInfoResponse[]> responseEntity = apiService.getRestTemplate().exchange(
                advertsFullInfoUri,
                HttpMethod.POST,
                apiService.setupRequestEntity(apiService.getAdKey(), formRequestBody(campaigns, dateTime)),
                AdCampaignInfoResponse[].class);

        int statusCode = responseEntity.getStatusCode().value();
        processResponse(campaigns, responseEntity.getBody());
    }

    private List<AdCampaignRequest> formRequestBody(List<AdCampaign> campaigns, LocalDateTime dateTime) {
        String yesterdayFormatted = apiService.getFormattedDate(dateTime);
        return campaigns.stream()
                .map(adCampaign -> new AdCampaignRequest(
                        Integer.parseInt(adCampaign.getCode()),
                        new String[]{yesterdayFormatted}))
                .collect(Collectors.toList());
    }

    private void processResponse(List<AdCampaign> campaigns, AdCampaignInfoResponse[] campaignInfos) {
        List<AdCampaign> filledCampaigns = new ArrayList<>();
        for (AdCampaign campaign : campaigns) {
            filledCampaigns.addAll(Arrays.stream(campaignInfos)

                    .filter(adCampaignInfoResponse ->
                            adCampaignInfoResponse.getAdvertId() == Integer.parseInt(campaign.getCode()))

                    .flatMap(adCampaignInfoResponse ->
                            adCampaignInfoResponse.getDays().get(0).getApps().stream()

                                    .flatMap((Function<AppStats, Stream<AdCampaign>>) appStats ->
                                            appStats.getNm().stream()

                                                    .map(nm -> {
                                                        AdCampaign adCampaign;
                                                        try {
                                                            adCampaign = (AdCampaign) campaign.clone();
                                                        } catch (CloneNotSupportedException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                        adCampaign.setProductCode(String.valueOf(nm.getNmId()));
                                                        adCampaign.setProductName(nm.getName());
                                                        adCampaign.setViews(nm.getViews());
                                                        adCampaign.setClicks(nm.getClicks());
                                                        adCampaign.setCtr(nm.getCtr());
                                                        adCampaign.setCpc(nm.getCpc());
                                                        adCampaign.setCarts(nm.getAtbs());
                                                        adCampaign.setOrders(nm.getOrders());
                                                        adCampaign.setOrderedItems(nm.getShks());
                                                        adCampaign.setOrdersSum(nm.getSum_price());
                                                        adCampaign.setCost(nm.getSum());
                                                        adCampaign.setCr(nm.getCr());
                                                        adCampaign.setPlatform(AppPlatform.fromInt(appStats.getAppType()));
                                                        return adCampaign;
                                                    }))).toList());
        }
        campaigns.clear();
        campaigns.addAll(filledCampaigns);
    }
}
