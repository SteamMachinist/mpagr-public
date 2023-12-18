package steammachinist.mpagrengine.dto.response.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StocksItem {
    private String supplierArticle;
    private int nmId;
    private int quantity;
    private int quantityFull;
    private String techSize;
    private double price;
    private double discount;
}
