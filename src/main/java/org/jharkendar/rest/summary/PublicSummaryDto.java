package org.jharkendar.rest.summary;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@RegisterForReflection
@NoArgsConstructor
@AllArgsConstructor
public class PublicSummaryDto {
    String id;
    String title;
    String text;
    String topicId;
    List<String> tagIds;
}
