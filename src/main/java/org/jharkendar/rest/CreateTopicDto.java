package org.jharkendar.rest;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class CreateTopicDto implements Serializable {
    @NotBlank(message = "name cannot be empty")
    String name;
}
