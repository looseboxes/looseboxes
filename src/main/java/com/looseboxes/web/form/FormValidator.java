package com.looseboxes.web.form;

import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.forms.FormFieldOld;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @author Josh
 */
public class FormValidator extends com.bc.web.core.form.DatabaseFormValidator {
    
    public FormValidator(UserType userType) {
        super(userType, null);
    }

    @Override
    public String getConfirmPasswordName() {
        return FormFieldOld.WebOnlyField.confirmPassword.name();
    }

    @Override
    public boolean isUserIdentifier(String columnName) {
        return Siteuser_.emailAddress.getName().equals(columnName) || Siteuser_.username.getName().equals(columnName);
    }
    
    @Override
    public boolean isExistingUser(String columnName, Object columnValue) {
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        try(Select<Number> qb = jpaContext.getDaoForSelect(Siteuser.class, Number.class)) {
            Number siteuserid = qb.from(Siteuser.class)
            .select(Siteuser_.siteuserid.getName())
            .where(columnName, columnValue)
            .createQuery().getSingleResult();
            return siteuserid != null;
        }catch(javax.persistence.NoResultException noNeedToLog) {
            return false;
        }
    }
}
