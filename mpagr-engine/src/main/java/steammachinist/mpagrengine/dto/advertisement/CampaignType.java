package steammachinist.mpagrengine.dto.advertisement;

import lombok.Getter;

@Getter
public enum CampaignType {
    CATALOG(4, "Каталог"),
    CARD(5, "Карточка товара"),
    SEARCH(6, "Поиск"),
    RECOMMENDATIONS(7, "Карусель"),
    AUTO(8, "Автоматические"),
    SEARCH_CATALOG(9, "Поиск + Каталог");

    private final int code;
    private final String russianName;

    CampaignType(int code, String russianName) {
        this.code = code;
        this.russianName = russianName;
    }

    public static CampaignType fromInt(int code) {
        for (CampaignType enumConstant : CampaignType.values()) {
            if (enumConstant.code == code) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for CampaignType: " + code);
    }
}
