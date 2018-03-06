package com.smilan.process.domain.announce;

import com.google.common.base.Preconditions;
import com.smilan.api.common.manager.DTOErrorHelper;
import com.smilan.api.common.manager.option.builder.DeleteOptionBuilder;
import com.smilan.api.common.manager.option.builder.OptionServiceBuilder;
import com.smilan.api.common.manager.option.builder.TimeFilterOptionBuilder;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.api.domain.category.Category;
import com.smilan.logic.domain.announce.AnnounceLogic;
import com.smilan.logic.domain.category.CategoryDAOI;
import com.smilan.logic.domain.category.CategoryLogic;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.logic.domain.security.CategoryPermision;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import org.springframework.transaction.annotation.Transactional;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class DefaultAnnounceManager implements AnnounceManager {

    private AnnounceLogic announceLogic;
    private CategoryLogic categoryLogic;
    private Realm realm;
    private XMPPService xmppService;

    @Transactional
    @Override
    public AnnounceManagerDTO define(AnnounceManagerDTO dto) {
        Preconditions.checkNotNull(dto);
        //store updated announce information
        List<String> updatedId = new ArrayList<>();
        for (Announce entity : dto.getEntities()) {
            if (entity.getId() != null) {
                updatedId.add(entity.getId());
            } else {
                //this is a creation we check the right to crate an announce with category 
                if (entity.getCategories() != null) {
                    for (Category category : entity.getCategories()) {
                        final Category loadByName = categoryLogic.searchCategoryByName(category.getValue());
                        if (loadByName == null) {
                            //category not present in database (eg creation of category)
                        } else {
                            if (loadByName.getPasswordValue() != null) {
                                //this is a password protected category so we check permission
                                final CategoryPermision categoryPermision = new CategoryPermision(category.getValue());
                                try{
                                    categoryPermision.checkCreatePermission();
                                }catch(org.apache.shiro.authz.UnauthorizedException e){
                                    DTOErrorHelper.makeError(dto,"ERR_WRONG_LOGIN_PASSWORD");
                                    return dto;
                                }
                            }
                        }
                    }
                }
            }
        }
        //save announce first time to generate an id
        dto = announceLogic.define(dto);
        //update permission
        for (Announce entity : dto.getEntities()) {
            final String id = entity.getId();
            if (!updatedId.contains(id)) {
                //this is a creation
                realm.setNewAnnouncePermissionForUser(entity.getProfileId(), id);
            }
        }
        //generate a jid from id
        {
            boolean updated = false;
            for (Announce entity : dto.getEntities()) {
                final String id = entity.getId();
                if (entity.getChatId() == null) {
                    //only for announce creation
                    entity.setChatId(xmppService.createRoom("announce-" + id));
                    updated = true;
                }
            }
            if (updated) {
                //save the new modifications
                dto = announceLogic.define(dto);
            }
        }
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public AnnounceManagerDTO search(AnnounceManagerDTO dto) {
        Preconditions.checkNotNull(dto);
        return announceLogic.search(dto);
    }

    @Transactional(readOnly = true)
    @Override
    public GeoSearchResult geoSearch(GeoSearchParameters param) {
        Preconditions.checkNotNull(param);
        return announceLogic.geoSearch(param);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> idSearch(AnnounceManagerDTO param) {
        Preconditions.checkNotNull(param);
        List<String> ret = new ArrayList<>();
        final List<Long> idSearch = announceLogic.idSearch(param);
        for (Long long1 : idSearch) {
            ret.add(long1 + "");
        }
        return ret;
    }

    public AnnounceLogic getAnnounceLogic() {
        return announceLogic;
    }

    public void setAnnounceLogic(AnnounceLogic announceLogic) {
        this.announceLogic = announceLogic;
    }

    @Transactional(readOnly = false)
    @Override
    public AnnounceManagerDTO delete(AnnounceManagerDTO dto) {
        Preconditions.checkNotNull(dto);
        return announceLogic.search(dto);
    }

    @Override
    @Transactional
    public void cleanUpAnnounces() {
        /*int snapanDeleteTime = 15;//15min
        int genanDeleteTime = 10080;//7day
        {
            //delete announce that are "snapanDeleteTime" min old
            final AnnounceManagerDTO delete = new AnnounceManagerDTOBuilder().withEntities(Arrays.asList(new AnnounceBuilder().withType("snapan").build())).build();
            delete.setOptionService(new OptionServiceBuilder()
                    .withDeleteOption(new DeleteOptionBuilder().withDelete(true).build())
                    .withTimeFilterOption(new TimeFilterOptionBuilder().withLastCreatedMin(snapanDeleteTime).build())
                    .build());
            announceLogic.search(delete);
        }
        {
            //delete announce that are "genanDeleteTime" min old
            final AnnounceManagerDTO delete = new AnnounceManagerDTOBuilder().withEntities(Arrays.asList(new AnnounceBuilder().withType("genan").build())).build();
            delete.setOptionService(new OptionServiceBuilder()
                    .withDeleteOption(new DeleteOptionBuilder().withDelete(true).build())
                    .withTimeFilterOption(new TimeFilterOptionBuilder().withLastCreatedMin(genanDeleteTime).build())
                    .build());
            announceLogic.search(delete);
        }*/
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public CategoryLogic getCategoryLogic() {
        return categoryLogic;
    }

    public void setCategoryLogic(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    public XMPPService getXmppService() {
        return xmppService;
    }

    public void setXmppService(XMPPService xmppService) {
        this.xmppService = xmppService;
    }

}
