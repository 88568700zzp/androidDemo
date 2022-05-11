package com.zzp.applicationkotlin.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class Book {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private Integer goodsId;
    private String name;

    @Generated(hash = 183836134)
    public Book(Long id, Integer goodsId, String name) {
        this.id = id;
        this.goodsId = goodsId;
        this.name = name;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
