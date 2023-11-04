package ntukhpi.semit.dde.studentsdata.utils;


import jakarta.persistence.AttributeConverter;

public class KinshipDegreeConverter implements AttributeConverter<KinshipDegree, Integer>
         {
    @Override
    public Integer convertToDatabaseColumn(KinshipDegree kinshipDegree) {
        return kinshipDegree.ordinal()+1;
    }

    @Override
    public KinshipDegree convertToEntityAttribute(Integer codKD) {
        KinshipDegree[] listKinshipDegree = KinshipDegree.values();
        return listKinshipDegree[codKD-1];
    }
}
