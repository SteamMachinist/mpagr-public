package steammachinist.mpagrengine.service.googlesheets;

import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpreadsheetService {
    private final GoogleSheetsApiService apiService;
    @Value("${gs.sheet.stats}")
    private String statsSheetName;
    @Value("${gs.sheet.ad}")
    private String adSheetName;

    public String setupSheetForDate(LocalDateTime dateTime) throws IOException {
        String sheetTitle = String.format("Позиции %s.%s mpagr", dateTime.getMonthValue(), dateTime.getYear());

        Spreadsheet spreadsheet = apiService.getSheetsService().spreadsheets().get(apiService.getSpreadsheetId()).execute();
        boolean sheetExists = spreadsheet.getSheets().stream().anyMatch(sheet -> sheetTitle.equals(sheet.getProperties().getTitle()));

        if (!sheetExists) {
            BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
            List<Request> requests = new ArrayList<>();
            AddSheetRequest addSheetRequest = new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetTitle));
            requests.add(new Request().setAddSheet(addSheetRequest));

            batchUpdateSpreadsheetRequest.setRequests(requests);
            apiService.getSheetsService().spreadsheets().batchUpdate(apiService.getSpreadsheetId(), batchUpdateSpreadsheetRequest).execute();
        }
        return sheetTitle;
    }
}
