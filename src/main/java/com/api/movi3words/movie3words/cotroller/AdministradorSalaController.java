package com.api.movi3words.movie3words.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.movi3words.movie3words.service.ISalasService;

@RestController
@RequestMapping("api/salas")
public class AdministradorSalaController {

	@Autowired
	private ISalasService _salaService;
	
	public AdministradorSalaController(ISalasService _salaService) {
		this._salaService = _salaService;
	}
	
	
}
