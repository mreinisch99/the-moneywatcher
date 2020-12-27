package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = Transaction.Q_GET_ALL_IN_RANGE, query = "select t from Transaction t where t.date.date between :" + Transaction.BEGIN_YEAR + " and :" + Transaction.END_YEAR),
        @NamedQuery(name = Transaction.Q_GET_TRANSACTION_BY_ID, query = "select t from Transaction t where t.id = :" + Transaction.ID_VAR)
})

@Entity
@Table(name = "WATCH_TRANSACTION")
public class Transaction implements Serializable {
    private static final long serialVersionUID = -4281575077333973070L;

    public static final String Q_GET_ALL_IN_RANGE = "Q_GET_ALL_IN_RANGE";
    public static final String Q_GET_TRANSACTION_BY_ID = "Q_GET_TRANSACTION_BY_ID";
    public static final String BEGIN_YEAR = "BEGIN_YEAR";
    public static final String END_YEAR = "END_YEAR";
    public static final String ID_VAR = "ID_VAR";

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private double amount;

    @Column(name = "INFO")
    private String info;

    @Column(name = "VAC", columnDefinition = "DEFAULT 0")
    private boolean vac = false;

    @Column(name = "IMG")
    private String imgSrc;

    @Embedded
    private Date date;


    public Transaction(Long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Transaction(TransactionDTO dto){
        this.id = dto.getId();
        this.amount = dto.getAmount();
        this.info = dto.getInfo();
        this.vac = dto.isVac();
        this.imgSrc = dto.getImgSrc();
        this.date = new Date(dto.getDate());
    }

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isVac() {
        return vac;
    }

    public void setVac(boolean vac) {
        this.vac = vac;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public TransactionDTO toDTO(){
        return new TransactionDTO(id, amount, info, vac, imgSrc, date.getDateAsLD());
    }
}
