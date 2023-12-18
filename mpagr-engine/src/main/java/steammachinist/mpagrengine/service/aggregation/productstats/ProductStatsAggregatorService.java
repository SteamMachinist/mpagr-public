package steammachinist.mpagrengine.service.aggregation.productstats;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.Product;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.service.persistance.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStatsAggregatorService {
    private final List<ProductStatsProvider> statsProviders;
    private final ProductService productService;

    public List<DatedDataList<ProductStats>> getAggregatedProductsStatsAtDateRange(LocalDateTime dateTime, int shift) {
        List<Product> products = productService.getAll();
        List<DatedDataList<ProductStats>> productsStatsForDays = new ArrayList<>();

        productsStatsForDays.add(getCurrentDayProductsStats(dateTime, createEmptyStatsFor(products)));

        for (int i = 0; i < shift; i++) {
            dateTime = dateTime.minusDays(1);
            productsStatsForDays.add(getBeforeDaysProductsStats(dateTime, createEmptyStatsFor(products)));
        }
        return productsStatsForDays;
    }

    private DatedDataList<ProductStats> getCurrentDayProductsStats(LocalDateTime dateTime, List<ProductStats> productsStats) {
        statsProviders.forEach(productStatsProvider -> {
            try {
                productStatsProvider.provideStats(productsStats, dateTime);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return new DatedDataList<>(dateTime, productsStats);
    }

    private DatedDataList<ProductStats> getBeforeDaysProductsStats(LocalDateTime beforeDay, List<ProductStats> productsStats) {
        statsProviders.forEach(productStatsProvider -> {
            try {
                if (!productStatsProvider.isCurrentDayOnly()) {
                    productStatsProvider.provideStats(productsStats, beforeDay);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return new DatedDataList<>(beforeDay, productsStats);
    }

    private List<ProductStats> createEmptyStatsFor(List<Product> products) {
        return products.stream().map(ProductStats::new).collect(Collectors.toList());
    }
}
