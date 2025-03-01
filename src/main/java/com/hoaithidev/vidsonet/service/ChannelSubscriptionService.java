package com.hoaithidev.vidsonet.service;

import com.hoaithidev.vidsonet.dto.request.SubscribeRequest;
import com.hoaithidev.vidsonet.entity.Channel;
import com.hoaithidev.vidsonet.entity.ChannelSubscription;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.ChannelRepository;
import com.hoaithidev.vidsonet.repository.ChannelSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelSubscriptionService {
    private final ChannelRepository channelRepository;
    private final ChannelSubscriptionRepository channelSubscriptionRepository;

    public boolean subscribe(SubscribeRequest request) {
        String subscriberId = request.getSubscriberId();
        String subscribedId = request.getSubscribedId();
        if (subscriberId.equals(subscribedId)) {
            throw new IllegalArgumentException("A channel cannot subscribe to itself.");
        }

        Channel subscriberChannel = channelRepository.findById(subscriberId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        Channel subscribedChannel = channelRepository.findById(subscribedId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        // Kiểm tra xem đã subscribe chưa
        if (channelSubscriptionRepository.existsBySubscriberChannelAndSubscribedChannel(subscriberChannel, subscribedChannel)) {
            throw new AppException(ErrorCode.CHANNEL_SUBSCRIBED_ALREADY);
        }


        ChannelSubscription subscription = ChannelSubscription.builder()
                .subscriberChannel(subscriberChannel)
                .subscribedChannel(subscribedChannel)
                .build();

        channelSubscriptionRepository.save(subscription);
        subscribedChannel.increaseSubscribersCount();
        channelRepository.save(subscriberChannel);
        return true;
    }

    public boolean unsubscribe(SubscribeRequest request) {
        String subscriberId = request.getSubscriberId();
        String subscribedId = request.getSubscribedId();
        if (subscriberId.equals(subscribedId)) {
            throw new IllegalArgumentException("A channel cannot unsubscribe to itself.");
        }

        Channel subscriberChannel = channelRepository.findById(subscriberId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        Channel subscribedChannel = channelRepository.findById(subscribedId)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        // Kiểm tra xem đã subscribe chưa
        if (!channelSubscriptionRepository.existsBySubscriberChannelAndSubscribedChannel(subscriberChannel, subscribedChannel)) {
            throw new AppException(ErrorCode.CHANNEL_NOT_SUBSCRIBED_YET);
        }
        ChannelSubscription channelSubscription = channelSubscriptionRepository
                .findChannelSubscriptionBySubscribedChannelIdAndSubscriberChannelId(subscribedId, subscriberId);

        channelSubscriptionRepository.delete(channelSubscription);
        subscribedChannel.decreaseSubscribersCount();
        return true;
    }
}
