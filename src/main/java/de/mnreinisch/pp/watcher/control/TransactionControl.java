package de.mnreinisch.pp.watcher.control;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.domain.Transaction;
import de.mnreinisch.pp.watcher.domain.TransactionRepository;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionControl {
    private TransactionRepository transactionRepository = TransactionRepository.getInstance();

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.getAllTransactions();
        return transactions
                .stream()
                .map(Transaction::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionInMonth(LocalDate localDate){
        List<Transaction> transactions = transactionRepository.getTransactionsInMonth(localDate);
        return transactions
                .stream()
                .map(Transaction::toDTO)
                .collect(Collectors.toList());
    }

    public void addTransaction(double amount, String info, boolean vac, String img, String date){
        TransactionDTO transactionDTO = new TransactionDTO(amount, info, vac, img, date);
        transactionRepository.addTransaction(transactionDTO);
    }

    public void removeTransaction(TransactionDTO transactionDTO) throws CustomException {
        if(transactionDTO.getId() == null) throw new CustomException("ID from given transaction object is null!");
        Transaction byId = transactionRepository.findById(transactionDTO.getId());
        if(byId == null) throw new CustomException("Couldn't find transaction with id " + transactionDTO.getId());
        transactionRepository.deleteTransaction(byId);
    }

    public List<TransactionDTO> getAllTransactionsByYear(int year) {
        List<Transaction> transactions = transactionRepository.getTransactionsInYear(year);
        return transactions
                .stream()
                .map(Transaction::toDTO)
                .collect(Collectors.toList());
    }

    public void updateEntity(TransactionDTO transactionDTO) throws CustomException {
        if(transactionDTO.getId() == null) throw new CustomException("ID from given transaction object is null!");
        transactionRepository.update(transactionDTO);
    }
}
