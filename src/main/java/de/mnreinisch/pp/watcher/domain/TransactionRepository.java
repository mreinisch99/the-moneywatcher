package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

public class TransactionRepository {
    private static TransactionRepository _instance;

    private EntityManager em;

    public static void closeConn(){
        if(_instance == null) return;
        _instance.close();
    }

    private void close() {
        if(em == null || !em.isOpen()) return;
        em.close();
    }

    public static TransactionRepository getInstance(){
        if(_instance == null){
            _instance = new TransactionRepository();
        }
        return _instance;
    }

    private TransactionRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("moneywatcher");
        em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);
    }

    public void addTransaction(TransactionDTO dto){
        try{
            em.getTransaction().begin();
            Transaction transaction = new Transaction(dto);
            em.persist(transaction);
            em.flush();
            em.getTransaction().commit();
        } catch (Throwable e){
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Transaction findById(Long id) throws CustomException {
        if(id == null) throw new CustomException("Please provide a valid id");
        Query q = em.createNamedQuery(Transaction.Q_GET_TRANSACTION_BY_ID);
        q.setParameter(Transaction.ID_VAR, id);
        List<Transaction> resultList = q.getResultList();
        return resultList.size() == 1 ? resultList.get(0) : null;
    }

    public void deleteTransaction(Transaction trans) throws CustomException {
        if(trans.getId() == null) throw new CustomException("ID-Field of transaction mustn't be null!");
        try{
            em.getTransaction().begin();
            em.remove(trans);
            em.flush();
            em.getTransaction().commit();
        } catch (Throwable e){
            em.getTransaction().rollback();
        }
    }

    public List<Transaction> getAllTransactions(){
        Query q = em.createNamedQuery(Transaction.Q_GET_ALL_TRANSACTIONS);
        return (List<Transaction>) q.getResultList();
    }

    public List<Transaction> getTransactionsInMonth(LocalDate localDate){
        LocalDate begin = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, localDate.getYear());
        c.set(Calendar.MONTH, localDate.getMonthOfYear());
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);

        LocalDate end = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), c.get(Calendar.DAY_OF_MONTH));
        Query q = em.createNamedQuery(Transaction.Q_GET_ALL_IN_RANGE);
        q.setParameter(Transaction.BEGIN_YEAR, begin.toDateTimeAtStartOfDay().toDate());
        q.setParameter(Transaction.END_YEAR, end.toDateTimeAtStartOfDay().toDate());
        return q.getResultList();
    }

    public List<Transaction> getTransactionsInYear(int year){
        LocalDate begin = new LocalDate(year, 1, 1);
        LocalDate end = new LocalDate(year, 12, 31);
        Query q = em.createNamedQuery(Transaction.Q_GET_ALL_IN_RANGE);
        q.setParameter(Transaction.BEGIN_YEAR, begin.toDateTimeAtStartOfDay().toDate());
        q.setParameter(Transaction.END_YEAR, end.toDateTimeAtStartOfDay().toDate());
        return q.getResultList();
    }

    public void update(TransactionDTO dto) throws CustomException {
        Transaction byId = findById(dto.getId());
        if(byId == null) throw new CustomException("Couldn't find transaction with id " + dto.getId());

        if(!byId.getId().equals(dto.getId())) return;
        try{
            em.getTransaction().begin();
            byId.setAmount(dto.getAmount());
            byId.setVac(dto.isVac());
            byId.setInfo(dto.getInfo());
            byId.getDate().setDateFromString(dto.getDate());
            em.flush();
            em.getTransaction().commit();
        } catch (Throwable e){
            em.getTransaction().rollback();
        }
    }

}
