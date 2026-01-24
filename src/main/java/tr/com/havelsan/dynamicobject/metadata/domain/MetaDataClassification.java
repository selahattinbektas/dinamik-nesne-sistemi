package tr.com.havelsan.dynamicobject.metadata.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "metadata_classifications")
public class MetaDataClassification {
    @Id
    private String id;
    private String drawClassification;
    private String classification;
    private String metaDataName;
    private String metaDataCode;
    private String symbolCode;
    private String unitType;
    private boolean undeletable;

    public MetaDataClassification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrawClassification() {
        return drawClassification;
    }

    public void setDrawClassification(String drawClassification) {
        this.drawClassification = drawClassification;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMetaDataName() {
        return metaDataName;
    }

    public void setMetaDataName(String metaDataName) {
        this.metaDataName = metaDataName;
    }

    public String getMetaDataCode() {
        return metaDataCode;
    }

    public void setMetaDataCode(String metaDataCode) {
        this.metaDataCode = metaDataCode;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public boolean isUndeletable() {
        return undeletable;
    }

    public void setUndeletable(boolean undeletable) {
        this.undeletable = undeletable;
    }
}
