package org.jharkendar.rest.summary;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class CreateSummaryDto {
    @NotBlank(message = "title cannot be empty")
    String title;

    String text;

    @NotNull
    String topicId;

    List<String> tagIds;
}
