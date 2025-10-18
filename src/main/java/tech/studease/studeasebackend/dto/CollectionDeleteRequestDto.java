package tech.studease.studeasebackend.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollectionDeleteRequestDto {

  @NotEmpty(message = "Collection IDs list cannot be empty")
  List<Long> collectionIds;
}
