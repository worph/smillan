package com.smilan.process.domain.category;

import com.google.common.base.Preconditions;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.CategoryManager;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import com.smilan.logic.domain.category.CategoryLogic;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.logic.domain.security.Realm;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.transaction.annotation.Transactional;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class DefaultCategoryManager implements CategoryManager {

    private CategoryLogic categoryLogic;
    private AdministrationExcutor administrationExcutor;
    private Realm realm;

    @Transactional
    @Override
    public CategoryManagerDTO define(CategoryManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        return categoryLogic.define(gererPersonneDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryManagerDTO search(CategoryManagerDTO gererPersonneDTO) {
        Preconditions.checkNotNull(gererPersonneDTO, "gererPersonneDTO must not be null");
        return categoryLogic.search(gererPersonneDTO);
    }

    @Transactional(readOnly = false)
    @Override
    public CategoryAuthResult auth(CategoryToken token) {
        final CategoryAuthResult ret = new CategoryAuthResult();
        ret.setAuth(false);
        administrationExcutor.run(() -> {
            //Admin rights are mandatory to retreiv password value
            if(token.getCategory()==null){
                ret.setAuth(false);
            }else{
                Category category = categoryLogic.searchCategoryByName(token.getCategory());
                if (category == null) {
                    ret.setAuth(false);
                } else if (category.getPasswordValue().equals(token.getPassword()) && category.getPasswordValue() != null) {
                    ret.setAuth(true);
                } else {
                    ret.setAuth(false);
                }
            }
        });
        if(ret.isAuth()){
            realm.setNewCategoryAuthPermission(token.getCategory());
        }
        return ret;
    }

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    @Override
    public CategoryManagerDTO delete(CategoryManagerDTO manageDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public AdministrationExcutor getAdministrationExcutor() {
        return administrationExcutor;
    }

    public void setAdministrationExcutor(AdministrationExcutor administrationExcutor) {
        this.administrationExcutor = administrationExcutor;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public CategoryManagerDTO list() {
         return new CategoryManagerDTOBuilder().withEntities(categoryLogic.list()).build();
    }

}
