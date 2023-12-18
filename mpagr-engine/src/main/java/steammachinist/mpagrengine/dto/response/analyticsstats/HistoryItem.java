package steammachinist.mpagrengine.dto.response.analyticsstats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryItem {
    private String dt;
    private int openCardCount;
    private int addToCartCount;
    private int ordersCount;
    private int ordersSumRub;
    private int buyoutsCount;
    private int buyoutsSumRub;
    private int buyoutPercent;
    private double addToCartConversion;
    private int cartToOrderConversion;
}
