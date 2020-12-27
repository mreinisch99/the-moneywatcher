package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.List;

public class TransactionRepository {
    private static TransactionRepository _instance;

    private EntityManager em;

    public static TransactionRepository getInstance(){
        if(_instance == null){
            _instance = new TransactionRepository();
        }
        return _instance;
    }

    private TransactionRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moneywatcher");
        em = emf.createEntityManager();
    }

    public Transaction addTransaction(TransactionDTO dto){
        Transaction transaction = new Transaction(dto);
        em.persist(transaction);
        return transaction;
    }

    public Transaction findById(Long id){
        if(id == null) throw new NullPointerException("Please provide a valid id");
        Query q = em.createNamedQuery(Transaction.Q_GET_TRANSACTION_BY_ID);
        q.setParameter(Transaction.ID_VAR, id);
        List<Transaction> resultList = q.getResultList();
        return resultList.size() == 1 ? resultList.get(0) : null;
    }

    public void deleteTransaction(Transaction trans){
        if(trans.getId() == null) throw new NullPointerException("ID-Field of transaction mustn't be null!");
        em.remove(trans);
    }

    public List<Transaction> getTransactionsInYear(int year){
        LocalDate begin = new LocalDate(year, 1, 1);
        LocalDate end = new LocalDate(year, 12, 31);
        Query q = em.createNamedQuery(Transaction.Q_GET_ALL_IN_RANGE);
        q.setParameter(Transaction.BEGIN_YEAR, begin.toDateTimeAtStartOfDay().toDate());
        q.setParameter(Transaction.END_YEAR, end.toDateTimeAtStartOfDay().toDate());
        return q.getResultList();
    }
}
