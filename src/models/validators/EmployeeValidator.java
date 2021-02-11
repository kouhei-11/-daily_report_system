package models.validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import models.Employee;
import utils.DBUtil;


public class EmployeeValidator {
    public static List<String> validate(Employee e , Boolean codeDuplicateCheckFlag, Boolean passwordCheckFlag){
    List<String> errors = new ArrayList<String>();

    String code_error = validateCode(e.getCode(), codeDuplicateCheckFlag);
    if(!code_error.equals("")){
        errors.add(code_error);
    }

    String name_error = validateName(e.getName());
    if(!name_error.equals("")){
        errors.add(name_error);
    }

    String password_error = valodatePassword(e.getPassword(), passwordCheckFlag);
    if(!password_error.equals("")){
        errors.add(password_error);
    }
    return errors;
}


private static String validateCode(String code, Boolean codeDuplicateCheckFlag){
    if(code == null ||  code.equals("")){
        return "社員番号を入力してください。";
    }
    if(codeDuplicateCheckFlag) {
        EntityManager em = DBUtil.createEntityManager();
        long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class).setParameter("code",code).getSingleResult();
        em.close();
        if(employees_count > 0){
            return "入力された社員番号の情報はすでに存在しています。";
        }
    }
