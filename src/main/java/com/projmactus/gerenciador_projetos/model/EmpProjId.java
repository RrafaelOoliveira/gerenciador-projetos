package com.projmactus.gerenciador_projetos.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

//Chave composta para a entidade EMP_PROJ.

@Embeddable //Indica que esta classe é uma chave composta
public class EmpProjId implements Serializable {

    private Long empId;
    private Long projId;

    public EmpProjId() {
    }

    public EmpProjId(Long empId, Long projId) {
        this.empId = empId;
        this.projId = projId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmpProjId that = (EmpProjId) o;
        return Objects.equals(empId, that.empId) && Objects.equals(projId, that.projId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, projId);
    }
}
