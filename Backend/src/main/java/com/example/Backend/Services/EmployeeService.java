package com.example.Backend.Services;

import com.example.Backend.Entity.*;
import com.example.Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private medicoRepository medicoRepository;
    @Autowired
    private personaltecnicoRepository tecnicoRepository;
    @Autowired
    private recepcionistaRepository recepcionistaRepository;
    @Autowired
    private farmaceuticoRepository farmaceuticoRepository;
    @Autowired
    private rolRepository rolRepository;
    @Autowired
    private especialidadRepository especialidadRepository;
    @Autowired
    private farmaciaRepository farmaciaRepository;

    public Map<String, Object> getAllEmployees() {
        Map<String, Object> employees = new HashMap<>();
        employees.put("medicos", medicoRepository.findAll());
        employees.put("tecnicos", tecnicoRepository.findAll());
        employees.put("recepcionistas", recepcionistaRepository.findAll());
        employees.put("farmaceuticos", farmaceuticoRepository.findAll());
        return employees;
    }

    public Object getEmployeeByRut(String rut) {
        Optional<medicoEntity> medico = medicoRepository.findByRut(rut);
        if (medico.isPresent()) return medico.get();

        Optional<personaltecnicoEntity> tecnico = tecnicoRepository.findByRut(rut);
        if (tecnico.isPresent()) return tecnico.get();

        Optional<recepcionistaEntity> recepcionista = recepcionistaRepository.findByRut(rut);
        if (recepcionista.isPresent()) return recepcionista.get();

        Optional<farmaceuticoEntity> farmaceutico = farmaceuticoRepository.findByRut(rut);
        if (farmaceutico.isPresent()) return farmaceutico.get();

        throw new IllegalArgumentException("No se encontró ningún empleado con el RUT: " + rut);
    }

    public Object registerEmployee(Map<String, Object> employeeData) {
        String rut = (String) employeeData.get("rut");
        String rol = (String) employeeData.get("rol");

        // Verificar si el RUT ya existe en cualquier tipo de empleado
        if (medicoRepository.findByRut(rut).isPresent() ||
            tecnicoRepository.findByRut(rut).isPresent() ||
            recepcionistaRepository.findByRut(rut).isPresent() ||
            farmaceuticoRepository.findByRut(rut).isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con el RUT: " + rut);
        }

        // Obtener el rol correspondiente
        String rolNombre;
        switch (rol.toLowerCase()) {
            case "medico":
                rolNombre = "Médico";
                break;
            case "tecnico":
                rolNombre = "Técnico";
                break;
            case "recepcionista":
                rolNombre = "Recepcionista";
                break;
            case "farmaceutico":
                rolNombre = "Farmacéutico";
                break;
            default:
                throw new IllegalArgumentException("Rol no válido: " + rol);
        }

        Optional<rolEntity> rolEntityOpt = rolRepository.findByNombre(rolNombre);
        if (!rolEntityOpt.isPresent()) {
            throw new IllegalArgumentException("No se encontró el rol: " + rolNombre);
        }
        rolEntity rolEntity = rolEntityOpt.get();

        // Registrar según el tipo de empleado
        switch (rol.toLowerCase()) {
            case "medico":
                return registerMedico(employeeData, rolEntity);
            case "tecnico":
                return registerTecnico(employeeData, rolEntity);
            case "recepcionista":
                return registerRecepcionista(employeeData, rolEntity);
            case "farmaceutico":
                return registerFarmaceutico(employeeData, rolEntity);
            default:
                throw new IllegalArgumentException("Rol no válido: " + rol);
        }
    }

    private medicoEntity registerMedico(Map<String, Object> data, rolEntity rol) {
        medicoEntity medico = new medicoEntity();
        String rutLimpio = ((String) data.get("rut")).replaceAll("[./-]", "");
        medico.setRut(rutLimpio);
        medico.setNombre((String) data.get("nombre"));
        medico.setApellido((String) data.get("apellido"));
        medico.setCorreo((String) data.get("correo"));
        medico.setTelefono((String) data.get("telefono"));
        medico.setPassword((String) data.get("password"));
        medico.setRol(rol); // <--- Asignar el rol correctamente

        // Manejar la especialidad
        String especialidadNombre = (String) data.get("especialidad");
        if (especialidadNombre != null && !especialidadNombre.trim().isEmpty()) {
            Optional<especialidadEntity> especialidadOpt = especialidadRepository.findByNombre(especialidadNombre);
            especialidadEntity especialidad;
            if (especialidadOpt.isPresent()) {
                especialidad = especialidadOpt.get();
            } else {
                especialidad = new especialidadEntity();
                especialidad.setNombre(especialidadNombre);
                especialidad = especialidadRepository.save(especialidad);
            }
            medico.setEspecialidad(especialidad);
        }

        return medicoRepository.save(medico);
    }

    private personaltecnicoEntity registerTecnico(Map<String, Object> data, rolEntity rol) {
        personaltecnicoEntity tecnico = new personaltecnicoEntity();
        String rutLimpio = ((String) data.get("rut")).replaceAll("[./-]", "");
        tecnico.setRut(rutLimpio);
        tecnico.setNombre((String) data.get("nombre"));
        tecnico.setApellido((String) data.get("apellido"));
        tecnico.setCorreo((String) data.get("correo"));
        tecnico.setTelefono((String) data.get("telefono"));
        tecnico.setPassword((String) data.get("password"));
        tecnico.setRol(rol);
        return tecnicoRepository.save(tecnico);
    }

    private recepcionistaEntity registerRecepcionista(Map<String, Object> data, rolEntity rol) {
        recepcionistaEntity recepcionista = new recepcionistaEntity();
        String rutLimpio = ((String) data.get("rut")).replaceAll("[./-]", "");
        recepcionista.setRut(rutLimpio);
        recepcionista.setNombre((String) data.get("nombre"));
        recepcionista.setApellido((String) data.get("apellido"));
        recepcionista.setCorreo((String) data.get("correo"));
        recepcionista.setTelefono((String) data.get("telefono"));
        recepcionista.setPassword((String) data.get("password"));
        recepcionista.setRol(rol);
        return recepcionistaRepository.save(recepcionista);
    }

    private farmaceuticoEntity registerFarmaceutico(Map<String, Object> data, rolEntity rol) {
        farmaceuticoEntity farmaceutico = new farmaceuticoEntity();
        String rutLimpio = ((String) data.get("rut")).replaceAll("[./-]", "");
        farmaceutico.setRut(rutLimpio);
        farmaceutico.setNombre((String) data.get("nombre"));
        farmaceutico.setApellido((String) data.get("apellido"));
        farmaceutico.setCorreo((String) data.get("correo"));
        farmaceutico.setTelefono((String) data.get("telefono"));
        farmaceutico.setPassword((String) data.get("password"));
        farmaceutico.setRol(rol);
        // Asociar farmacia si viene farmaciaId
        if (data.containsKey("farmaciaId") && data.get("farmaciaId") != null && !data.get("farmaciaId").toString().isEmpty()) {
            try {
                Long farmaciaId = Long.parseLong(data.get("farmaciaId").toString());
                Optional<farmaciaEntity> farmaciaOpt = farmaciaRepository.findById(farmaciaId);
                farmaciaOpt.ifPresent(farmaceutico::setFarmacia);
            } catch (NumberFormatException e) {
                // Si el id no es válido, no se asocia farmacia
            }
        }
        return farmaceuticoRepository.save(farmaceutico);
    }

    public Object updateEmployee(String rut, Map<String, Object> employeeData) {
        // Primero determinar el tipo de empleado
        Optional<medicoEntity> medico = medicoRepository.findByRut(rut);
        if (medico.isPresent()) {
            return updateMedico(medico.get(), employeeData);
        }

        Optional<personaltecnicoEntity> tecnico = tecnicoRepository.findByRut(rut);
        if (tecnico.isPresent()) {
            return updateTecnico(tecnico.get(), employeeData);
        }

        Optional<recepcionistaEntity> recepcionista = recepcionistaRepository.findByRut(rut);
        if (recepcionista.isPresent()) {
            return updateRecepcionista(recepcionista.get(), employeeData);
        }

        Optional<farmaceuticoEntity> farmaceutico = farmaceuticoRepository.findByRut(rut);
        if (farmaceutico.isPresent()) {
            return updateFarmaceutico(farmaceutico.get(), employeeData);
        }

        throw new IllegalArgumentException("No se encontró ningún empleado con el RUT: " + rut);
    }

    private medicoEntity updateMedico(medicoEntity medico, Map<String, Object> data) {
        if (data.containsKey("nombre")) medico.setNombre((String) data.get("nombre"));
        if (data.containsKey("apellido")) medico.setApellido((String) data.get("apellido"));
        if (data.containsKey("correo")) medico.setCorreo((String) data.get("correo"));
        if (data.containsKey("telefono")) medico.setTelefono((String) data.get("telefono"));
        if (data.containsKey("password")) medico.setPassword((String) data.get("password"));

        if (data.containsKey("especialidad")) {
            String especialidadNombre = (String) data.get("especialidad");
            if (especialidadNombre != null && !especialidadNombre.trim().isEmpty()) {
                Optional<especialidadEntity> especialidadOpt = especialidadRepository.findByNombre(especialidadNombre);
                especialidadEntity especialidad;
                if (especialidadOpt.isPresent()) {
                    especialidad = especialidadOpt.get();
                } else {
                    especialidad = new especialidadEntity();
                    especialidad.setNombre(especialidadNombre);
                    especialidad = especialidadRepository.save(especialidad);
                }
                medico.setEspecialidad(especialidad);
            }
        }

        return medicoRepository.save(medico);
    }

    private personaltecnicoEntity updateTecnico(personaltecnicoEntity tecnico, Map<String, Object> data) {
        if (data.containsKey("nombre")) tecnico.setNombre((String) data.get("nombre"));
        if (data.containsKey("apellido")) tecnico.setApellido((String) data.get("apellido"));
        if (data.containsKey("correo")) tecnico.setCorreo((String) data.get("correo"));
        if (data.containsKey("telefono")) tecnico.setTelefono((String) data.get("telefono"));
        if (data.containsKey("password")) tecnico.setPassword((String) data.get("password"));
        return tecnicoRepository.save(tecnico);
    }

    private recepcionistaEntity updateRecepcionista(recepcionistaEntity recepcionista, Map<String, Object> data) {
        if (data.containsKey("nombre")) recepcionista.setNombre((String) data.get("nombre"));
        if (data.containsKey("apellido")) recepcionista.setApellido((String) data.get("apellido"));
        if (data.containsKey("correo")) recepcionista.setCorreo((String) data.get("correo"));
        if (data.containsKey("telefono")) recepcionista.setTelefono((String) data.get("telefono"));
        if (data.containsKey("password")) recepcionista.setPassword((String) data.get("password"));
        return recepcionistaRepository.save(recepcionista);
    }

    private farmaceuticoEntity updateFarmaceutico(farmaceuticoEntity farmaceutico, Map<String, Object> data) {
        if (data.containsKey("nombre")) farmaceutico.setNombre((String) data.get("nombre"));
        if (data.containsKey("apellido")) farmaceutico.setApellido((String) data.get("apellido"));
        if (data.containsKey("correo")) farmaceutico.setCorreo((String) data.get("correo"));
        if (data.containsKey("telefono")) farmaceutico.setTelefono((String) data.get("telefono"));
        if (data.containsKey("password")) farmaceutico.setPassword((String) data.get("password"));
        // Actualizar farmacia si viene farmaciaId
        if (data.containsKey("farmaciaId") && data.get("farmaciaId") != null && !data.get("farmaciaId").toString().isEmpty()) {
            try {
                Long farmaciaId = Long.parseLong(data.get("farmaciaId").toString());
                Optional<farmaciaEntity> farmaciaOpt = farmaciaRepository.findById(farmaciaId);
                if (farmaciaOpt.isPresent()) {
                    farmaceutico.setFarmacia(farmaciaOpt.get());
                }
            } catch (NumberFormatException e) {
                // Si el id no es válido, no se actualiza la farmacia
            }
        }
        return farmaceuticoRepository.save(farmaceutico);
    }

    public void deleteEmployee(String rut) {
        Optional<medicoEntity> medico = medicoRepository.findByRut(rut);
        if (medico.isPresent()) {
            medicoRepository.delete(medico.get());
            return;
        }

        Optional<personaltecnicoEntity> tecnico = tecnicoRepository.findByRut(rut);
        if (tecnico.isPresent()) {
            tecnicoRepository.delete(tecnico.get());
            return;
        }

        Optional<recepcionistaEntity> recepcionista = recepcionistaRepository.findByRut(rut);
        if (recepcionista.isPresent()) {
            recepcionistaRepository.delete(recepcionista.get());
            return;
        }

        Optional<farmaceuticoEntity> farmaceutico = farmaceuticoRepository.findByRut(rut);
        if (farmaceutico.isPresent()) {
            farmaceuticoRepository.delete(farmaceutico.get());
            return;
        }

        throw new IllegalArgumentException("No se encontró ningún empleado con el RUT: " + rut);
    }
}