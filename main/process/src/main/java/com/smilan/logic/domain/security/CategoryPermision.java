package com.smilan.logic.domain.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * category:<ressource>:<add announce/read announce/manage category>
 *
 * @author pierr
 */
public class CategoryPermision {

    public String ressourceName;

    public static final String PREFIX = "category:";
    public static final String ADD = "add";//ability to create an announce with this category
    public static final String READ = "read";//ability to read an announce with this category
    public static final String UPDATE = "update";//ability to update the category info (eg password/icon)

    public CategoryPermision(String ressourceName) {
        this.ressourceName = PREFIX + ressourceName;
    }

    public void changeRessource(String ressourceName) {
        this.ressourceName = PREFIX + ressourceName;
    }

    public String getRessourceSecurityStringID() {
        return ressourceName;
    }
    
    public String permissionAll() {
        return ressourceName + ":*";
    }

    public String permissionAdd() {
        return ressourceName + ":" + CategoryPermision.ADD;
    }

    public String permissionRead() {
        return ressourceName + ":" + CategoryPermision.READ;
    }

    public String permissionUpdate() {
        return ressourceName + ":" + CategoryPermision.UPDATE;
    }

    public void checkCreatePermission() {
        checkCategoryPermision(ADD);
    }

    public void checkUpdatePermission() {
        checkCategoryPermision(UPDATE);
    }

    public boolean hasReadPermission() {
        return hasCategoryPermision(READ);
    }

    public void checkReadPermission() {
        checkCategoryPermision(READ);
    }

    public boolean hasCategoryPermision(String operation) {
        final Subject subject = SecurityUtils.getSubject();
        try {
            switch (operation) {
                case ADD:
                    return subject.isPermitted(getRessourceSecurityStringID() + ":" + ADD);
                case READ:
                    return subject.isPermitted(getRessourceSecurityStringID() + ":" + READ);
                case UPDATE:
                    return subject.isPermitted(getRessourceSecurityStringID() + ":" + UPDATE);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (org.apache.shiro.authz.UnauthorizedException exception) {
            String username = subject.getPrincipal() + "";
            System.out.println("Userid : " + username);
            throw exception;
        }
    }

    public void checkCategoryPermision(String operation) {
        final Subject subject = SecurityUtils.getSubject();
        try {
            switch (operation) {
                case ADD:
                    subject.checkPermission(getRessourceSecurityStringID() + ":" + ADD);
                    break;
                case READ:
                    subject.checkPermission(getRessourceSecurityStringID() + ":" + READ);
                    break;
                case UPDATE:
                    subject.checkPermission(getRessourceSecurityStringID() + ":" + UPDATE);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (org.apache.shiro.authz.UnauthorizedException exception) {
            String username = subject.getPrincipal() + "";
            System.out.println("Userid : " + username);
            throw exception;
        }
    }
}
