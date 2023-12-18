package steammachinist.mpagrengine.dto.advertisement;

import lombok.Getter;

@Getter
public enum CampaignStatus {
    DELETING(-1),
    READY(4),
    ENDED(7),
    REFUSED(8),
    ACTIVE(9),
    PAUSED(11);

    private final int code;

    CampaignStatus(int code) {
        this.code = code;
    }

    public static CampaignStatus fromInt(int code) {
        for (CampaignStatus enumConstant : CampaignStatus.values()) {
            if (enumConstant.code == code) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for CampaignStatus: " + code);
    }
}
