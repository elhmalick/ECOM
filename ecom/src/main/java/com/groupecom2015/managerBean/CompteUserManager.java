package com.groupecom2015.managerBean;

import com.groupecom2015.entities.CompteUser;
import com.groupecom2015.entitieManager.CompteUserFacade;
import com.groupecom2015.managerBean.util.SessionManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

@Named("compteUserManager")
@SessionScoped
public class CompteUserManager implements Serializable {

    private CompteUser compte;
    private List<CompteUser> comptes;
    private String isClient = "none";
    private String isAdmin = "none";
    private String isLogin = "none";
    private String isNotLogin = "none";
    @EJB
    private CompteUserFacade compteUserFacade;

    public CompteUserManager(){
        comptes = null;
        compte = new CompteUser();
    }

    public List<CompteUser> getComptes() {
        if(comptes == null){
            comptes = compteUserFacade.findAll();
        }
        return comptes;
    }

    public void setComptes(List<CompteUser> comptes) {
        this.comptes = comptes;
    }
    

    public String getIsNotLogin() {
        return isNotLogin;
    }

    public void setIsNotLogin(String isNotLogin) {
        this.isNotLogin = isNotLogin;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsClient() {
        return isClient;
    }

    public void setIsClient(String isClient) {
        this.isClient = isClient;
    }

    public CompteUserFacade getCompteUserFacade() {
        return compteUserFacade;
    }

    public void setCompteUserFacade(CompteUserFacade compteUserFacade) {
        this.compteUserFacade = compteUserFacade;
    }

    public void setCompte(CompteUser compte) {
        this.compte = compte;
    }    

    public String addUser() {
        compteUserFacade.create(compte);
        return "login";
    }

    public String connecter() {
        if (compte.getEmail() != null && compte.getMotDePasse() != null) {
            boolean valide = compteUserFacade.connect(compte);
            if (valide) {
                compte = compteUserFacade.findByEmail(compte.getEmail());
                return "index";
            }            
        }
        compte = new CompteUser();
        return "login";
    }

    public void verifierLogin() {
        if (compte.getEmail() != null) {
            this.isLogin = "";
            this.isNotLogin = "none";
        } else {
            this.isNotLogin = "";
            this.isLogin = "none";
        }
    }

    public String verifier() {
        if (compte.getEmail() != null) {

            if (compte.getTypeCompte().equals("admin")) {
                isClient = "none";
                isAdmin = "";
            } else {
                isClient = "";
                isAdmin = "none";
            }
            return "generation";
        }
        return "login";
    }

    public String deconnecter() {
        SessionManager session = SessionManager.getInstance();
        session.set("email", null);
        compte = null;
        return "login";
    }

    public String supprimerCompteUser() {
        SessionManager session = SessionManager.getInstance();
        String email = session.get("email").toString();
        compteUserFacade.remove(compteUserFacade.find(email));
        compte = null;
        return "index";
    }

    public String adminSupprimerCompteUser(String email) {
        if (email.isEmpty()) {
            return "";
        } else {
            compteUserFacade.remove(compteUserFacade.find(email));
            comptes = compteUserFacade.findAll();
            return "displayCompteUser";
        }
    }

    public String supprimerCompteUser(String email) {
        compteUserFacade.remove(compteUserFacade.find(email));
        return "displayCompteUser";
    }

    public CompteUser getCompte() {
        if (compte == null) {
            compte = new CompteUser();           
        }
        return compte;
    }

    private CompteUserFacade getFacade() {
        return compteUserFacade;
    }

    public String prepareEdit() {
        compte = compteUserFacade.find(compte.getEmail());
        return "modifierCompteUser";
    }

    public String update() {
        try {
            getFacade().edit(compte);
            return "generation";
        } catch (Exception e) {
            return null;
        }
    }    

    public CompteUser getCompteUser(String id) {
        return compteUserFacade.find(id);
    }

    public void onRowEdit(RowEditEvent event) {
        CompteUser cu = (CompteUser) event.getObject();
        compteUserFacade.edit(cu);
    }

    public void onRowCancel(RowEditEvent event) {
    }
}
