package steammachinist.mpagrengine.dto.response.analyticsstats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import steammachinist.mpagrengine.dto.response.AdditionalError;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsStatsResponse {
    private List<DataItem> data;
    private boolean error;
    private String errorText;
    private List<AdditionalError> additionalErrors;
}

