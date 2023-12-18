package steammachinist.mpagrengine.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsStatsRequestNew {
    //private String[] brandNames;
    //private int[] objectIDs;
    //private int[] tagIDs;
    private int[] nmIDs;
    //private String timezone;
    private Period period;
    //private OrderBy orderBy;
    private int page;

//    @Data
//    @AllArgsConstructor
//    public static class OrderBy {
//        private String field;
//        private String mode;
//    }
}
