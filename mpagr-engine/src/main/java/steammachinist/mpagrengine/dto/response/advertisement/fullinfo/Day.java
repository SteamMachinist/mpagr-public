package steammachinist.mpagrengine.dto.response.advertisement.fullinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Day {
    private String date;
    //private int views;
    //private int clicks;
    //private double ctr;
    //private double cpc;
    //private double sum;
    //private int atbs;
    //private int orders;
    //private int cr;
    //private int shks;
    //private double sum_price;
    private List<AppStats> apps;
}
