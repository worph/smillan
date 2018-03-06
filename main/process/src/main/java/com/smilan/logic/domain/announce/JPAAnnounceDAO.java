/*
 * AnnounceEntityo change this license header, choose License Headers in Project Properties.
 * AnnounceEntityo change this template file, choose AnnounceEntityools | AnnounceEntityemplates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.announce;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPADeleteClause; 
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.DeleteOption;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.Order;
import com.smilan.api.common.manager.option.SearchOption;
import com.smilan.api.common.manager.option.TimeFilterOption;
import com.smilan.api.domain.announce.dto.GeoSearchParameters;
import com.smilan.api.domain.announce.dto.GeoSearchResult;
import com.smilan.api.domain.announce.dto.builder.GeoSearchElementBuilder;
import com.smilan.logic.common.DefaultJPADAO;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import com.smilan.logic.domain.announce.entity.QAnnounceEntity;
import com.smilan.logic.domain.announce.geolocalisation.MySqlDialectExtended;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.category.entity.QCategoryEntity;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.security.CategoryPermision;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class JPAAnnounceDAO extends DefaultJPADAO<AnnounceEntity> implements AnnounceDAOInterface {

    @Override
    public List<AnnounceEntity> search(List<AnnounceEntity> criteria, OptionService optionService) {
        SearchOption searchOption = null;
        DeleteOption deleteOption = null;
        if (optionService != null) {
            searchOption = optionService.getSearchOption();
            deleteOption = optionService.getDeleteOption();
        }
        final HashMap<String, Object> ret = getGeoDistPath(searchOption);
        BooleanExpression expression = (BooleanExpression) ret.get("expression");
        NumberTemplate<Float> geodistPath = (NumberTemplate<Float>) ret.get("geodistPath");

        Predicate[] whereDataPredicates = computeWhereDataPredicate(criteria, optionService, expression);
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);

        if (deleteOption == null ? true : !deleteOption.isDelete()) {
            final JPAQuery<AnnounceEntity> query = queryFactory.selectFrom(QAnnounceEntity.announceEntity).where(whereDataPredicates);
            applySearchOptionToQuery(query, searchOption, geodistPath);
            return query.fetch();
        } else {
            final JPADeleteClause delete = createDeleteQueryAndCheckPermissionMulti(whereDataPredicates);
            final long execute = delete.execute();
            deleteOption.setDeletedNumber((int) execute);
            return null;
        }
    }

    private List<Long> searchIdInternal(List<AnnounceEntity> criteria, OptionService optionService) {
        SearchOption searchOption = null;
        if (optionService != null) {
            searchOption = optionService.getSearchOption();
        }
        final HashMap<String, Object> ret = getGeoDistPath(searchOption);
        BooleanExpression expression = (BooleanExpression) ret.get("expression");
        NumberTemplate<Float> geodistPath = (NumberTemplate<Float>) ret.get("geodistPath");

        Predicate[] whereDataPredicates = computeWhereDataPredicate(criteria, optionService, expression);
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);

        final JPAQuery<Long> query = queryFactory.select(QAnnounceEntity.announceEntity.id).from(QAnnounceEntity.announceEntity).where(whereDataPredicates);

        applySearchOptionToQuery(query, searchOption, geodistPath);

        return query.fetch();
    }

    private HashMap<String, Object> getGeoDistPath(SearchOption searchOption) {
        HashMap<String, Object> ret = new HashMap<>();
        NumberTemplate<Float> geodistPath = null;
        float grodistMax = 0;
        {
            if (searchOption != null) {
                if (searchOption.getOrder() != null) {
                    for (com.smilan.api.common.manager.option.Order order : searchOption.getOrder()) {
                        if (order.getItem().equals("geodist")) {
                            final String parameters = order.getParameters();
                            final String[] split = parameters.split(",");
                            geodistPath = Expressions.numberTemplate(Float.class,
                                    "function('" + MySqlDialectExtended.geodistFunction + "'," + AnnounceEntity.latLocationPath + "," + AnnounceEntity.lonLocationPath + "," + split[0] + "," + split[1] + ")");
                            grodistMax = Float.parseFloat(split[2]);
                        }
                    }
                }
            }
        }
        BooleanExpression expression = null;
        if (geodistPath != null) {
            expression = geodistPath.between(0, grodistMax);
        }
        ret.put("expression", expression);
        ret.put("geodistPath", geodistPath);
        return ret;
    }

    private Predicate[] computeWhereDataPredicate(List<AnnounceEntity> criteria, OptionService optionService, BooleanExpression geodistPath) {
        SearchOption searchOption = null;
        if (optionService != null) {
            searchOption = optionService.getSearchOption();
        }
        List<Predicate> whereDataPredicates = new ArrayList<>();
        BooleanBuilder whereData = new BooleanBuilder();
        List<Predicate> predicates = new ArrayList<>();
        EntityPathBase<AnnounceEntity> qEntity = forgeWhere(predicates, criteria, optionService);
        if (searchOption == null
                ? true
                : (searchOption.getExpression() == null ? true : !searchOption.getExpression().equals(SearchOption.EXP_OR))) {
            for (Predicate predicate : predicates) {
                whereData.and(predicate);
            }
        } else {
            for (Predicate predicate : predicates) {
                whereData.or(predicate);
            }
        }
        whereDataPredicates.add(whereData);
        if (geodistPath != null) {
            whereDataPredicates.add(geodistPath);
        }
        return whereDataPredicates.toArray(new Predicate[0]);
    }

    private void applySearchOptionToQuery(JPAQuery query, SearchOption searchOption, NumberTemplate<Float> geodistPath) {
        if (searchOption != null) {
            if (searchOption.getNumber() != null) {
                if (searchOption.getPage() != null) {
                    query.offset(searchOption.getPage() * searchOption.getNumber());
                }
                query.limit(searchOption.getNumber());
            }
            if (searchOption.getOrder() != null) {
                final List<OrderSpecifier<?>> orderData = forgeOrder(searchOption.getOrder(), geodistPath);
                query.orderBy(orderData.toArray(new OrderSpecifier<?>[0]));
                //query.orderBy(Expressions.numberAnnounceEntityemplate(cl, template, args))
            }
        }
    }

    @Override
    public List<Long> idSearch(List<AnnounceEntity> announces, OptionService optionService) {
        List<AnnounceEntity> announcesCriteria = new ArrayList<>();
        if (announces.isEmpty()) {
            throw new IllegalArgumentException("must have at least one entity");
        }
        List<Long> searchResult = new ArrayList<>();
        for (AnnounceEntity announce : announces) {
            announcesCriteria.add(announce);
        }
        if (!announcesCriteria.isEmpty()) {
            final List<Long> search = searchIdInternal(announcesCriteria, optionService);
            if (search != null) {
                //note: null is in case of delete
                searchResult.addAll(search);
            }
        }
        try {
            return searchResult;
        } catch (javax.persistence.EntityNotFoundException ex) {
            return searchResult;
        }
    }

    @Override
    public EntityPathBase<AnnounceEntity> forgeWhere(List<Predicate> predicates, List<AnnounceEntity> criteria, OptionService optionService) {
        QAnnounceEntity qAnnounceEntity = QAnnounceEntity.announceEntity;
        TimeFilterOption timeFilterOption = null;
        if (optionService != null) {
            timeFilterOption = optionService.getTimeFilterOption();
            if (timeFilterOption != null) {
                if (timeFilterOption.getLastUpdatedMin() > 0) {
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MINUTE, -timeFilterOption.getLastUpdatedMin());
                    Date dateInPast = now.getTime();
                    predicates.add(qAnnounceEntity.updated.before(dateInPast));
                }
                if (timeFilterOption.getLastCreatedMin() > 0) {
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MINUTE, -timeFilterOption.getLastCreatedMin());
                    Date dateInPast = now.getTime();
                    predicates.add(qAnnounceEntity.created.before(dateInPast));
                }
            }
        }
        for (AnnounceEntity announceEntity : criteria) {
            if (announceEntity.getId() != null) {
                predicates.add(qAnnounceEntity.id.eq(announceEntity.getId()));
            }
            if (announceEntity.getText() != null) {
                predicates.add(qAnnounceEntity.text.eq(announceEntity.getText()));
            }
            if (announceEntity.getTitle() != null) {
                predicates.add(qAnnounceEntity.title.eq(announceEntity.getTitle()));
            }
            if (announceEntity.getProfileId() != null) {
                predicates.add(qAnnounceEntity.profileId.eq(announceEntity.getProfileId()));
            }
            if (announceEntity.getCategories() != null) {
                if(announceEntity.getCategories().isEmpty()){
                    //we search in all the category available (eg not passworded) or anounce without
                    predicates.add(qAnnounceEntity.categories.any().password.isNotNull().not());
                }else{
                    for (CategoryEntity category : announceEntity.getCategories()) {
                        final CategoryPermision categoryPermision = new CategoryPermision(category.getValue());
                        if (categoryPermision.hasReadPermission()) {
                            predicates.add(qAnnounceEntity.categories.any().value.eq(category.getValue()));
                        } else {
                            //if user don't have the right for this category we just don't display it if this category has a password
                            final QCategoryEntity any = qAnnounceEntity.categories.any();
                            final QCategoryEntity any2 = qAnnounceEntity.categories.any();
                            predicates.add(any.value.eq(category.getValue()).and(any.password.isNull()).and(any2.password.isNotNull().not()));
                        }
                    }
                }
            } else {
                //we search in all the category available (eg not passworded)
                predicates.add(qAnnounceEntity.categories.any().password.isNotNull().not());
            }
            if (announceEntity.getType() != null) {
                predicates.add(qAnnounceEntity.type.eq(announceEntity.getType()));
            }
            if (announceEntity.getMedia() != null) {
                for (MediaEntity media : announceEntity.getMedia()) {
                    predicates.add(qAnnounceEntity.media.any().id.eq(media.getId()));
                }
            }
        }
        return qAnnounceEntity;
    }

    public List<OrderSpecifier<?>> forgeOrder(List<com.smilan.api.common.manager.option.Order> orders, NumberTemplate<Float> geodistPath) {
        QAnnounceEntity qAnnounceEntity = QAnnounceEntity.announceEntity;
        ArrayList<OrderSpecifier<?>> ret = new ArrayList<>();
        for (com.smilan.api.common.manager.option.Order order : orders) {
            if (order.getItem().equals("created")) {
                final DateTimePath<Date> path = qAnnounceEntity.created;
                if (Order.SORT_DESC.equals(order.getDirection())) {
                    ret.add(path.desc());
                } else {
                    ret.add(path.asc());
                }
            } else if (order.getItem().equals("geodist") && geodistPath != null) {
                if (Order.SORT_DESC.equals(order.getDirection())) {
                    ret.add(geodistPath.desc());
                } else {
                    ret.add(geodistPath.asc());
                }
            } else {
                throw new Error();
            }
        }
        return ret;
        //CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        //ret.add(Expressions.numberTemplate(Integer.class,"function('geodist',announceEntity.location.lat,announceEntity.location.lon,1.0,1.0)").asc());
        //ret.add(qAnnounceEntity.created.desc());
        //ret.add(Expressions.numberTemplate(Integer.class,"add_ph(announceEntity.location.lat,{0})", 1.0f).asc());
        //ret.add(Expressions.numberTemplate(Integer.class,"function('geodist',announceEntity.location.lat,announceEntity.location.lon,{0},{1})", 1.0f,1.0f).asc());
        //Expressions.
        //ret.add(CustomizedOrderBy.sqlFormula("add_ph("+qAnnounceEntity.location.lon))
        //Expression<Integer> i1 = cb.literal(1);
        //ret.add(new OrderSpecifier<Integer> (Order.ASC, cb.function("add_ph",Integer.TYPE,i1,qAnnounceEntity.location.lat.as("lat"))));
    }

    @Override
    public GeoSearchResult geoSearch(GeoSearchParameters param) {
        NumberTemplate<Float> geodistPath = Expressions.numberTemplate(Float.class,
                "function('" + MySqlDialectExtended.geodistFunction + "'," + AnnounceEntity.latLocationPath + "," + AnnounceEntity.lonLocationPath + "," + param.getLat() + "," + param.getLon() + ")");
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<Tuple> query
                = queryFactory.select(QAnnounceEntity.announceEntity.id, QAnnounceEntity.announceEntity.location.lat, QAnnounceEntity.announceEntity.location.lon)
                        .from(QAnnounceEntity.announceEntity).where(geodistPath.between(0.0f, param.getDist())).orderBy(geodistPath.asc());
        final List<Tuple> fetch = query.fetch();
        final GeoSearchResult geoSearchResult = new GeoSearchResult();
        ArrayList<GeoSearchResult.GeoSearchElement> sringsList = new ArrayList<>();
        for (Tuple value : fetch) {
            final GeoSearchResult.GeoSearchElement elm = new GeoSearchElementBuilder()
                    .withId(value.get(QAnnounceEntity.announceEntity.id) + "")
                    .withLat(value.get(QAnnounceEntity.announceEntity.location.lat) + "")
                    .withLng(value.get(QAnnounceEntity.announceEntity.location.lon) + "")
                    .build();
            sringsList.add(elm);
        }
        geoSearchResult.setAnnounces(sringsList);
        return geoSearchResult;
    }

    @Override
    public List<Long> getAnnounceId(Long profileId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<Long> query = queryFactory.select(QAnnounceEntity.announceEntity.id).from(QAnnounceEntity.announceEntity).where(QAnnounceEntity.announceEntity.profileId.eq(profileId));
        return query.fetch();
    }

    @Override
    public JPADeleteClause createDeleteQueryAndCheckPermission(BooleanBuilder whereData) {
        return createDeleteQueryAndCheckPermissionMulti(whereData);
    }

    public JPADeleteClause createDeleteQueryAndCheckPermissionMulti(Predicate... predicates) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<Long> query = queryFactory.select(QAnnounceEntity.announceEntity.id).from(QAnnounceEntity.announceEntity).where(predicates);
        final List<Long> idsToDelete = query.fetch();
        //check delete right on ressource
        for (Long idToDelete : idsToDelete) {
            getCrudPermission().checkDeletePermission(idToDelete);
        }
        final JPADeleteClause delete = queryFactory.delete(QAnnounceEntity.announceEntity).where(QAnnounceEntity.announceEntity.id.in(idsToDelete));
        return delete;
    }

}
