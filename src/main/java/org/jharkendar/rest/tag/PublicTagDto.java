package org.jharkendar.rest.tag;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Value;

@RegisterForReflection
@Value(staticConstructor = "of")
public class PublicTagDto {
    String id;
    String name;
}
