package steammachinist.mpagrengine.service.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import steammachinist.mpagrengine.dto.response.price.PriceItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Getter
public class WbApiService {
    @Value("${wb.key.standard}")
    private String standardKey;
    @Value("${wb.key.stats}")
    private String statsKey;
    @Value("${wb.key.ad}")
    private String adKey;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public HttpEntity<String> setupRequestEntity(String authorizationKey, Object requestBody) throws JsonProcessingException {
        if (requestBody == null) {
            return new HttpEntity<>(
                    createHeaders(authorizationKey));
        } else {
            return new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    createHeaders(authorizationKey));
        }
    }

    public HttpHeaders createHeaders(String authorizationKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationKey);
        headers.set("Content-Type", "application/json");
        headers.set("accept", "application/json");
        return headers;
    }

    public String getFormattedDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
