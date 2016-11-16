package app.document;

import java.util.List;

/**
 * Created by Admin on 13.11.2016.
 */
public class DocumentWrapper {
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public DocumentWrapper(List<Document> documents) {
        this.documents = documents;
    }

    public DocumentWrapper() {

    }

    public void setdocuments(List<Document> documents) {
        this.documents = documents;
    }
}
