package um.ssdd.proyecto_ssdd.Spring.Controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import um.ssdd.proyecto_ssdd.Spring.Services.FicheroService;
import um.ssdd.proyecto_ssdd.Spring.Services.UsuarioService;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.CodigoDTO;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.FicheroDTO;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.UsuarioLogin;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.UsuarioPost;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.UsuarioPut;
import um.ssdd.proyecto_ssdd.Spring.Services.DTOs.UsuarioResponse;


@CrossOrigin(origins = "http://localhost:4200", maxAge =3600)
@RestController
@RequestMapping("/api/usuarios")
public class Controller {
	
	@Autowired
	private UsuarioService usuarioService;	
	
	@Autowired
	private FicheroService ficheroService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
		
		
		
		return ResponseEntity.ok(usuarioService.getAll());
		
	}
	

/*
	 | |  | |                         (_)       
	 | |  | | ___  _   _   __ _  _ __  _   ___  
	 | |  | |/ __|| | | | / _` || '__|| | / _ \ 
	 | |__| |\__ \| |_| || (_| || |   | || (_) |
	  \____/ |___/ \__,_| \__,_||_|   |_| \___/ 
*/
	

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> getUsuario(@PathVariable("id") String id) {
		
		UsuarioResponse u = usuarioService.get(id);
		
		if (u != null)
			return ResponseEntity.ok(u);
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<UsuarioResponse> postUsuario(@RequestBody UsuarioPost usuario) {
		
		UsuarioResponse user = usuarioService.post(usuario);
		
		if (user == null)
			return ResponseEntity.status(409).build();
		
		return ResponseEntity.ok(user);
		
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> putUsuario(@PathVariable("id") String id, @RequestBody UsuarioPut usuario) {

		switch(usuarioService.put(id, usuario)) {
		case 0:
			return ResponseEntity.badRequest().build();
		case 1:
			return ResponseEntity.notFound().build();
		case 2:
			return ResponseEntity.noContent().build();
		}
		return null;
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<String> patchUsuario(@PathVariable("id") String id, @RequestBody UsuarioPut usuario) {

		switch(usuarioService.patch(id, usuario)) {
		case 0:
			return ResponseEntity.badRequest().build();
		case 1:
			return ResponseEntity.notFound().build();
		case 2:
			return ResponseEntity.noContent().build();
		}
		return null;
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUsuario(@PathVariable("id") String id) {
		
		if (usuarioService.delete(id))
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.notFound().build();
	}
	
	
	@PostMapping("code")
	public ResponseEntity<String> checkConfirmationCode(@RequestBody CodigoDTO codigoDTO) {
		
		if (usuarioService.checkCode(codigoDTO))
			return ResponseEntity.ok().build();
		
		
		return ResponseEntity.notFound().build();
		
		
		
	}
	
/*
	  _
	 | |                  (_)       
	 | |      ___    __ _  _  _ __  
	 | |     / _ \  / _` || || '_ \ 
	 | |____| (_) || (_| || || | | |
	 |______|\___/  \__, ||_||_| |_|
	                 __/ |          
	                |___/  
*/
	
	
	@PostMapping("/login")
	public ResponseEntity<UsuarioResponse> loginUsuario(@RequestBody UsuarioLogin usuario) {
		
		UsuarioResponse user = usuarioService.login(usuario);
		if (user == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(user);

	}

/*	
	  ______  _        _                       
	  |  ____|(_)      | |                      
	  | |__    _   ___ | |__    ___  _ __  ___  
	  |  __|  | | / __|| '_ \  / _ \| '__|/ _ \ 
	  | |     | || (__ | | | ||  __/| |  | (_) |
	  |_|     |_| \___||_| |_| \___||_|   \___/
*/
	
	@PostMapping("/{id}/file")
	public ResponseEntity<String> uploadFile(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
		
		

		URI uri = ficheroService.post(id, file);
		
		if (uri != null) 
			return ResponseEntity.created(uri).build();
				
		
		return ResponseEntity.badRequest().build();
		
	}
	
	
	@GetMapping("/files")
	public ResponseEntity<List<FicheroDTO>> getAllFicheros(){
		
		return ResponseEntity.ok(ficheroService.getAll());
		
	}
	
	@GetMapping("/{id}/files")
	public ResponseEntity<List<FicheroDTO>> getAllFicherosUsuario(@PathVariable("id") String id) {
			
		return ResponseEntity.ok(ficheroService.getAllUsuario(id));
		
	}
	
	
	@GetMapping("/{id}/file/{fid}")
	public ResponseEntity<String> getFicheroText(@PathVariable("id") String id, @PathVariable("fid") String fid) {
		String text = ficheroService.getText(id, fid);
		if ( text != null)
			return ResponseEntity.ok(text);
		return ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{id}/file/{fid}")
	public ResponseEntity<String> deleteFichero(@PathVariable("id") String id, @PathVariable("fid") String fid) {
		
		
		if (ficheroService.delete(id, fid)) {
			
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
		
	}

}
