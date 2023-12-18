package steammachinist.mpagrengine.service.aggregation.advertisement;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import steammachinist.mpagrengine.dto.advertisement.AdCampaign;
import steammachinist.mpagrengine.dto.advertisement.CampaignStatus;
import steammachinist.mpagrengine.dto.response.advertisement.shortinfo.AdCampaignItem;
import steammachinist.mpagrengine.service.aggregation.WbApiService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdCampaignsListProviderService {
    private final String advertsListUri = "https://advert-api.wb.ru/adv/v0/adverts";
    private final WbApiService apiService;

    public List<AdCampaign> getActiveCampaignsList() throws JsonProcessingException {
        ResponseEntity<AdCampaignItem[]> responseEntity = apiService.getRestTemplate().exchange(
                UriComponentsBuilder.fromHttpUrl(advertsListUri)
                        .queryParam("status", CampaignStatus.ACTIVE.getCode())
                        .queryParam("limit", 500)
                        .queryParam("offset", 0)
                        .build().toUriString(),
                HttpMethod.GET,
                apiService.setupRequestEntity(apiService.getAdKey(), null),
                AdCampaignItem[].class);

        int statusCode = responseEntity.getStatusCode().value();
        return createCampaignsFromItems(responseEntity.getBody());
    }

    private List<AdCampaign> createCampaignsFromItems(AdCampaignItem[] items) {
        return Arrays.stream(items)
                .map(adCampaignItem -> new AdCampaign(
                        adCampaignItem.getAdvertId(),
                        adCampaignItem.getName(),
                        adCampaignItem.getStatus(),
                        adCampaignItem.getType()))
                .collect(Collectors.toList());
    }
}
