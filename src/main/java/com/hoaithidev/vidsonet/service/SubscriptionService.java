package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.SubscriptionRequest;
import com.hoaithidev.vidsonet.dto.response.ChannelResponse;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.Subscription;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final ChannelRepository channelRepository;
    private final SubscriptionRepository subscriptionRepository;

    public boolean subscribe(SubscriptionRequest request) {
        String subscriberId = request.getSubscriberId();
        String channelId = request.getChannelId();
        if (subscriberId.equals(channelId)) {
            throw new IllegalArgumentException("A channel cannot subscribe to itself.");
        }

        Channel subscriber = channelRepository.findById(subscriberId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        // Kiểm tra xem đã subscribe chưa
        if (subscriptionRepository.existsBySubscriberAndChannel(subscriber, channel)) {
            throw new AppException(ErrorCode.CHANNEL_SUBSCRIBED_ALREADY);
        }


        Subscription subscription = Subscription.builder()
                .subscriber(subscriber)
                .channel(channel)
                .build();

        subscriptionRepository.save(subscription);
        channel.setSubscribersCount(channel.getSubscribers().size());
        channelRepository.save(channel);
        return true;
    }

    public boolean unsubscribe(SubscriptionRequest request) {
        String subscriberId = request.getSubscriberId();
        String channelId = request.getChannelId();
        if (subscriberId.equals(channelId)) {
            throw new IllegalArgumentException("A channel cannot unsubscribe to itself.");
        }

        Channel subscriber = channelRepository.findById(subscriberId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        // Kiểm tra xem đã subscribe chưa
        if (!subscriptionRepository.existsBySubscriberAndChannel(subscriber, channel)) {
            throw new AppException(ErrorCode.CHANNEL_NOT_SUBSCRIBED_YET);
        }
        Subscription subscription = subscriptionRepository
                .findSubscriptionByChannelAndSubscriber(channel, subscriber);

        subscriptionRepository.delete(subscription);
        channel.setSubscribersCount(channel.getSubscribers().size());
        channelRepository.save(channel);
        return true;
    }

    public List<ChannelResponse> getSubscribers(String channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));
        List<ChannelResponse> channelResponses = new ArrayList<>();
        for (Subscription subscription : channel.getSubscribers()) {
            channelResponses.add(ChannelResponse.builder()
                    .id(subscription.getSubscriber().getId())
                    .name(subscription.getSubscriber().getName())
                    .build());
        }
        return channelResponses;

    }

}
