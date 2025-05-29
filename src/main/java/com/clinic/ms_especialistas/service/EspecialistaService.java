package com.clinic.ms_especialistas.service;

import com.clinic.ms_especialistas.model.Especialista;
import com.clinic.ms_especialistas.repository.EspecialistaRepository;
import com.clinic.ms_especialistas.service.redis.RedisService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EspecialistaService {

    @Autowired
    private EspecialistaRepository especialistaRepository;

    @Autowired
    private RedisService redisService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String ESPECIALISTAS_KEY = "ESPECIALISTAS";

    public List<Especialista> getAllEspecialistas() {
        // Check if the data is already in Redis
        List<Especialista> especialistas = (List<Especialista>) redisService.get(ESPECIALISTAS_KEY);

        if (especialistas == null) {
            // If not, fetch from the repository and save to Redis
            especialistas = especialistaRepository.getEspecialistas();
            redisService.save(ESPECIALISTAS_KEY, especialistas, 1, TimeUnit.HOURS);
            System.out.println("LLama a BBDD");// Cache for 1 hour
        }
        System.out.println("LLama a Redis");
        return especialistas;
    }

    public List<Especialista> findEspecialistasByFiltro(String trackingId, Map<String, Object> filtros) {
        StringBuilder sql = new StringBuilder(
                "SELECT e.* " +
                    "FROM CLINIC_ESPECIALISTA e " +
                    "WHERE 1 = 1 AND e.DELETE_TS IS NULL");

        Map<String, Object> paramsQuery = new HashMap<>();

        if (!filtros.isEmpty()) {

            if (filtros.containsKey("nombre")) {
                sql.append(" AND e.NOMBRE LIKE :nombre");
                paramsQuery.put("nombre", "%" + filtros.get("nombre") + "%");
            }

            if (filtros.containsKey("apellidos")) {
                sql.append(" AND e.APELLIDOS LIKE :apellidos");
                paramsQuery.put("apellidos", "%" + filtros.get("apellidos") + "%");
            }

            if (filtros.containsKey("dni")) {
                sql.append(" AND e.DNI = :dni");
                paramsQuery.put("dni", filtros.get("dni"));
            }

            if (filtros.containsKey("especialidad")) {
                sql.append(" AND e.ESPECIALIDAD = :especialidad");
                paramsQuery.put("especialidad", filtros.get("especialidad"));
            }

            sql.append(" ORDER BY e.ID");
            /*sql.append(" OFFSET :page ROWS FETCH NEXT :size ROWS ONLY");*/

            Query query = entityManager.createNativeQuery(sql.toString(), Especialista.class);
            /*query.setParameter("page", page);
            query.setParameter("size", size);*/
            paramsQuery.forEach(query::setParameter);

            System.out.println(sql.toString());
            System.out.println("LLama a BBDD");

            return query.getResultList();
        } else {
            List<Especialista> especialistas = (List<Especialista>) redisService.get(ESPECIALISTAS_KEY);

            if (especialistas == null) {
                Query query = entityManager.createNativeQuery(sql.toString(), Especialista.class);
                especialistas = query.getResultList();
                redisService.save(ESPECIALISTAS_KEY, especialistas, 1, TimeUnit.HOURS);
                System.out.println("LLama a BBDD");
            }

            System.out.println("LLama a Redis");
            return especialistas;
        }
    }
}
