package tr.com.havelsan.dynamicobject.metadata.api.dto;

public class MetaDataClassificationDTO {
    private String id;
    private String drawClassification;
    private String classification;
    private String metaDataName;
    private String metaDataCode;
    private String symbolCode;
    private String unitType;
    private boolean undeletable;

    public MetaDataClassificationDTO() {
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
