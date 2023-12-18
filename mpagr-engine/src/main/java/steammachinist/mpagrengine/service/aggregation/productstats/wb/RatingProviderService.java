package steammachinist.mpagrengine.service.aggregation.productstats.wb;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.dto.response.rating.RatingResponse;
import steammachinist.mpagrengine.service.aggregation.WbApiService;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RatingProviderService implements ProductStatsProvider {
    private final String ratingUri = "https://feedbacks-api.wb.ru/api/v1/feedbacks/products/rating/nmid";
    private final WbApiService apiService;

    @Override
    public void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException {
        for (ProductStats productStats : productsStats) {
            ResponseEntity<RatingResponse> responseEntity = apiService.getRestTemplate().exchange(
                    UriComponentsBuilder.fromHttpUrl(ratingUri)
                            .queryParam("nmId", Integer.parseInt(productStats.getProduct().getCode()))
                            .build().toUriString(),
                    HttpMethod.GET,
                    apiService.setupRequestEntity(apiService.getStandardKey(), null),
                    RatingResponse.class);

            processResponse(productStats, Objects.requireNonNull(responseEntity.getBody()));
        }
    }

    @Override
    public void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException {
        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
    }

    @Override
    public boolean isCurrentDayOnly() {
        return true;
    }

    private void processResponse(ProductStats productStats, RatingResponse response) {
        productStats.setRating(Double.parseDouble(response.getData().getValuation()));
    }
}
