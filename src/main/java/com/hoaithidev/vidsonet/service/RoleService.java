package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.RoleRequest;
import com.hoaithidev.vidsonet.dto.response.RoleResponse;
import com.hoaithidev.vidsonet.entity.Role;
import com.hoaithidev.vidsonet.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    RoleRepository roleRepository;

    public boolean create(RoleRequest request) {
        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        try {
            if (roleRepository.existsById(role.getName())) {
                log.error("Role already exists");
                return false;
            }
            roleRepository.save(role);
        }catch (Exception e){
            log.error("Error creating role", e);
            return false;
        }
        return true;
    }

    public Set<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(role -> RoleResponse.builder()
                        .name(role.getName())
                        .description(role.getDescription())
                        .build())
                .collect(Collectors.toSet());
    }

    public void delete(String name) throws Exception {
        if(!roleRepository.existsById(name)){
            throw new Exception("Role does not exist");
        }
        roleRepository.deleteById(name);
    }
}
