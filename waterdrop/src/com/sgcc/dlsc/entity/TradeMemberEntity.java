package com.sgcc.dlsc.entity;

import java.math.BigDecimal;

public class TradeMemberEntity {
	private String id;
	//排序序号
	private int orderno;
	private BigDecimal price;
	private BigDecimal energy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getOrderno() {
		return orderno;
	}
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getEnergy() {
		return energy;
	}
	public void setEnergy(BigDecimal energy) {
		this.energy = energy;
	}
	@Override
	public String toString() {
		return "TradeMemberEntity [id=" + id + ", orderno=" + orderno
				+ ", price=" + price + ", energy=" + energy + "]";
	}
}
