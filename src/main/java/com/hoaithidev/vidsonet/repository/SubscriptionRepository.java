package com.hoaithidev.vidsonet.repository;

import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsBySubscriberAndChannel(Channel subscriber, Channel channel);

    Subscription findSubscriptionByChannelAndSubscriber(Channel channel, Channel subscriber);

}
