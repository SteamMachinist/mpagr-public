package steammachinist.mpagrengine.dto.response.advertisement.fullinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoosterStats {
    private String date;
    private long nm;
    private int avg_position;
}
