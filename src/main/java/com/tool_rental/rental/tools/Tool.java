package com.tool_rental.rental.tools;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tools")
public class Tool {

    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type", nullable = false)
    private ToolType type;
    @Column(name = "brand", nullable = false)
    private String brand;

    public Tool(String code, ToolType type, String brand) {
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

    public Tool() {
        // for hibernate
    }

    public String getCode() {
        return code;
    }

    public ToolType getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }
}
