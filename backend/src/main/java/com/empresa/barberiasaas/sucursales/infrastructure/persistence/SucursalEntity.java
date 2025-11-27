package com.empresa.barberiasaas.sucursales.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sucursales")
public class SucursalEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String nombre;
    private String slug;
    private String direccion;
    private String ciudad;

    @Column(name = "branding_primary_color")
    private String brandingPrimaryColor;

    @Column(name = "branding_highlight_color")
    private String brandingHighlightColor;

    @Column(name = "branding_accent_color")
    private String brandingAccentColor;

    @Column(name = "branding_headline")
    private String brandingHeadline;

    @Column(name = "branding_tagline")
    private String brandingTagline;

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AvailabilityRuleEntity> availabilityRules = new ArrayList<>();

    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CatalogItemEntity> catalogItems = new ArrayList<>();

    public SucursalEntity() {
    }

    public SucursalEntity(UUID id, String nombre, String slug, String direccion, String ciudad,
                          String brandingPrimaryColor, String brandingHighlightColor, String brandingAccentColor,
                          String brandingHeadline, String brandingTagline) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.brandingPrimaryColor = brandingPrimaryColor;
        this.brandingHighlightColor = brandingHighlightColor;
        this.brandingAccentColor = brandingAccentColor;
        this.brandingHeadline = brandingHeadline;
        this.brandingTagline = brandingTagline;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSlug() {
        return slug;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getBrandingPrimaryColor() {
        return brandingPrimaryColor;
    }

    public String getBrandingHighlightColor() {
        return brandingHighlightColor;
    }

    public String getBrandingAccentColor() {
        return brandingAccentColor;
    }

    public String getBrandingHeadline() {
        return brandingHeadline;
    }

    public String getBrandingTagline() {
        return brandingTagline;
    }

    public List<AvailabilityRuleEntity> getAvailabilityRules() {
        return availabilityRules;
    }

    public List<CatalogItemEntity> getCatalogItems() {
        return catalogItems;
    }

    public void setAvailabilityRules(List<AvailabilityRuleEntity> availabilityRules) {
        this.availabilityRules = availabilityRules;
    }

    public void setCatalogItems(List<CatalogItemEntity> catalogItems) {
        this.catalogItems = catalogItems;
    }
}

