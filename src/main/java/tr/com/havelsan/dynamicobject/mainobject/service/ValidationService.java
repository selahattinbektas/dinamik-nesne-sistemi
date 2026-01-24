package tr.com.havelsan.dynamicobject.mainobject.service;

public interface ValidationService {
    boolean validate(String validatorClassName, Object value);
}
