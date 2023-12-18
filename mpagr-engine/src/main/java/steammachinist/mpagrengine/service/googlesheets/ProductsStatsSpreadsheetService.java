package steammachinist.mpagrengine.service.googlesheets;

import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsStatsSpreadsheetService {
    private final GoogleSheetsApiService apiService;
    @Value("${gs.sheet.stats}")
    private String statsSheetName;

    public void updateProductsStats(List<DatedDataList<ProductStats>> productsStatsForDates) {
        DatedDataList<ProductStats> currentDayStats = productsStatsForDates.get(0);
        productsStatsForDates.remove(0);
        try {
            if (!checkCurrentDayStatsPresent(currentDayStats.getDateTime())) {
                appendCurrentDayStats(currentDayStats);
            }
            updateBeforeDaysStats(productsStatsForDates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkCurrentDayStatsPresent(LocalDateTime now) throws IOException {
        int lastRowNumber = apiService.getLastRowNumber(statsSheetName);

        if (lastRowNumber > 1) {
            String range = statsSheetName + "!A" + lastRowNumber + ":R" + lastRowNumber;
            ValueRange response = apiService.valuesGet(range);
            List<List<Object>> values = response.getValues();

            if (values != null && !values.isEmpty()) {
                List<Object> lastRowValues = values.get(0);
                return lastRowValues.get(2).equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }
        return false;
    }

    private void appendCurrentDayStats(DatedDataList<ProductStats> productsStats) throws IOException {
        List<List<Object>> stats = transformProductStatsToRows(productsStats.getList(), productsStats.getDateTime());
        ValueRange values = new ValueRange().setValues(stats);
        apiService.valuesAppend(statsSheetName, values);
    }

    private void updateBeforeDaysStats(List<DatedDataList<ProductStats>> productsStatsForDates) throws IOException {
        ValueRange response = apiService.valuesGet(statsSheetName);
        List<List<Object>> values = response.getValues();
        if (values != null) {
            for (DatedDataList<ProductStats> datedProductsStats : productsStatsForDates) {
                String targetDate = datedProductsStats.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                for (List<Object> row : values) {
                    if (row.get(2).toString().equals(targetDate)) {
                        ProductStats productStats = datedProductsStats.getList().stream()
                                .filter(productStats1 -> productStats1.getProduct().getCode()
                                        .equals(row.get(1).toString()))
                                .toList()
                                .get(0);
                        System.out.println(row.get(2));
                        System.out.println(productStats.getProduct().getCode());
                        System.out.println(productStats.getOrders());
                        System.out.println(productStats.getSales());
                        System.out.println(productStats.getViews());
                        row.set(5, productStats.getOrders());
                        row.set(6, productStats.getSales());
                        row.set(7, productStats.getViews());
                    }
                }
            }
            ValueRange updatedValues = new ValueRange().setValues(values);
            apiService.valuesUpdate(statsSheetName, updatedValues);
        }
    }

    public static List<List<Object>> transformProductStatsToRows(List<ProductStats> productsStats, LocalDateTime dateTime) {
        List<List<Object>> rows = new ArrayList<>();

        for (ProductStats stats : productsStats) {
            List<Object> row = new ArrayList<>();
            row.addAll(Arrays.asList(
                    stats.getProduct().getVendorCode(),
                    stats.getProduct().getCode(),
                    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    stats.getPrice(),
                    stats.getRating(),
                    stats.getOrders(),
                    stats.getSales(),
                    stats.getViews(),
                    stats.getStocks()
            ));
            row.addAll(stats.getPosition().values().stream().toList());
            rows.add(row);
        }
        return rows;
    }
}
