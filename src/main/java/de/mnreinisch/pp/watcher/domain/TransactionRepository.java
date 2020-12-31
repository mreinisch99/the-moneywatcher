package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import org.joda.time.LocalDate;

import javax.persistence.Query;
import java.util.List;

public class TransactionRepository {
    private static TransactionRepository _instance;
    private EMFactory emFactory;

    public static TransactionRepository getInstance(){
        if(_instance == null){
            _instance = new TransactionRepository();
        }
        return _instance;
    }

    private TransactionRepository() {
        emFactory = EMFactory.getInstance();
    }

    public void addTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction(dto);
        emFactory.persist(transaction);
    }

    public Transaction findById(Long id) throws CustomException {
        if(id == null) throw new CustomException("Please provide a valid transaction id!");
        Query q = emFactory.createNamedQuery(Transaction.Q_GET_TRANSACTION_BY_ID);
        q.setParameter(Transaction.ID_VAR, id);
        List<Transaction> resultList = q.getResultList();
        return resultList.size() == 1 ? resultList.get(0) : null;
    }

    public void deleteTransaction(Transaction dto) throws CustomException {
        if(dto.getId() == null) throw new CustomException("Please provide a valid transaction id!");
        Transaction byId = findById(dto.getId());
        emFactory.remove(byId);
    }

    public List<Transaction> getTransactionsInRange(LocalDate startTime, LocalDate endDate){
        Query q = emFactory.createNamedQuery(Transaction.Q_GET_ALL_IN_RANGE);
        q.setParameter(Transaction.BEGIN_YEAR, startTime.toDateTimeAtStartOfDay().toDate());
        q.setParameter(Transaction.END_YEAR, endDate.toDateTimeAtStartOfDay().toDate());
        return q.getResultList();
    }

    public void update(TransactionDTO dto) throws CustomException {
        if(dto.getId() == null) throw new CustomException("Please provide a valid transaction id!");
        Transaction byId = findById(dto.getId());
        if(byId == null) throw new CustomException("Couldn't find transaction with id " + dto.getId());

        if(!byId.getId().equals(dto.getId())) return;
        try{
            emFactory.getEm().getTransaction().begin();
            byId.setAmount(dto.getAmount());
            byId.setVac(dto.isVac());
            byId.setInfo(dto.getInfo());
            byId.getDate().setDateFromString(dto.getDate());
            emFactory.getEm().flush();
            emFactory.getEm().getTransaction().commit();
        } catch (Throwable e){
            emFactory.getEm().getTransaction().rollback();
        }
    }

}
