package gr.aueb.cf.agronitor.service.exceptions;

public class EntityAlreadyExistsException extends Exception{
    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(Class<?> entityClass, Long id) {
        super("Entity " + entityClass.getSimpleName() + " with id " + id + " already exists");
    }

//    Specific for user because username must be unique
    public EntityAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username: " + username + " already exists");
    }
}
