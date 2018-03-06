package com.smilan.api.common.manager.option;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Option générique d'appel au service
 *
 * @author Thomas
 *
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class OptionService implements Serializable {

    /** Generated SerialVersionUID */
    private static final long serialVersionUID = -9050329181638510091L;

    private SearchOption   searchOption;
    private ErrorOption   errorOption;
    private DeleteOption   deleteOption;
    private TimeFilterOption   timeFilterOption;

    /** Default constructor */
    public OptionService() {
    }

    @Override
    public String toString() {
        return "OptionService{" + "searchOption=" + searchOption + ", errorOption=" + errorOption + '}';
    }

    public SearchOption getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(SearchOption searchOption) {
        this.searchOption = searchOption;
    }

    public ErrorOption getErrorOption() {
        return errorOption;
    }

    public void setErrorOption(ErrorOption errorOption) {
        this.errorOption = errorOption;
    }

    public DeleteOption getDeleteOption() {
        return deleteOption;
    }

    public void setDeleteOption(DeleteOption deleteOption) {
        this.deleteOption = deleteOption;
    }

    public TimeFilterOption getTimeFilterOption() {
        return timeFilterOption;
    }

    public void setTimeFilterOption(TimeFilterOption timeFilterOption) {
        this.timeFilterOption = timeFilterOption;
    }
    
    public List<Option> getOptions(){
        return Arrays.asList(searchOption,errorOption,deleteOption,timeFilterOption);
    }
    
}
