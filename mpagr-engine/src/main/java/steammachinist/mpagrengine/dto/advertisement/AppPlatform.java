package steammachinist.mpagrengine.dto.advertisement;

import lombok.Getter;

@Getter
public enum AppPlatform {
    WEBSITE(1, "Web"),
    ANDROID(32, "Android"),
    IOS(64, "iOS");

    private final int code;
    private final String name;

    AppPlatform(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AppPlatform fromInt(int code) {
        for (AppPlatform enumConstant : AppPlatform.values()) {
            if (enumConstant.code == code) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Invalid integer value for AppPlatform: " + code);
    }
}
