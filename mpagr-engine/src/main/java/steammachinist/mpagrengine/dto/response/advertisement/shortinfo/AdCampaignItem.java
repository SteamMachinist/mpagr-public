package steammachinist.mpagrengine.dto.response.advertisement.shortinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdCampaignItem {
    private String endTime;
    private String createTime;
    private String changeTime;
    private String startTime;
    private String name;
    private int dailyBudget;
    private int advertId;
    private int status;
    private int type;
}
