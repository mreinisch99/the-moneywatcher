package de.mnreinisch.pp.watcher.control.dto;

import de.mnreinisch.pp.watcher.domain.Date;
import org.joda.time.LocalDate;

public class TransactionDTO implements Comparable<TransactionDTO> {
    private Long id;
    private double amount;
    private String info;
    private boolean vac;
    private String imgSrc;
    private String date;
    private LocalDate localDate;

    public TransactionDTO(double amount, String info, boolean vac, String imgSrc, String date) {
        this.amount = amount;
        this.info = info;
        this.vac = vac;
        this.imgSrc = imgSrc;
        this.date = date;
        this.localDate = Date.convertStringToLD(date);
    }

    public TransactionDTO(Long id, double amount, String info, boolean vac, String imgSrc, String date) {
        this.id = id;
        this.amount = amount;
        this.info = info;
        this.vac = vac;
        this.imgSrc = imgSrc;
        this.date = date;
        this.localDate = Date.convertStringToLD(date);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        this.localDate = Date.convertStringToLD(date);
    }

    @Override
    public int compareTo(TransactionDTO o) {
        return this.localDate.compareTo(o.localDate);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", info='" + info + '\'' +
                ", vac=" + vac +
                ", imgSrc='" + imgSrc + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
