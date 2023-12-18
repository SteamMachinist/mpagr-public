package steammachinist.mpagrengine.service.aggregation.productstats.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.dto.response.stocks.StocksItem;
import steammachinist.mpagrengine.service.aggregation.WbApiService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StocksProviderService implements ProductStatsProvider {
    private final String stocksUri = "https://statistics-api.wildberries.ru/api/v1/supplier/stocks";
    private final WbApiService apiService;

    @Override
    public void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException {
        String formattedToday = apiService.getFormattedDate(dateTime.minusDays(1));

        ResponseEntity<StocksItem[]> responseEntity = apiService.getRestTemplate().exchange(
                UriComponentsBuilder.fromHttpUrl(stocksUri)
                        .queryParam("dateFrom", formattedToday).build().toUriString(),
                HttpMethod.GET,
                apiService.setupRequestEntity(apiService.getStatsKey(), null),
                StocksItem[].class);

        int statusCode = responseEntity.getStatusCode().value();
        processResponse(productsStats, Objects.requireNonNull(responseEntity.getBody()));
    }

    @Override
    public void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException {
        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
    }

    @Override
    public boolean isCurrentDayOnly() {
        return true;
    }

    private void processResponse(List<ProductStats> productsStats, StocksItem[] stocksItems) {
        for (StocksItem stocksItem : stocksItems) {
            productsStats.stream()
                    .filter(productStats
                            -> productStats.getProduct().getCode()
                            .equals(String.valueOf(stocksItem.getNmId())))
                    .findFirst().orElse(new ProductStats())
                    .addStocks(stocksItem.getQuantity());
        }
    }
}
