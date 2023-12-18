package steammachinist.mpagrengine.dto.advertisement;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdCampaign implements Cloneable{
    private String code;
    private String name;
    private CampaignStatus status;
    private CampaignType type;
    private String productCode;
    private String productName;
    private int views;
    private int clicks;
    private double ctr;
    private double cpc;
    private int carts;
    private int orders;
    private int orderedItems;
    private double ordersSum;
    private double cost;
    private double cr;
    private AppPlatform platform;

    public AdCampaign(int code, String name, int status, int type) {
        this.code = String.valueOf(code);
        this.name = name;
        this.status = CampaignStatus.fromInt(status);
        this.type = CampaignType.fromInt(type);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AdCampaign cloned = (AdCampaign) super.clone();

        cloned.status = this.status;
        cloned.type = this.type;
        cloned.platform = this.platform;

        return cloned;
    }
}
