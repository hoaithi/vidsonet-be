package com.hoaithidev.vidsonet.repository;

import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.ChannelSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelSubscriptionRepository extends JpaRepository<ChannelSubscription, Long> {
    boolean existsBySubscriberChannelAndSubscribedChannel(Channel subscriberChannel, Channel subscribedChannel);
}
