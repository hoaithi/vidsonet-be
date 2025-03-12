package com.hoaithidev.vidsonet.repository;

import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {
//    boolean existsByName(String name);
    List<Channel> findAllByUserId(String id);

    boolean existsByName(String name);


}
