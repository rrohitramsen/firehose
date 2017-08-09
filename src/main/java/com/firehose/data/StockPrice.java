package com.firehose.data;


import java.util.Date;

/**
 * Created by rohitkumar on 02/08/17.
 * Bombay Stock Exchange Stock Data Format.
 */
public class StockPrice {

    private Date date;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Double wap;
    private Integer noOfShares;
    private Integer noOfTrades;
    private Double totalTurnover;
    private Integer deliverableQuantity;
    private Double deliQtyToTradedQty;
    private Double spreadHighLow;
    private Double spreadCloseOpen;

    public StockPrice() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getWap() {
        return wap;
    }

    public void setWap(Double wap) {
        this.wap = wap;
    }

    public Integer getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(Integer noOfShares) {
        this.noOfShares = noOfShares;
    }

    public Integer getNoOfTrades() {
        return noOfTrades;
    }

    public void setNoOfTrades(Integer noOfTrades) {
        this.noOfTrades = noOfTrades;
    }

    public Double getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(Double totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public Integer getDeliverableQuantity() {
        return deliverableQuantity;
    }

    public void setDeliverableQuantity(Integer deliverableQuantity) {
        this.deliverableQuantity = deliverableQuantity;
    }

    public Double getDeliQtyToTradedQty() {
        return deliQtyToTradedQty;
    }

    public void setDeliQtyToTradedQty(Double deliQtyToTradedQty) {
        this.deliQtyToTradedQty = deliQtyToTradedQty;
    }

    public Double getSpreadHighLow() {
        return spreadHighLow;
    }

    public void setSpreadHighLow(Double spreadHighLow) {
        this.spreadHighLow = spreadHighLow;
    }

    public Double getSpreadCloseOpen() {
        return spreadCloseOpen;
    }

    public void setSpreadCloseOpen(Double spreadCloseOpen) {
        this.spreadCloseOpen = spreadCloseOpen;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "date=" + date +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                ", wap=" + wap +
                ", noOfShares=" + noOfShares +
                ", noOfTrades=" + noOfTrades +
                ", totalTurnover=" + totalTurnover +
                ", deliverableQuantity=" + deliverableQuantity +
                ", deliQtyToTradedQty=" + deliQtyToTradedQty +
                ", spreadHighLow=" + spreadHighLow +
                ", spreadCloseOpen=" + spreadCloseOpen +
                '}';
    }

}
