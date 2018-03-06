package com.smilan.api.common.manager.option;

import java.io.Serializable;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Option générique d'appel au service de recherche
 *
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class SearchOption implements Serializable,Option {

    /** Generated SerialVersionUID */
    private static final long serialVersionUID = -7280120992283176802L;
    public static final String EXP_OR = "or";
    public static final String EXP_AND = "and";

    private Integer           number;//limit

    private Integer           page;

    private List<Order>            order;

    private String            expression;//"or" or "and"

    /** Default constructor */
    public SearchOption() {
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OptionRecherche [nombreEntree=").append(this.number).append(", pageNumero=").append(this.page).append(", tri=")
                .append(", ordre=")
        .append(this.order).append(", expressionOperateurRechercheMultiple=").append(this.expression).append("]");
        return builder.toString();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }
}
