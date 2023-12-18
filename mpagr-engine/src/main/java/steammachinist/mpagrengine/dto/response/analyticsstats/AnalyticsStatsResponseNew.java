package steammachinist.mpagrengine.dto.response.analyticsstats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsStatsResponseNew {
    private AnalyticsData data;
    private boolean error;
    private String errorText;
    //private List<AdditionalError> additionalErrors;

    @Data
    @AllArgsConstructor
    public static class AnalyticsData {
        private int page;
        private boolean isNextPage;
        private List<Card> cards;
    }

    @Data
    @AllArgsConstructor
    public static class Card {
        private long nmID;
        private String vendorCode;
        private String brandName;
        //private List<Tag> tags;
        //private ObjectClass object;
        private Statistics statistics;
        private Stocks stocks;
    }

//    @Data
//    @AllArgsConstructor
//    public static class Tag {
//        private int id;
//        private String name;
//    }

//    @Data
//    @AllArgsConstructor
//    public static class ObjectClass {
//        private int id;
//        private String name;
//    }

    @Data
    @AllArgsConstructor
    public static class Statistics {
        private SelectedPeriod selectedPeriod;
//        private SelectedPeriod previousPeriod;
//        private PeriodComparison periodComparison;
    }

    @Data
    @AllArgsConstructor
    public static class SelectedPeriod {
        private String begin;
        private String end;
        private int openCardCount;
        private int addToCartCount;
        private int ordersCount;
        private int ordersSumRub;
        private int buyoutsCount;
        private int buyoutsSumRub;
        private int cancelCount;
        private int cancelSumRub;
        private int avgPriceRub;
        private double avgOrdersCountPerDay;
//        private Conversions conversions;
    }

//    @Data
//    @AllArgsConstructor
//    public static class PeriodComparison {
//        private int openCardDynamics;
//        private int addToCartDynamics;
//        private int ordersCountDynamics;
//        private int ordersSumRubDynamics;
//        private int buyoutsCountDynamics;
//        private int buyoutsSumRubDynamics;
//        private int cancelCountDynamics;
//        private int cancelSumRubDynamics;
//        private int avgOrdersCountPerDayDynamics;
//        private int avgPriceRubDynamics;
//        private Conversions conversions;
//    }

//    @Data
//    @AllArgsConstructor
//    public static class Conversions {
//        private int addToCartPercent;
//        private int cartToOrderPercent;
//        private int buyoutsPercent;
//    }

    @Data
    @AllArgsConstructor
    public static class Stocks {
        private int stocksMp;
        private int stocksWb;
    }

//    @Data
//    @AllArgsConstructor
//    public static class AdditionalError {
//        private String field;
//        private String description;
//    }
}
