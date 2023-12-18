package steammachinist.mpagrengine.service.aggregation.productstats.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.dto.request.AnalyticsStatsRequest;
import steammachinist.mpagrengine.dto.request.Period;
import steammachinist.mpagrengine.dto.response.analyticsstats.AnalyticsStatsResponse;
import steammachinist.mpagrengine.dto.response.analyticsstats.HistoryItem;
import steammachinist.mpagrengine.service.aggregation.WbApiService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsStatsProviderService implements ProductStatsProvider {
    private final String analyticsStatsHistoryUri = "https://suppliers-api.wildberries.ru/content/v1/analytics/nm-report/detail/history";
    private final WbApiService apiService;

    private final int batchSize = 20;

    @Override
    public void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException {
        List<List<ProductStats>> batchedProductsStats = createBatchProductsStats(productsStats);
        Period period = preparePeriod(dateTime);

        productsStats.clear();
        for (List<ProductStats> productStatsBatch : batchedProductsStats) {
            AnalyticsStatsRequest analyticsStatsRequest = new AnalyticsStatsRequest(
                    getProductsCodes(productStatsBatch),
                    period);

            ResponseEntity<AnalyticsStatsResponse> responseEntity = apiService.getRestTemplate().exchange(
                    analyticsStatsHistoryUri,
                    HttpMethod.POST,
                    apiService.setupRequestEntity(apiService.getStandardKey(), analyticsStatsRequest),
                    AnalyticsStatsResponse.class
            );
            int statusCode = responseEntity.getStatusCode().value();
            productsStats.addAll(processResponse(productStatsBatch, Objects.requireNonNull(responseEntity.getBody())));
        }
    }

    @Override
    public void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException {
        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
    }

    @Override
    public boolean isCurrentDayOnly() {
        return false;
    }

    private List<List<ProductStats>> createBatchProductsStats(List<ProductStats> productsStats) {
        List<List<ProductStats>> batchedProductsStats = new ArrayList<>();
        for (int i = 0; i < productsStats.size(); i += batchSize) {
            int end = Math.min(i + batchSize, productsStats.size());
            List<ProductStats> batch = productsStats.subList(i, end);
            batchedProductsStats.add(new ArrayList<>(batch));
        }
        return batchedProductsStats;
    }

    private int[] getProductsCodes(List<ProductStats> productStats) {
        return productStats.stream()
                .map(stats -> stats.getProduct().getCode())
                .map(Integer::parseInt)
                .mapToInt(x -> x)
                .toArray();
    }

    private Period preparePeriod(LocalDateTime dateTime) {
        String formattedDate = apiService.getFormattedDate(dateTime);
        return new Period(formattedDate, formattedDate);
    }

    private List<ProductStats> processResponse(List<ProductStats> productsStats, AnalyticsStatsResponse response) {
        return StreamUtils.zip(productsStats.stream(), response.getData().stream(),
                (productStats, dataItem) -> {
                    HistoryItem item = dataItem.getHistory().get(0);
                    productStats.setOrders(item.getOrdersCount());
                    productStats.setViews(item.getOpenCardCount());
                    productStats.setSales(item.getBuyoutsCount());
                    return productStats;
                }).collect(Collectors.toList());
    }
}
