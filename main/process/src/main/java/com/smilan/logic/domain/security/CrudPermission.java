/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.security;

import com.google.common.base.Preconditions;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
//CRUD = Create Read Update Delete
//crud:<ressource>:<CRUD>:<id (for RUD)>:<field (for RU)>
 * @author pierr
 */
public class CrudPermission {
    public final String ressourceName;
    
    public static final String PREFIX = "crud:";
    public static final String CREATE = "create";
    public static final String READ = "read";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    public CrudPermission(String ressourceName) {
        this.ressourceName = PREFIX+ressourceName;
    }
    
    public String getRessourceSecurityStringID(){
        return ressourceName;
    }

    public String getRessourceName() {
        return ressourceName;
    } 
        
    public String permissionCreate(){
        return ressourceName+":"+CrudPermission.CREATE;
    }
    
    public String permissionReadDelete(String id,String field){
        return ressourceName+":"+CrudPermission.READ+","+CrudPermission.DELETE+":"+id+":"+field;
    }
        
    public String permissionDelete(String id){
        return ressourceName+":"+CrudPermission.DELETE+":"+id;
    }
    
    public String permissionRead(String id,String field){
        return ressourceName+":"+CrudPermission.READ+":"+id+":"+field;
    }
    
    public String permissionUpdateDelete(String id,String field){
        return ressourceName+":"+CrudPermission.UPDATE+","+CrudPermission.DELETE+":"+id+":"+field;
    }
    public String permissionUpdate(String id,String field){
        return ressourceName+":"+CrudPermission.UPDATE+":"+id+":"+field;
    }
        
    public void checkCreatePermission(){
        checkCrudPermission(CREATE,null,null);
    }
    
    public void checkDeletePermission(Long id){
        checkCrudPermission(DELETE,id,null);
    }
    
    public void checkUpdatePermission(Long id,String field){
        checkCrudPermission(UPDATE,id,field);
    }
    
    public boolean hasReadPermission(Long id,String field){
        return hasCrudPermission(READ,id,field);
    }
        
    public void checkReadPermission(Long id,String field){
        checkCrudPermission(READ,id,field);
    }
    
    public boolean hasCrudPermission(String crud,Long id,String field){
        final Subject subject = SecurityUtils.getSubject();
        try{
            switch(crud){
                case CREATE:
                    Preconditions.checkArgument(id==null);
                    Preconditions.checkArgument(field==null);
                    return subject.isPermitted(getRessourceSecurityStringID()+":"+CREATE);
                case READ:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkNotNull(field);
                    return subject.isPermitted(getRessourceSecurityStringID()+":"+READ+":"+id+":"+field);
                case UPDATE:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkNotNull(field);
                    return subject.isPermitted(getRessourceSecurityStringID()+":"+UPDATE+":"+id+":"+field);
                case DELETE:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkArgument(field==null);
                    return subject.isPermitted(getRessourceSecurityStringID()+":"+DELETE+":"+id);
                default:throw new IllegalArgumentException();
            }
        }catch(org.apache.shiro.authz.UnauthorizedException exception){
            String username = subject.getPrincipal()+"";
            System.out.println("Userid : "+username);
            throw exception;
        }
    }
    
    public void checkCrudPermission(String crud,Long id,String field){
        final Subject subject = SecurityUtils.getSubject();
        try{
            switch(crud){
                case CREATE:
                    Preconditions.checkArgument(id==null);
                    Preconditions.checkArgument(field==null);
                    subject.checkPermission(getRessourceSecurityStringID()+":"+CREATE);
                    break;
                case READ:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkNotNull(field);
                    subject.checkPermission(getRessourceSecurityStringID()+":"+READ+":"+id+":"+field);
                    break;
                case UPDATE:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkNotNull(field);
                    subject.checkPermission(getRessourceSecurityStringID()+":"+UPDATE+":"+id+":"+field);
                    break;
                case DELETE:
                    Preconditions.checkNotNull(id);
                    Preconditions.checkArgument(field==null);
                    subject.checkPermission(getRessourceSecurityStringID()+":"+DELETE+":"+id);
                    break;
                default:throw new IllegalArgumentException();
            }
        }catch(org.apache.shiro.authz.UnauthorizedException exception){
            String username = subject.getPrincipal()+"";
            System.out.println("Userid : "+username);
            throw exception;
        }
    }

}
