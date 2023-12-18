package steammachinist.mpagrengine.service.aggregation.productstats.telegram;

import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.util.UnsupportedNativeLibraryException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Data
@Component
public class TelegramApp implements AutoCloseable {
    private SimpleTelegramClient client;

    @Value("${tg.api.id}")
    private String apiId;
    @Value("${tg.api.hash}")
    private String apiHash;
    @Value("${tg.user-phone}")
    private String userPhone;

    public TelegramApp() {
    }

    public TelegramApp setupApp(SimpleTelegramClientFactory clientFactory) {
        try {
            Init.init();
        } catch (UnsupportedNativeLibraryException e) {
            throw new RuntimeException(e);
        }

        Log.setLogMessageHandler(1, new Slf4JLogMessageHandler());

        APIToken apiToken = new APIToken(Integer.parseInt(apiId), apiHash);
        TDLibSettings settings = TDLibSettings.create(apiToken);
        Path sessionPath = Paths.get("mpagr-tdlight-session");
        settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));
        settings.setFileDatabaseEnabled(false);
        settings.setMessageDatabaseEnabled(false);

        SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);
        SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.user(userPhone);
        clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);

        this.client = clientBuilder.build(authenticationData);

        try {
            String[] optionNames = {
                    "disable_minithumbnails",
                    "disable_notifications",
                    "ignore_update_chat_last_message",
                    "ignore_update_chat_read_inbox",
                    "ignore_update_user_chat_action",
                    "ignore_server_deletes_and_reads",
                    "receive_access_hashes",
                    "disable_auto_download",
                    "ignore_inline_thumbnails",
                    "disable_top_chats",
                    "ignore_platform_restrictions"
            };
            for (String optionName : optionNames) {
                client.send(new TdApi.SetOption(optionName, new TdApi.OptionValueBoolean(true))).get(1, TimeUnit.MINUTES);
            }

        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    public TdApi.Chat updateChat(String publicChatUsername) throws ExecutionException, InterruptedException, TimeoutException {
        return client.send(new TdApi.SearchPublicChat(publicChatUsername)).get(1, TimeUnit.MINUTES);
    }

    public TdApi.Message sendMessage(TdApi.Chat chat, String message) throws ExecutionException, InterruptedException, TimeoutException {
        TdApi.SendMessage sendMessage = new TdApi.SendMessage();
        sendMessage.chatId = chat.id;
        sendMessage.inputMessageContent = new TdApi.InputMessageText(
                new TdApi.FormattedText(message, new TdApi.TextEntity[0]), false, false);
        return client.sendMessage(sendMessage, true).get(1, TimeUnit.MINUTES);
    }

    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        TdApi.AuthorizationState authorizationState = update.authorizationState;
        if (authorizationState instanceof TdApi.AuthorizationStateReady) {
            System.out.println("Logged in");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
            System.out.println("Closing...");
        } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
            System.out.println("Closed");
        } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
            System.out.println("Logging out...");
        }
    }
}
