package org.jharkendar.rest.tag;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class UpdateTagDto {
    @NotBlank(message = "name cannot be empty")
    String name;
}
