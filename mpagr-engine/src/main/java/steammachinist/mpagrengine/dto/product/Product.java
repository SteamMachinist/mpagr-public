package steammachinist.mpagrengine.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private Long id;
    //private String name;
    private String vendorCode;
    private String code;
    private String responsible;
    //private Double costPrice;
    private List<String> queries;
}
