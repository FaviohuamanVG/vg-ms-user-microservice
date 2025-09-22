package vallegrande.edu.pe.vgmsacademic.domain.model.enums;

public enum StatusEnum {
    A("A"),
    I("I"),
    C("C"),
    T("T");
    
    private final String value;
    
    StatusEnum(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static StatusEnum fromValue(String value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor de estado inválido: " + value);
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}