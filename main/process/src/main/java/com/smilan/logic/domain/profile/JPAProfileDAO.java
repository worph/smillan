/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.profile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.Order;
import com.smilan.logic.common.DefaultJPADAO;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.profile.entity.ProfileEntity;
import com.smilan.logic.domain.profile.entity.QProfileEntity;
import com.smilan.logic.domain.user.entity.QUserEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class JPAProfileDAO extends DefaultJPADAO<ProfileEntity> implements ProfileDAOInterface {

    @Override
    public EntityPathBase<ProfileEntity> forgeWhere(List<Predicate> predicates, List<ProfileEntity> criteria,OptionService optionService) {
        QProfileEntity qProfileEntity = QProfileEntity.profileEntity;

        for (ProfileEntity userEntity : criteria) {
            if (userEntity.getId() != null) {
                predicates.add(qProfileEntity.id.eq(userEntity.getId()));
            }
            if (userEntity.getUserId() != null) {
                predicates.add(qProfileEntity.userId.eq(userEntity.getUserId()));
            }
            if (userEntity.getProfileName() != null) {
                predicates.add(qProfileEntity.profileName.eq(userEntity.getProfileName()));
            }
            if (userEntity.getXmppId() != null) {
                predicates.add(qProfileEntity.xmppId.eq(userEntity.getXmppId()));
            }
            if (userEntity.getAvatar() != null) {
                predicates.add(qProfileEntity.avatar.eq(userEntity.getAvatar()));
            }
        }
        return qProfileEntity;
    }

    @Override
    public ArrayList<OrderSpecifier<?>> forgeOrder(List<Order> orders) {
        QProfileEntity qProfileEntity = QProfileEntity.profileEntity;
        ArrayList<OrderSpecifier<?>> ret =new ArrayList<>();
        for (com.smilan.api.common.manager.option.Order order : orders) {
            if (order.getItem().equals("created")) {
                final DateTimePath<Date> path = qProfileEntity.created;
                if ("desc".equals(order.getDirection())) {
                    ret.add(path.desc());
                } else {
                    ret.add(path.asc());
                }
            } else {
                throw new Error();
            }
        }
        return ret;
    }

    @Override
    public Long getIDFromUserID(Long userID) {
        QProfileEntity qProfileEntity = QProfileEntity.profileEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<Long> query = queryFactory.select(qProfileEntity.id).from(qProfileEntity).where(qProfileEntity.userId.eq(userID));
        final List<Long> fetch = query.fetch();
        if(fetch.size()==1){
            return fetch.get(0);
        }
        if(fetch.isEmpty()){
            return null;
        }
        throw new Error();
    }

}
