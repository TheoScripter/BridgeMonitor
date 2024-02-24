package fr.esisar.bridgemonitor.dto.plain.pont;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class DTOPlainHistoriqueEtatPont {
	public Long id;
	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime dateTimeChangement;
}
