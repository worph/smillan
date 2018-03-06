/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.media;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.common.DefaultJPADAO;
import com.smilan.logic.domain.media.entity.QMediaEntity;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.media.entity.builder.MediaEntityBuilder;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class JPAMediaDAO extends DefaultJPADAO<MediaEntity> implements MediaDAOInterface {

    @Override
    public EntityPathBase<MediaEntity> forgeWhere(List<Predicate> predicates, List<MediaEntity> criteria,OptionService optionService) {
        QMediaEntity qMediaEntity = QMediaEntity.mediaEntity;

        for (MediaEntity mediaEntity : criteria) {
            if (mediaEntity.getId() != null) {
                predicates.add(qMediaEntity.id.eq(mediaEntity.getId()));
            }
            if (mediaEntity.getType()!= null) {
                predicates.add(qMediaEntity.type.eq(mediaEntity.getType()));
            }
            if (mediaEntity.getUrl()!= null) {
                predicates.add(qMediaEntity.url.eq(mediaEntity.getUrl()));
            }
        }
        return qMediaEntity;
    }

    @Override
    public MediaEntity retreiveOrCreate(Media media) {
        if(media.getId()==null){
            return new MediaEntityBuilder()
                    .withId(null)
                    .withType(media.getType())
                    .withUrl(media.getUrl())
                    .build();
        }
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<MediaEntity> query = queryFactory.select(QMediaEntity.mediaEntity).from(QMediaEntity.mediaEntity).where(QMediaEntity.mediaEntity.id.eq(Long.parseLong(media.getId())));
        final List<MediaEntity> fetch = query.fetch();
        if(fetch.isEmpty()){
            return new MediaEntityBuilder()
                    .withId(null)
                    .withType(media.getType())
                    .withUrl(media.getUrl())
                    .build();
        }
        if(fetch.size()==1){
            return fetch.get(0);
        }
        throw new Error("Can't happens");
    }

}
