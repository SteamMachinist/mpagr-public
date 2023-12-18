package steammachinist.mpagrengine.service.aggregation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.advertisement.AdCampaign;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.service.aggregation.advertisement.AdAggregatorService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsAggregatorService;
import steammachinist.mpagrengine.service.googlesheets.AdCampaignsSpreadsheetService;
import steammachinist.mpagrengine.service.googlesheets.ProductsStatsSpreadsheetService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregationLauncherService implements Runnable{
    private final ProductStatsAggregatorService productAggregatorService;
    private final AdAggregatorService adAggregatorService;
    private final AdCampaignsSpreadsheetService adCampaignsSpreadsheetService;
    private final ProductsStatsSpreadsheetService productsStatsSpreadsheetService;

    @Value("${product-stats.shift-days-before}")
    private int productStatsDaysShift;

    public void aggregateProductStats(LocalDateTime currentDay) {
        List<DatedDataList<ProductStats>> productsStatsForDates
                = productAggregatorService.getAggregatedProductsStatsAtDateRange(currentDay, productStatsDaysShift);
        productsStatsSpreadsheetService.updateProductsStats(productsStatsForDates);
    }

    public void aggregateAdvertisementCampaignInfo(LocalDateTime previousDay) {
        DatedDataList<AdCampaign> datedAdCampaigns
                = new DatedDataList<>(previousDay, adAggregatorService.getCampaignsStatsForPreviousDay(previousDay));
        adCampaignsSpreadsheetService.updateAdCampaignsStats(datedAdCampaigns);
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        aggregateProductStats(now);
        aggregateAdvertisementCampaignInfo(now.minusDays(1));
    }
}
