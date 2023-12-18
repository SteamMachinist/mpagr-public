package steammachinist.mpagrengine.service.googlesheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GoogleSheetsApiService {
    private final String APPLICATION_NAME = "mpagr";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String CREDENTIALS_FILE = "/credentials.json";

    @Getter
    @Value("${gs.spreadsheet-id}")
    private String spreadsheetId;

    private GoogleCredentials credentials;

    private void createCredentials() throws IOException {
        credentials = GoogleCredentials
                .fromStream(Objects.requireNonNull(GoogleSheetsApiService.class.getResourceAsStream(CREDENTIALS_FILE)))
                .createScoped(SCOPES);
        credentials.refreshIfExpired();
    }

    public Sheets getSheetsService() {
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            if (credentials == null) {
                createCredentials();
            } else {
                credentials.refreshIfExpired();
            }
            return new Sheets
                    .Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSheetId(String sheetName) {
        try {
            Spreadsheet spreadsheet = getSheetsService().spreadsheets().get(spreadsheetId).execute();
            List<Sheet> sheets = spreadsheet.getSheets();
            for (Sheet sheet : sheets) {
                if (sheetName.equals(sheet.getProperties().getTitle())) {
                    return sheet.getProperties().getSheetId();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ValueRange valuesGet(String range) throws IOException {
        return getValues().get(spreadsheetId, range).execute();
    }

    public void valuesAppend(String range, ValueRange values) throws IOException {
        getValues().append(spreadsheetId, range, values).setValueInputOption("USER_ENTERED").execute();
    }

    public void valuesUpdate(String range, ValueRange values) throws IOException {
        getValues().update(spreadsheetId, range, values).setValueInputOption("USER_ENTERED").execute();
    }

    public Sheets.Spreadsheets.Values getValues() {
        return getSheetsService().spreadsheets().values();
    }

    public void executeBatchUpdate(BatchUpdateSpreadsheetRequest request) throws IOException {
        getSheetsService().spreadsheets().batchUpdate(spreadsheetId, request).execute();
    }

    public int getLastRowNumber(String sheetName) throws IOException {
        ValueRange response = getValues().get(spreadsheetId, sheetName).execute();
        List<List<Object>> values = response.getValues();
        if (values != null && !values.isEmpty()) {
            return values.size();
        }
        return 0;
    }
}
