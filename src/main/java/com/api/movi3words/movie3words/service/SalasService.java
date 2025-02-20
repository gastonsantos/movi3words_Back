package com.api.movi3words.movie3words.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class SalasService implements ISalasService {

	@Override
	public String CrearSala() {
		String chars= "abcdefghijklmnopqrstuvwxyz0123456789";
		Integer lenght = 20;
		Random random = new Random();
		StringBuilder sb = new StringBuilder(lenght);
		for (int i = 0; i < lenght; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();	
        
	}
	
	
}
