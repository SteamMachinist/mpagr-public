package steammachinist.mpagrengine.dto.product;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductStats {
    private Product product;
    private Map<String, Integer> position;
    private Double price;
    private Double priceSpp;
    private Integer categories;
    private Integer orders;
    private Integer sales;
    private Integer views;
    private Integer stocks;
    private Double rating;

    public ProductStats(Product product) {
        this.product = product;
        position = new LinkedHashMap<>();
        price = 0.0;
        //priceSpp = 0.0;
        //categories = 0;
        orders = 0;
        sales = 0;
        views = 0;
        stocks = 0;
        rating = 0.0;
    }

    public void addStocks(int added) {
        if (stocks == null) {
            stocks = added;
        }
        stocks += added;
    }
}
