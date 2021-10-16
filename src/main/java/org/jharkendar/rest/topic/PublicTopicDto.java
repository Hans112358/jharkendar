package org.jharkendar.rest.topic;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Value;

@RegisterForReflection
@Value(staticConstructor = "of")
public class PublicTopicDto {
    String id;
    String name;
}
