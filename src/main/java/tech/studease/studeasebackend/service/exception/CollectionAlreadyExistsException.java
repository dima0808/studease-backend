package tech.studease.studeasebackend.service.exception;

public class CollectionAlreadyExistsException extends RuntimeException {

  public CollectionAlreadyExistsException(String name) {
    super("Collection with name " + name + " already exists");
  }
}
