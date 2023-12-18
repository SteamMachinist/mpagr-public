package steammachinist.mpagrengine.service.aggregation.productstats.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.dto.response.price.PriceItem;
import steammachinist.mpagrengine.service.aggregation.WbApiService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PricesProviderService implements ProductStatsProvider {
    private final String pricesUri = "https://suppliers-api.wildberries.ru/public/api/v1/info";
    private final WbApiService apiService;

    @Override
    public void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException {
        ResponseEntity<PriceItem[]> responseEntity = apiService.getRestTemplate().exchange(
                pricesUri,
                HttpMethod.GET,
                apiService.setupRequestEntity(apiService.getStandardKey(), null),
                PriceItem[].class);

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

    private void processResponse(List<ProductStats> productsStats, PriceItem[] priceItems) {
        for (PriceItem priceItem : priceItems) {
            productsStats.stream()
                    .filter(productStats
                            -> productStats.getProduct().getCode()
                            .equals(String.valueOf(priceItem.getNmId())))
                    .findFirst().orElse(new ProductStats())
                    .setPrice(priceItem.getPrice() * (1 - priceItem.getDiscount() / 100.0));
        }
    }
}
