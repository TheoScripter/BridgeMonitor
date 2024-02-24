package fr.esisar.bridgemonitor.dto.plain.pont;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.validation.constraints.NotBlank;

public class DTOPlainPont {
	public Long id;
	
	@NotBlank //ne doit pas etre une chaine vide
	public String nom;
    public Float longueur;
    public Float largeur;
    public String latitude;
    public String longitude;
    
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dateCreation;    
       
}
