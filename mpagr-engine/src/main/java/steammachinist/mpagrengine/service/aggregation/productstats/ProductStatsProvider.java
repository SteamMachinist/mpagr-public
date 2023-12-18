package steammachinist.mpagrengine.service.aggregation.productstats;

import com.fasterxml.jackson.core.JsonProcessingException;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductStatsProvider {
    void provideStats(List<ProductStats> productsStats, LocalDateTime dateTime) throws JsonProcessingException;
    void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException;
    boolean isCurrentDayOnly();
}
