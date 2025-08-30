package com.ecom.spring_boot_ecom.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String filed;
    Long fieldId;

    public ResourceNotFoundException(){

    }
    public ResourceNotFoundException(String resourceName, String fieldName) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.filed = filed;
    }
    public ResourceNotFoundException(String resourceName, String filed, Long fieldId) {
        super(String.format("%s not found with %s : %d", resourceName, filed, fieldId));
        this.resourceName = resourceName;
        this.filed = filed;
        this.fieldId = fieldId;
    }


}
