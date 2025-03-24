package tech.studease.studeasebackend.service.exception;

public class CollectionNotFoundException extends RuntimeException {

  public CollectionNotFoundException(String name) {
    super("Collection with name " + name + " not found");
  }
}
