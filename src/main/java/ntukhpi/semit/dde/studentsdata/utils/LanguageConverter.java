package ntukhpi.semit.dde.studentsdata.utils;


import jakarta.persistence.AttributeConverter;

public class LanguageConverter implements AttributeConverter<Language, Integer>
         {
    @Override
    public Integer convertToDatabaseColumn(Language pl) {
        return switch (pl) {
            case UK -> 1;
            case EN -> 2;
        };
    }

    @Override
    public Language convertToEntityAttribute(Integer codL) {
        return switch (codL) {
            case 1 -> Language.UK;
            case 2 -> Language.EN;
            default -> throw new IllegalStateException("Unexpected value: " + codL);
        };
    }
}
