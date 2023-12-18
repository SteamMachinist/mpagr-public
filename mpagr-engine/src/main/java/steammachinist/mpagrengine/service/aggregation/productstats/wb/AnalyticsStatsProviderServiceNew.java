package steammachinist.mpagrengine.service.aggregation.productstats.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.service.aggregation.WbApiService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsStatsProviderServiceNew implements ProductStatsProvider {
    private final String analyticsDetailsUri = "https://suppliers-api.wildberries.ru/content/v1/analytics/nm-report/detail";
    private final WbApiService apiService;

    @Override
    public void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException {

    }

    @Override
    public void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException {
        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
    }

//    public void provideStats(List<DatedDataList<ProductStats>> datedProductsStatsList) throws JsonProcessingException {
//        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
//    }

    private int[] getProductsCodes(List<ProductStats> productStats) {
        return productStats.stream()
                .map(stats -> stats.getProduct().getCode())
                .map(Integer::parseInt)
                .mapToInt(x -> x)
                .toArray();
    }

    @Override
    public boolean isCurrentDayOnly() {
        return false;
    }
}
