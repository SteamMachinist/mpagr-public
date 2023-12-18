package steammachinist.mpagrengine.service.googlesheets;

import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.advertisement.AdCampaign;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdCampaignsSpreadsheetService {
    private final GoogleSheetsApiService apiService;
    @Value("${gs.sheet.ad}")
    private String adSheetName;
    private final int dateColumn = 5;

    public void updateAdCampaignsStats(DatedDataList<AdCampaign> campaigns) {
        //deleteRowsByDate(campaigns.getDateTime());
        try {
            if (!checkYesterdayAdCampaignsPresent(campaigns.getDateTime()))
            {
                appendNewAdCampaignStats(campaigns);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private boolean checkYesterdayAdCampaignsPresent(LocalDateTime dateTime) throws IOException {
        int lastRowNumber = apiService.getLastRowNumber(adSheetName);
        if (lastRowNumber > 1) {
            String range = adSheetName + "!A" + lastRowNumber + ":P" + lastRowNumber;
            ValueRange response = apiService.valuesGet(range);
            List<List<Object>> values = response.getValues();

            if (values != null && !values.isEmpty()) {
                List<Object> lastRowValues = values.get(0);
                return lastRowValues.get(dateColumn).equals(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }
        return false;
    }

    private void deleteRowsByDate(LocalDateTime dateTime) {
        try {
            String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ValueRange response = apiService.valuesGet(adSheetName);
            List<List<Object>> values = response.getValues();

            if (values != null) {
                List<Request> requests = new ArrayList<>();

                for (int i = 0; i < values.size(); i++) {
                    List<Object> row = values.get(i);

                    if (row.get(dateColumn).toString().equals(date)) {
                        requests.add(new Request()
                                .setDeleteDimension(new DeleteDimensionRequest()
                                        .setRange(new DimensionRange()
                                                .setSheetId(apiService.getSheetId(adSheetName))
                                                .setDimension("ROWS")
                                                .setStartIndex(i)
                                                .setEndIndex(i + 1)
                                        )
                                ));
                    }
                }
                if (!requests.isEmpty()) {
                    BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
                    apiService.executeBatchUpdate(batchUpdateRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendNewAdCampaignStats(DatedDataList<AdCampaign> campaigns) {
        List<List<Object>> stats = transformCampaignsToRows(campaigns.getList(), campaigns.getDateTime());
        ValueRange values = new ValueRange().setValues(stats);
        try {
            apiService.valuesAppend(adSheetName, values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<List<Object>> transformCampaignsToRows(List<AdCampaign> campaigns, LocalDateTime dateTime) {
        List<List<Object>> rows = new ArrayList<>();
        for (AdCampaign campaign : campaigns) {
            List<Object> row = new ArrayList<>(Arrays.asList(
                    campaign.getCode(),
                    campaign.getName(),
                    campaign.getType().getRussianName(),
                    campaign.getProductCode(),
                    campaign.getProductName(),
                    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    campaign.getViews(),
                    campaign.getClicks(),
                    campaign.getCtr(),
                    campaign.getCpc(),
                    campaign.getCarts(),
                    campaign.getOrderedItems(),
                    campaign.getOrdersSum(),
                    campaign.getCost(),
                    campaign.getCr(),
                    campaign.getPlatform().getName()
            ));
            rows.add(row);
        }
        return rows;
    }
}
