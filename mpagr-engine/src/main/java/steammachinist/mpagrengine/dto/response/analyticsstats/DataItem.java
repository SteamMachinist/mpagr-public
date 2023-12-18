package steammachinist.mpagrengine.dto.response.analyticsstats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataItem {
    private long nmID;
    private String imtName;
    private String vendorCode;
    private List<HistoryItem> history;
}
