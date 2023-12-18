package steammachinist.mpagrengine.service.aggregation.productstats.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.tdlight.client.SimpleTelegramClientFactory;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.dto.DatedDataList;
import steammachinist.mpagrengine.dto.product.ProductStats;
import steammachinist.mpagrengine.service.aggregation.productstats.ProductStatsProvider;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TgBotStatsProviderService implements ProductStatsProvider {
    private final TelegramApp telegramApp;

    @Value("${bot.username}")
    private String botUsername;

    @Override
    public void provideStats(List<ProductStats> productStats, LocalDateTime dateTime) {
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();
             TelegramApp app = telegramApp.setupApp(clientFactory)) {
            TdApi.Chat chat = app.updateChat(botUsername);
            String text = "";
            String newText = "";
            for (ProductStats productStatsCurrent : productStats) {
                for (String query : productStatsCurrent.getProduct().getQueries()) {
                    app.sendMessage(chat, productStatsCurrent.getProduct().getCode() + " " + query);
                    while (true) {
                        TimeUnit.MILLISECONDS.sleep(500);
                        chat = app.updateChat(botUsername);
                        newText = getLastMessage(chat);
                        if (newText.equals(text)) {
                            continue;
                        }
                        text = newText;
                        List<String> numbers = getMessageNumbers(text);
                        if (numbers.size() >= 3) {
                            putQueryPosition(productStatsCurrent, query, numbers);
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void provideStats(DatedDataList<ProductStats> datedProductsStats) throws JsonProcessingException {
        provideStats(datedProductsStats.getList(), datedProductsStats.getDateTime());
    }

    @Override
    public boolean isCurrentDayOnly() {
        return true;
    }

    private String getLastMessage(Chat chat) {
        TdApi.MessageText messageText = (TdApi.MessageText) chat.lastMessage.content;
        return messageText.text.text;
    }

    private List<String> getMessageNumbers(String text) {
        List<String> tokens = Arrays.stream(text.split("\\W+")).toList();
        return tokens.stream().filter(s -> s.matches("[0-9]+")).toList();
    }

    private void putQueryPosition(ProductStats productStats, String query, List<String> messageNumbers) {
        productStats.getPosition().put(query,
                (Integer.parseInt(messageNumbers.get(1)) - 1) * 100 + Integer.parseInt(messageNumbers.get(2)));
    }
}