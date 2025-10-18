package tech.studease.studeasebackend.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.studease.studeasebackend.dto.CollectionDto;
import tech.studease.studeasebackend.dto.CollectionInfo;
import tech.studease.studeasebackend.dto.CollectionListInfo;
import tech.studease.studeasebackend.dto.QuestionListDto;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.service.CollectionService;
import tech.studease.studeasebackend.service.QuestionService;
import tech.studease.studeasebackend.service.mapper.CollectionMapper;
import tech.studease.studeasebackend.service.mapper.QuestionMapper;

@RestController
@RequestMapping("/api/v1/admin/collections")
@RequiredArgsConstructor
public class CollectionManagementController {

  private final CollectionService collectionService;
  private final CollectionMapper collectionMapper;

  private final QuestionService questionService;
  private final QuestionMapper questionMapper;

  @GetMapping
  public ResponseEntity<CollectionListInfo> getAllCollections() {
    return ResponseEntity.ok(collectionMapper.toCollectionListInfo(collectionService.findAll()));
  }

  @GetMapping("{collectionId}")
  public ResponseEntity<CollectionInfo> getCollectionInfoById(@PathVariable Long collectionId) {
    return ResponseEntity.ok(
        collectionMapper.toCollectionInfo(collectionService.findById(collectionId)));
  }

  @GetMapping("{collectionId}/questions")
  public ResponseEntity<QuestionListDto> getQuestionsByCollectionId(
      @PathVariable Long collectionId) {
    return ResponseEntity.ok(
        questionMapper.toQuestionListDto(questionService.findByCollectionId(collectionId), true));
  }

  @PostMapping
  public ResponseEntity<CollectionInfo> createCollection(
      @RequestBody @Valid CollectionDto collectionDto) {
    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(
            collectionMapper.toCollectionInfo(
                collectionService.create(collectionMapper.toCollection(collectionDto))));
  }

  @PostMapping("{collectionId}/questions")
  public ResponseEntity<QuestionListDto> addQuestionsToCollection(
      @PathVariable Long collectionId, @RequestBody @Valid QuestionListDto questionListDto) {
    Collection collection = collectionService.findById(collectionId);
    return ResponseEntity.status(HttpStatus.CREATED.value())
        .body(
            questionMapper.toQuestionListDto(
                questionService.createAll(
                    questionMapper.toQuestionList(questionListDto.getQuestions(), collection)),
                true));
  }

  @DeleteMapping("{collectionId}")
  public ResponseEntity<Void> deleteCollection(@PathVariable Long collectionId) {
    collectionService.deleteById(collectionId);
    return ResponseEntity.noContent().build();
  }
}
