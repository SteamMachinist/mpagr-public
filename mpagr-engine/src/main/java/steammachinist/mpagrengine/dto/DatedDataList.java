package steammachinist.mpagrengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatedDataList<T> {
    private LocalDateTime dateTime;
    private List<T> list;
}
