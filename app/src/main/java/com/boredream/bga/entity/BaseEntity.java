package com.boredream.bga.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable {

    protected String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}