package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.AuthUtils.getUserFromAuthentication;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.studease.studeasebackend.dto.CollectionDeleteRequestDto;
import tech.studease.studeasebackend.repository.CollectionRepository;
import tech.studease.studeasebackend.repository.UserRepository;
import tech.studease.studeasebackend.repository.entity.Collection;
import tech.studease.studeasebackend.repository.entity.User;
import tech.studease.studeasebackend.service.CollectionService;
import tech.studease.studeasebackend.service.exception.CollectionAlreadyExistsException;
import tech.studease.studeasebackend.service.exception.CollectionNotFoundException;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;

  @Override
  public List<Collection> findAll() {
    String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return collectionRepository.findAllByAuthorEmail(authorEmail);
  }

  @Override
  public Collection findById(Long collectionId) {
    String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return collectionRepository
        .findByIdAndAuthorEmail(collectionId, authorEmail)
        .orElseThrow(() -> new CollectionNotFoundException(collectionId));
  }

  @Override
  @Transactional
  public Collection create(Collection collection) {
    User author = getUserFromAuthentication();
    if (collectionRepository.existsByNameAndAuthorEmail(collection.getName(), author.getEmail())) {
      throw new CollectionAlreadyExistsException(collection.getName());
    }
    collection.setAuthor(author);
    return collectionRepository.save(collection);
  }

  @Override
  @Transactional
  public void deleteById(Long collectionId) {
    collectionRepository.deleteById(collectionId);
  }

  @Override
  @Transactional
  public void deleteAllByIds(CollectionDeleteRequestDto request) {
    List<Collection> collections = collectionRepository.findAllById(request.getCollectionIds());
    collectionRepository.deleteAll(collections);
  }
}
