package service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Value;

@RegisterForReflection
@Value(staticConstructor = "of")
public class PublicTopicDto {
    String name;
}
