package vallegrande.edu.pe.vgmsacademic.domain.model.enums;

public enum PeriodTypeEnum {
    ANNUAL("ANNUAL"),
    SEMESTER("SEMESTER"),
    TRIMESTER("TRIMESTER"),
    BIMESTER("BIMESTER");
    
    private final String value;
    
    PeriodTypeEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static PeriodTypeEnum fromValue(String value) {
        for (PeriodTypeEnum type : PeriodTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valor de tipo de período inválido: " + value);
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}