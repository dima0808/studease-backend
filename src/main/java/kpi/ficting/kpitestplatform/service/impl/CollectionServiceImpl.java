package kpi.ficting.kpitestplatform.service.impl;

import kpi.ficting.kpitestplatform.repository.UserRepository;
import kpi.ficting.kpitestplatform.repository.entity.User;
import kpi.ficting.kpitestplatform.service.exception.UserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import kpi.ficting.kpitestplatform.repository.entity.Collection;
import kpi.ficting.kpitestplatform.repository.CollectionRepository;
import kpi.ficting.kpitestplatform.service.CollectionService;
import kpi.ficting.kpitestplatform.service.exception.CollectionAlreadyExistsException;
import kpi.ficting.kpitestplatform.service.exception.CollectionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
  public Collection findByName(String collectionName) {
    String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return collectionRepository.findByNameAndAuthorEmail(collectionName, authorEmail)
        .orElseThrow(() -> new CollectionNotFoundException(collectionName));
  }

  @Override
  @Transactional
  public Collection create(Collection collection) {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User author = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException(userEmail));
    if (collectionRepository.existsByNameAndAuthorEmail(collection.getName(), author.getEmail())) {
      throw new CollectionAlreadyExistsException(collection.getName());
    }
    collection.setAuthor(author);
    return collectionRepository.save(collection);
  }

  @Override
  @Transactional
  public void deleteByName(String collectionName) {
    collectionRepository.deleteByName(collectionName);
  }
}
