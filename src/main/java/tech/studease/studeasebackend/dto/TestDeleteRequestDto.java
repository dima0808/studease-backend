package tech.studease.studeasebackend.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestDeleteRequestDto {

  @NotEmpty(message = "Test IDs list cannot be empty")
  List<UUID> testIds;
}
