package de.mnreinisch.pp.watcher.domain;

import javax.persistence.*;

public class EMFactory {
    private static EMFactory emFactory;
    private EntityManager em;

    public static EMFactory getInstance() {
        if(emFactory == null){
            emFactory = new EMFactory();
        }
        return emFactory;
    }

    public static void closeConnection() {
        if(emFactory == null) return;
        emFactory.em.close();
    }

    private EMFactory() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moneywatcher");
        em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);

    }

    public EntityManager getEm() {
        return em;
    }

    public void persist(Object obj){
        try{
            em.getTransaction().begin();
            em.persist(obj);
            em.flush();
            em.getTransaction().commit();
        } catch (Throwable e){
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void remove(Object obj){
        try {
            em.getTransaction().begin();
            em.remove(obj);
            em.flush();
            em.getTransaction().commit();
        } catch (Throwable e){
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Query createNamedQuery(String s){
        return em.createNamedQuery(s);
    }
}
