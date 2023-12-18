package steammachinist.mpagrengine.service.aggregation.advertisement;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.advertisement.AdCampaign;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdAggregatorService {
    private final AdCampaignsListProviderService campaignsListProviderService;
    private final AdCampaignStatsProviderService campaignStatsProviderService;

    public List<AdCampaign> getCampaignsStatsForPreviousDay(LocalDateTime dateTime) {
        List<AdCampaign> campaigns;
        try {
            campaigns = campaignsListProviderService.getActiveCampaignsList();
            campaignStatsProviderService.provideCampaignStats(campaigns, dateTime);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return campaigns;
    }
}
