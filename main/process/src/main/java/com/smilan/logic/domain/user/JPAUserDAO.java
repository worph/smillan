/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.user;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.logic.common.DefaultJPADAO;
import com.smilan.logic.domain.user.entity.QUserEntity;
import com.smilan.logic.domain.user.entity.UserEntity;
import com.smilan.logic.domain.user.entity.builder.UserEntityBuilder;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder; 

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class JPAUserDAO extends DefaultJPADAO<UserEntity> implements UserDAOInterface {

    @Override
    public EntityPathBase<UserEntity> forgeWhere(List<Predicate> predicates, List<UserEntity> criteria, OptionService optionService) {
        QUserEntity qUserEntity = QUserEntity.userEntity;

        for (UserEntity userEntity : criteria) {
            if (userEntity.getId() != null) {
                predicates.add(qUserEntity.id.eq(userEntity.getId()));
            }
            if (userEntity.getEmail() != null) {
                predicates.add(qUserEntity.email.eq(userEntity.getEmail()));
            }
            if (userEntity.getLogin() != null) {
                predicates.add(qUserEntity.login.eq(userEntity.getLogin()));
            }
            if (userEntity.getPassword() != null) {
                predicates.add(qUserEntity.password.eq(userEntity.getPassword()));
            }
            if (userEntity.getXmppPassword() != null) {
                predicates.add(qUserEntity.xmppPassword.eq(userEntity.getXmppPassword()));
            }
        }
        return qUserEntity;
    }

    @Override
    public UserEntity getUserFromUsername(String username) {
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<UserEntity> query = queryFactory.selectFrom(qUserEntity).where(qUserEntity.login.eq(username));
        final List<UserEntity> fetch = query.fetch();
        if (fetch.size() == 1) {
            return fetch.get(0);
        }
        if (fetch.isEmpty()) {
            return null;
        }
        throw new Error();
    }

    @Override
    public UserEntity getUserFromUserId(String userId) {
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<UserEntity> query = queryFactory.selectFrom(qUserEntity).where(qUserEntity.id.eq(Long.parseLong(userId)));
        final List<UserEntity> fetch = query.fetch();
        if (fetch.size() == 1) {
            return fetch.get(0);
        }
        if (fetch.isEmpty()) {
            return null;
        }
        throw new Error();
    }
    
    @Override
    public UserEntity getUserFromApiKey(String apiKey) {
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<UserEntity> query = queryFactory.selectFrom(qUserEntity).where(qUserEntity.apiKey.eq(apiKey));
        final List<UserEntity> fetch = query.fetch();
        if (fetch.size() == 1) {
            return fetch.get(0);
        }
        if (fetch.isEmpty()) {
            return null;
        }
        throw new Error();
    }

    @Override
    public void setLogin(Long id, String login) {
        final UserEntity entity = load(new UserEntityBuilder().withId(id).build());
        entity.setLogin(login);
        save(entity);
    }

    @Override
    public void setPassword(Long id, String password) {
        final UserEntity entity = load(new UserEntityBuilder().withId(id).build());
        entity.setPassword(password);
        save(entity);
    }

    @Override
    public UserEntity setUser(UserEntity user) { 
        save(user);
        return user;
    }


}
