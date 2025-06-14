package com.example.Backend.Services;

import com.example.Backend.Entity.*;
import com.example.Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private administradorRepository adminRepository;
    
    @Autowired
    private medicoRepository medicoRepo;
    
    @Autowired
    private pacienteRepository pacienteRepo;
    
    @Autowired
    private personaltecnicoRepository tecnicoRepo;
    
    @Autowired
    private recepcionistaRepository recepcionistaRepo;
    
    @Autowired
    private farmaceuticoRepository farmaceuticoRepo;
    
    @Autowired
    private rolRepository rolRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Object login(String rut, String password, String rol) {
        switch (rol) {
            case "admin":
                return loginAdmin(rut, password);
            case "doctor":
                return loginMedico(rut, password);
            case "patient":
                return loginPaciente(rut, password);
            case "technician":
                return loginTecnico(rut, password);
            case "receptionist":
                return loginRecepcionista(rut, password);
            case "pharmacy":
                return loginFarmaceutico(rut, password);
            default:
                return null;
        }
    }

    private Object loginAdmin(String rut, String password) {
        Optional<administradorEntity> admin = adminRepository.findByRut(rut);
        if (admin.isPresent() && password.equals(admin.get().getPassword())) {
            return admin.get();
        }
        return null;
    }

    private Object loginMedico(String rut, String password) {
        Optional<medicoEntity> medico = medicoRepo.findByRut(rut);
        if (medico.isPresent() && password.equals(medico.get().getPassword())) {
            return medico.get();
        }
        return null;
    }

    private Object loginPaciente(String rut, String password) {
        Optional<pacienteEntity> paciente = pacienteRepo.findByRut(rut);
        if (paciente.isPresent() && password.equals(paciente.get().getPassword())) {
            return paciente.get();
        }
        return null;
    }

    private Object loginTecnico(String rut, String password) {
        Optional<personaltecnicoEntity> tecnico = tecnicoRepo.findByRut(rut);
        if (tecnico.isPresent() && password.equals(tecnico.get().getPassword())) {
            return tecnico.get();
        }
        return null;
    }

    private Object loginRecepcionista(String rut, String password) {
        Optional<recepcionistaEntity> recepcionista = recepcionistaRepo.findByRut(rut);
        if (recepcionista.isPresent() && password.equals(recepcionista.get().getPassword())) {
            return recepcionista.get();
        }
        return null;
    }

    private Object loginFarmaceutico(String rut, String password) {
        Optional<farmaceuticoEntity> farmaceutico = farmaceuticoRepo.findByRut(rut);
        if (farmaceutico.isPresent() && password.equals(farmaceutico.get().getPassword())) {
            return farmaceutico.get();
        }
        return null;
    }

    public pacienteEntity registrarPaciente(pacienteEntity paciente) {
        System.out.println("Intentando registrar paciente con correo: " + paciente.getCorreo() + ", rut: " + paciente.getRut());
        
        // Validar campos requeridos
        if (paciente.getNombrePa() == null || paciente.getNombrePa().trim().isEmpty()) {
            System.out.println("El nombre es requerido");
            throw new IllegalArgumentException("nombre");
        }
        if (paciente.getApellidoPa() == null || paciente.getApellidoPa().trim().isEmpty()) {
            System.out.println("El apellido es requerido");
            throw new IllegalArgumentException("apellido");
        }
        if (paciente.getRut() == null || paciente.getRut().trim().isEmpty()) {
            System.out.println("El RUT es requerido");
            throw new IllegalArgumentException("rut");
        }
        if (paciente.getCorreo() == null || paciente.getCorreo().trim().isEmpty()) {
            System.out.println("El correo es requerido");
            throw new IllegalArgumentException("correo");
        }
        if (paciente.getPassword() == null || paciente.getPassword().trim().isEmpty()) {
            System.out.println("La contraseña es requerida");
            throw new IllegalArgumentException("password");
        }

        if (pacienteRepo.findByRut(paciente.getRut()).isPresent()) {
            System.out.println("Ya existe un paciente con ese rut");
            throw new IllegalArgumentException("rut");
        }
        if (pacienteRepo.existsByCorreo(paciente.getCorreo())) {
            System.out.println("Ya existe un paciente con ese correo");
            throw new IllegalArgumentException("correo");
        }

        // Obtener el rol de paciente
        Optional<rolEntity> rolPaciente = rolRepository.findByNombre("patient");
        if (rolPaciente.isPresent()) {
            paciente.setRol(rolPaciente.get());
            // Guardar la contraseña en texto plano
            // paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
            // Guardar el paciente
            pacienteEntity guardado = pacienteRepo.save(paciente);
            System.out.println("Paciente guardado: " + guardado);
            return guardado;
        }
        return null;
    }
} 