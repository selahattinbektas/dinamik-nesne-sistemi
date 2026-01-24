package tr.com.havelsan.dynamicobject.mainobject.service.impl;

import org.springframework.stereotype.Service;
import tr.com.havelsan.dynamicobject.mainobject.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public boolean validate(String validatorClassName, Object value) {
        return validatorClassName == null || validatorClassName.isBlank() || value != null;
    }
}
