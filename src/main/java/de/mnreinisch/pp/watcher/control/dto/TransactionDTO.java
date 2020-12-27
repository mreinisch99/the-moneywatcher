package de.mnreinisch.pp.watcher.control.dto;

import org.joda.time.LocalDate;

public class TransactionDTO {
    private Long id;
    private double amount;
    private String info;
    private boolean vac;
    private String imgSrc;
    private LocalDate date;

    public TransactionDTO(Long id, double amount, String info, boolean vac, String imgSrc, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.info = info;
        this.vac = vac;
        this.imgSrc = imgSrc;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
